(ns download-forex-data.core
  (require [clj-time.core :as time]
           [clj-time.periodic :as time-period]
           [clj-time.format :as fmt]))

(defn time-range
  "Return a lazy sequence of DateTime's from start to end, incremented
  by 'step' units of time."
  ; http://www.rkn.io/2014/02/13/clojure-cookbook-date-ranges/
  [start end step]
  (let [inf-range (time-period/periodic-seq start step)
        below-end? (fn [t] (time/within? (time/interval start end) t))]
          (take-while below-end? inf-range)))

(defn true-fx-months-available []
  "TrueFX data begins in May 2009, and is available up until last month"
  (let [one-month (time/months 1)]
    (time-range (time/date-time 2009 5)
                (time/minus (time/now) one-month) ; minus, as we always want last month
                one-month)))

(defn true-fx-url
  "Return a string that will contain tick data.
  fx-pair needs to be a valid forex pair, such as GBPUSD"
  [datetime fx-pair]
  (let [month-name (clojure.string/upper-case (fmt/unparse (fmt/formatter "MMMM") datetime))
        month-num  (fmt/unparse (fmt/formatter "MM") datetime)
        year       (time/year datetime)]
    ; http://truefx.com/dev/data/{year}/{month}-{year}/{pair}-{year}-{month-as-int}.zip
    (format "http://truefx.com/dev/data/%d/%s-%d/%s-%d-%s.zip" year month-name year fx-pair year month-num)))

(defn -main
  [& args]
  (doseq [url (map #(true-fx-url % (first args)) (true-fx-months-available))]
    (println url)))

