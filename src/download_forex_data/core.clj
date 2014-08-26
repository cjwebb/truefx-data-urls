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

(def true-fx-months-available
  (time-range (time/date-time 2009 5)
              (time/date-time 2014 8) ; make this automatically adjust to current date
              (time/months 1))) 

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
  (doseq [url (map #(true-fx-url % (first args)) true-fx-months-available)]
    (println url)))

