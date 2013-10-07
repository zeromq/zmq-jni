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

(ns zeromq.zmq
  (:refer-clojure :exclude [send])
  (:import org.zeromq.jni.ZMQ
           java.io.Closeable
           java.nio.ByteBuffer))

(def ^:const context-opts
  {:zmq-io-threads 1
   :zmq-max-sockets 2})

(defprotocol Context
  (io-threads
    [_]
    [_ value]))

(defrecord ManagedContext [^long context-ptr closed?]
  Context
  (io-threads [this]
    (let [opt (:zmq-io-threads context-opts)]
      (ZMQ/zmq_ctx_get context-ptr opt)))
  (io-threads [this value]
    (let [opt (:zmq-io-threads context-opts)]
      (ZMQ/zmq_ctx_set context-ptr opt (int value))))
  Closeable
  (close [this]
    (when (compare-and-set! closed? false true)
      (ZMQ/zmq_ctx_destroy context-ptr))))

(defn context
  ([]
     (context 1))
  ([value]
     (doto (->ManagedContext (ZMQ/zmq_ctx_new) (atom false))
       (io-threads (int value)))))

(def ^:const socket-types
  {:pair ZMQ/PAIR
   :pub ZMQ/PUB
   :sub ZMQ/SUB
   :req ZMQ/REQ
   :rep ZMQ/REP
   :dealer ZMQ/DEALER
   :router ZMQ/ROUTER
   :pull ZMQ/PULL
   :push ZMQ/PUSH
   :xpub ZMQ/XPUB
   :xsub ZMQ/XSUB
   :xreq ZMQ/XREQ
   :xrep ZMQ/XREP})

(def ^:const sock-opts
  {:affinity ZMQ/AFFINITY
   :identity ZMQ/IDENTITY
   :subscribe ZMQ/SUBSCRIBE
   :unsubscribe ZMQ/UNSUBSCRIBE
   :rate ZMQ/RATE
   :recovery-ivl ZMQ/RECOVERY_IVL
   :sndbuf ZMQ/SNDBUF
   :rcvbuf ZMQ/RCVBUF
   :rcvmore ZMQ/RCVMORE
   :fd ZMQ/FD
   :events ZMQ/EVENTS
   :type ZMQ/TYPE
   :linger ZMQ/LINGER
   :reconnect-ivl ZMQ/RECONNECT_IVL
   :backlog ZMQ/BACKLOG
   :reconnect-ivl-max ZMQ/RECONNECT_IVL_MAX
   :maxmsgsize ZMQ/MAXMSGSIZE
   :sndhwm ZMQ/SNDHWM
   :rcvhwm ZMQ/RCVHWM
   :multicast-hops ZMQ/MULTICAST_HOPS
   :rcvtimeo ZMQ/RCVTIMEO
   :sndtimeo ZMQ/SNDTIMEO
   :ipv4only ZMQ/IPV4ONLY
   :last-endpoint ZMQ/LAST_ENDPOINT
   :router-mandatory ZMQ/ROUTER_MANDATORY
   :tcp-keepalive ZMQ/TCP_KEEPALIVE
   :tcp-keepalive-cnt ZMQ/TCP_KEEPALIVE_CNT
   :tcp-keepalive-idle ZMQ/TCP_KEEPALIVE_IDLE
   :tcp-keepalive-intvl ZMQ/TCP_KEEPALIVE_INTVL
   :tcp-accept-filter ZMQ/TCP_ACCEPT_FILTER
   :delay-attach-on-connect ZMQ/DELAY_ATTACH_ON_CONNECT
   :xpub-verbose ZMQ/XPUB_VERBOSE})

(defprotocol Socket
  (send [this buffer flags])
  (send-bb [this bb flags])
  (receive
    [this buffer flags]
    [this flags])
  (receive-bb [this bb flags])
  (connect [this endpoint])
  (bind [this endpoint])
  (subscribe [this topic])
  (unsubscribe [this topic])
  (receive-more? [this])
  (errno [this]))

(defrecord ManagedSocket [^long socket-ptr closed?]
  Socket
  (send [this buffer flags]
    (ZMQ/zmq_send socket-ptr buffer (int 0) (int (count buffer)) (int flags)))
  (send-bb [this bb flags]
    (ZMQ/zmq_send socket-ptr ^ByteBuffer bb flags))
  (receive [this flags]
    (ZMQ/zmq_recv socket-ptr (int flags)))
  (receive [this buffer flags]
    (ZMQ/zmq_recv socket-ptr buffer (int 0) (int (count buffer)) (int flags)))
  (receive-bb [this bb flags]
    (ZMQ/zmq_recv socket-ptr ^ByteBuffer bb flags))
  (connect [this endpoint]
    (ZMQ/zmq_connect socket-ptr ^String endpoint))
  (bind [this endpoint]
    (ZMQ/zmq_bind socket-ptr ^String endpoint))
  (subscribe [this topic]
    (let [sockopt (:subscribe sock-opts)]
      (ZMQ/zmq_setsockopt socket-ptr (int sockopt) ^bytes topic)))
  (unsubscribe [this topic]
    (let [sockopt (:unsubscribe sock-opts)]
      (ZMQ/zmq_setsockopt socket-ptr (int sockopt) ^bytes topic)))
  (receive-more? [this]
      (= 1 (ZMQ/zmq_getsockopt_int socket-ptr (int (:rcvmore sock-opts)))))
  (errno [this]
    (ZMQ/zmq_errno))
  Closeable
  (close [this]
    (when (compare-and-set! closed? false true)
      (ZMQ/zmq_close socket-ptr))))

(defn socket [context socket-type]
  (if-let [type (socket-types socket-type)]
    (->ManagedSocket (ZMQ/zmq_socket (:context-ptr context) type) (atom false))
    (throw (IllegalArgumentException. (format "Unknown socket type: %s" socket-type)))))

(defn create-poll-items [& items]
  (let [size (count items)
        ^ByteBuffer bb (ByteBuffer/allocateDirect (* size 16))]
    (doseq [{:keys [socket fd events]} items]
      (.putLong bb socket)
      (.putInt bb (or fd 0))
      (.putShort bb events)
      (.putShort bb 0))
    bb))
