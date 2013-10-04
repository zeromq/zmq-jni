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
