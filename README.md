# zmq-jni

[![Build Status](https://travis-ci.org/trevorbernard/zmq-jni.png)](https://travis-ci.org/trevorbernard/zmq-jni)

Simple High Performance JNI Wrapper for ØMQ

## Motivation

The goal of this project is to simplify and make easy the development of high
performance ØMQ binding on the JVM. Ideally when this project stabilizes, it
will underpin libraries such as: [jzmq](https://github.com/zeromq/jzmq),
[cljzmq](https://github.com/zeromq/cljzmq) and
[zmq-async](https://github.com/lynaghk/zmq-async).

## Building

```bash
cmake .
mvn compile
mvn clojure:test
```
## Deploy locally

```bash
mvn install
```

## TODO

* Build 32 and 64 bit libraries for Windows, Linux and Mac OS X. Currently I
  only include Linux/amd64 inside the JAR

## Acknowledgements

YourKit is kindly supporting ZeroMQ project with its full-featured [Java Profiler](http://www.yourkit.com/java/profiler/index.jsp).

## License

Copyright © 2013 Trevor Bernard

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
