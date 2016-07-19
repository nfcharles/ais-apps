# ais-apps

ais-apps is a collection of AIS (Automatic Identification System) applications built using clj-ais (https://github.com/nfcharles/clj-ais)

#### Supported Types

See https://github.com/nfcharles/clj-ais for supported types.  Type support can be extended.

## Installation

ais-apps is not available in public clojure repos yet.  Fork repo and build.

```bash
$ cd /path/to/ais-apps
$ lein ubjarbar
```

## Usage

```bash
Usage: ais-decode [options] INPUT MESSAGE-TYPES OUTPUT-NAME

Description:
  Decodes ais sentences from input source.  At least 1 message type must be specified.

  e.g.
    bin/ais-decode --output-format csv
                   --threads 3
                   /tmp/sample-input.txt
                   1,2,3,5
                   decoded-messages

  The prior command generates a file named decoded-messages.csv in cwd with decoded ais sentences
  of type 1,2,3,5, using 3 threads to decode the sentences.

Options:
 -o, --output-format <format>   Output file format: 'csv' or 'json'
 -t, --threads <int>            Total count of decoding threads.
 -h, --help                     Show help.

Required:
 INPUT         Path to input file.
 MESSAGE-TYPES Comma separated list of message types.  For example, 1,5 decodes ais message
               types 1 and 5.
 OUTPUT-NAME   Output filename
```

## Examples

###  Decoding messages
#### type 1, 2, 3 

    $ bin/ais-decode --threads 2 --output-format json input/nmea-sample 1,2,3 sample-1-2-3

```bash
AIS_LIB_VER=0.8.3
JAVA_OPTS=-Xms2048m -Xmx4096m
RELEASE_JAR=target/uberjar/ais-apps-0.1.0-SNAPSHOT-standalone.jar
INPUT=input/nmea-sample
MESSAGE_TYPES=1,2,3
THREADS=2
OUTPUT_FORMAT=json
OUTPUT_NAME=sample-1-2-3
INCLUDE TYPES: #{1 3 2}
16-07-19 03:58:11 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 03:58:12 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 03:58:13 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 03:58:13 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 03:58:13 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 03:58:14 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 03:58:15 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:206] - count.invalid.total=7
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:207] - count.dropped.total=13027
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:280] - count.decoder.thread_1=36137
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:280] - count.decoder.thread_0=35995
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:293] - count.collector=36137
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:293] - count.collector=35995
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:305] - writing sample-1-2-3-part-0.json
16-07-19 03:58:15 navil-ubuntu INFO [apps.decoding:305] - writing sample-1-2-3-part-1.json
16-07-19 03:58:16 navil-ubuntu INFO [apps.decoding:307] - count.writer.thread_0=36137
16-07-19 03:58:16 navil-ubuntu INFO [apps.decoding:307] - count.writer.thread_1=35995
"Elapsed time: 5754.551919 msecs"
```
  
#### type 5

    $ bin/ais-decode --threads 2 --output-format json input/nmea-sample 5 sample-5

```bash
AIS_LIB_VER=0.8.3
JAVA_OPTS=-Xms2048m -Xmx4096m
RELEASE_JAR=target/uberjar/ais-apps-0.1.0-SNAPSHOT-standalone.jar
INPUT=input/nmea-sample
MESSAGE_TYPES=5
THREADS=2
OUTPUT_FORMAT=json
OUTPUT_NAME=sample-5
INCLUDE TYPES: #{5}
16-07-19 04:00:30 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 04:00:30 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 04:00:31 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 04:00:31 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 04:00:32 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 04:00:32 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 04:00:32 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,A,D02M45ikTNfr<`N000000000000,2*29
16-07-19 04:00:32 navil-ubuntu DEBUG [apps.decoding:255] - Invalid message syntax: !BSVDM,1,1,,B,D02M45iWPNfr<`N000000000000,2*12
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:206] - count.invalid.total=8
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:207] - count.dropped.total=80775
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:280] - count.decoder.thread_0=1151
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:280] - count.decoder.thread_1=1063
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:293] - count.collector=1151
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:293] - count.collector=1063
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:305] - writing sample-5-part-0.json
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:305] - writing sample-5-part-1.json
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:307] - count.writer.thread_1=1063
16-07-19 04:00:32 navil-ubuntu INFO [apps.decoding:307] - count.writer.thread_0=1151
"Elapsed time: 2400.86191 msecs"
```

## Extending

See https://github.com/nfcharles/clj-ais for extending type support.

## License

Copyright © 2016 Navil Charles

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
