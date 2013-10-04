;;
;; Copyright 2013 Trevor Bernard
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;     http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

(ns zeromq.zmq-tcp
  (:require [zeromq.zmq :as zmq])
  (:use clojure.test)
  (:import java.nio.ByteBuffer))

(deftest push-pull-test
  (with-open [context (zmq/context)
              push (doto (zmq/socket context :push)
                     (zmq/connect "tcp://localhost:6001"))
              pull (doto (zmq/socket context :pull)
                     (zmq/bind "tcp://*:6001"))]
    (zmq/send push (.getBytes "helloworld") 0)
    (let [buf (zmq/receive pull 0)]
      (is (= "helloworld" (String. buf))))))

(deftest send-bb-test
  (with-open [context (zmq/context)
              push (doto (zmq/socket context :push)
                     (zmq/connect "tcp://localhost:6001"))
              pull (doto (zmq/socket context :pull)
                     (zmq/bind "tcp://*:6001"))]
    (let [bb (doto (ByteBuffer/allocateDirect 10)
               (.put (.getBytes "helloworld"))
               (.flip))]
      (zmq/send-bb push bb 0))
    (let [buf (zmq/receive pull 0)]
      (is (= "helloworld" (String. buf))))))

(deftest receive-bb-test
  (with-open [context (zmq/context)
              push (doto (zmq/socket context :push)
                     (zmq/connect "tcp://localhost:6001"))
              pull (doto (zmq/socket context :pull)
                     (zmq/bind "tcp://*:6001"))]
    (zmq/send push (.getBytes "helloworld") 0)
    (let [bb (ByteBuffer/allocateDirect 10)
          _ (zmq/receive-bb pull bb 0)
          buf (byte-array 10)]
      (.flip bb)
      (.get bb buf)
      (is (= "helloworld" (String. buf))))))

(def send-more 2)

(deftest pub-sub-test
  (with-open [context (zmq/context)
              sub (doto (zmq/socket context :sub)
                     (zmq/connect "tcp://localhost:6001")
                     (zmq/subscribe (.getBytes "A")))
              pub (doto (zmq/socket context :pub)
                     (zmq/bind "tcp://*:6001"))]
    (Thread/sleep 200)
    (zmq/send pub (.getBytes "A") send-more)
    (zmq/send pub (.getBytes "helloworld") 0)
    (let [_ (zmq/receive sub 0)
          actual (zmq/receive sub 0)]
      (is (= "helloworld" (String. actual))))))
