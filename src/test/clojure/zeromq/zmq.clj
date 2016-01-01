;;
;; Copyright 2013-2014 Trevor Bernard
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
  {:io-threads ZMQ/IO_THREADS
   :max-sockets ZMQ/MAX_SOCKETS})

(def ^:const send-more ZMQ/SNDMORE)

(def ^:const dont-wait ZMQ/DONTWAIT)

(defprotocol Context
  (io-threads
    [_]
    [_ value])
  (max-sockets
    [_]
    [_ value]))

(defrecord ManagedContext [^long address]
  Context
  (io-threads [this]
    (let [opt (:io-threads context-opts)]
      (ZMQ/zmq_ctx_get address opt)))
  (io-threads [this value]
    (let [opt (:io-threads context-opts)]
      (ZMQ/zmq_ctx_set address opt (int value))))
  (max-sockets [this]
    (let [opt (:max-sockets context-opts)]
      (ZMQ/zmq_ctx_get address opt)))
  (max-sockets [this value]
    (let [opt (:max-sockets context-opts)]
      (ZMQ/zmq_ctx_set address opt (int value))))
  Closeable
  (close [this]
    (ZMQ/zmq_ctx_destroy address)))

(defn context
  ([]
     (context 1))
  ([value]
     (doto (->ManagedContext (ZMQ/zmq_ctx_new))
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
  (send
    [this buffer]
    [this buffer flags]
    [this buffer off len flags])
  (send-bb
    [this bb]
    [this bb flags])
  (receive
    [this]
    [this flags]
    [this buffer flags]
    [this buffer off len flags])
  (receive-bb
    [this bb]
    [this bb flags])
  (connect [this endpoint])
  (bind [this endpoint])
  (subscribe [this topic])
  (unsubscribe [this topic])
  (receive-more? [this])
  (errno [this]))

(defrecord ManagedSocket [^long address]
  Socket
  (send [this buffer]
    (send this buffer 0 (count buffer) 0))
  (send [this buffer flags]
    (send this buffer 0 (count buffer) flags))
  (send [this buffer off len flags]
    (ZMQ/zmq_send address buffer (int off) (int len) (int flags)))
  (send-bb [this bb]
    (ZMQ/zmq_send address ^ByteBuffer bb (int 0)))
  (send-bb [this bb flags]
    (ZMQ/zmq_send address ^ByteBuffer bb flags))
  (receive [this]
    (receive this 0))
  (receive [this flags]
    (ZMQ/zmq_recv address (int flags)))
  (receive [this buffer flags]
    (receive this buffer 0 (count buffer) flags))
  (receive [this buffer off len flags]
    (ZMQ/zmq_recv address buffer (int off) (int len) (int flags)))
  (receive-bb [this bb]
    (ZMQ/zmq_recv address ^ByteBuffer bb (int 0)))
  (receive-bb [this bb flags]
    (ZMQ/zmq_recv address ^ByteBuffer bb flags))
  (connect [this endpoint]
    (ZMQ/zmq_connect address ^String endpoint))
  (bind [this endpoint]
    (ZMQ/zmq_bind address ^String endpoint))
  (subscribe [this topic]
    (let [sockopt (:subscribe sock-opts)]
      (ZMQ/zmq_setsockopt address (int sockopt) ^bytes topic)))
  (unsubscribe [this topic]
    (let [sockopt (:unsubscribe sock-opts)]
      (ZMQ/zmq_setsockopt address (int sockopt) ^bytes topic)))
  (receive-more? [this]
    (= 1 (ZMQ/zmq_getsockopt_int address (int (:rcvmore sock-opts)))))
  (errno [this]
    (ZMQ/zmq_errno))
  Closeable
  (close [this]
    (ZMQ/zmq_close address)))

(defn curve-keypair [public secret]
  (ZMQ/zmq_curve_keypair public secret))

(defn z85-encode [dest buf]
  (ZMQ/zmq_z85_encode dest buf))

(defn z85-dencode [dest buf]
  (ZMQ/zmq_z85_decode dest buf))

(defn socket [context socket-type]
  (if-let [type (socket-types socket-type)]
    (->ManagedSocket (ZMQ/zmq_socket (:address context) type))
    (throw (IllegalArgumentException. (format "Unknown socket type: %s" socket-type)))))
