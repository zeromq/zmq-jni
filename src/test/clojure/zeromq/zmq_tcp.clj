(ns zeromq.zmq-tcp
  (:require [zeromq.zmq :as zmq])
  (:use clojure.test))

(deftest push-pull
  (with-open [context (zmq/context)
              push (doto (zmq/socket context :push)
                     (zmq/connect "tcp://localhost:6001"))
              pull (doto (zmq/socket context :pull)
                     (zmq/bind "tcp://*:6001"))]
    (zmq/send push (.getBytes "helloworld") 0)
    (let [buf (zmq/receive pull 0)]
      (is (= "helloworld" (String. buf))))))
