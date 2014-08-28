# TrueFX Data URLs

Generate and output URLs to TrueFX tick data.

## To Run
    $ lein deps
    $ lein run "GBPUSD" > output.txt

This will generate about 60 URLS to download just the `GBPUSD` currency pair. This project currently only accepts one pair at a time, and they must be in a similar format to above. For example, another pair would be `EURGBP`.

And then to download, and unpack all the data

    $ cat output.txt | xargs -Iuri wget uri
    $ unzip "*.zip"

Be warned, downloading and unzipping one currency pair will take up ~10GB in disk space.

## To Do
The current code will only download tickdata from May 2009 to July 2014. When TrueFX upload new data, the code will need changing. It should be relatively simple to change the code to adjust automatically.

