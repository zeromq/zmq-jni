/*
 * Copyright 2013 Trevor Bernard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zeromq.jni;

import java.nio.ByteBuffer;

public final class ZMQ {
    static {
        Loader.loadLibrary("zmqjni");
    }

    private static native void nativeInit();

    static {
        nativeInit();
    }

    public static native int version();

    public static native long zmq_ctx_new();

    public static native boolean zmq_ctx_set(long context, int name, int value);

    public static native int zmq_ctx_get(long context, int name);

    public static native boolean zmq_ctx_destroy(long context);

    public static native long zmq_socket(long context, int type);

    public static native boolean zmq_bind(long socket, String endpoint);

    public static native boolean zmq_unbind(long socket, String endpoint);

    public static native boolean zmq_connect(long socket, String endpoint);

    public static native boolean zmq_disconnect(long socket, String endpoint);

    public static native boolean zmq_close(long socket);

    public static native int zmq_errno();

    public static native String zmq_strerror(int errnum);

    public static native int zmq_send(long socket, byte[] buf, int offset, int len, int flags);

    public static native int zmq_send(long socket, ByteBuffer buf, int flags);

    public static native byte[] zmq_recv(long socket, int flags);

    public static native int zmq_recv(long socket, byte[] buf, int offset, int len, int flags);

    public static native int zmq_recv(long socket, ByteBuffer buf, int flags);

    public static native int zmq_setsockopt(long socket, int option, int value);

    public static native int zmq_setsockopt(long socket, int option, long value);

    public static native int zmq_setsockopt(long socket, int option, byte[] value);

    public static native int zmq_getsockopt_int(long socket, int option);

    public static native long zmq_getsockopt_long(long socket, int option);

    public static native byte[] zmq_getsockopt_bytes(long socket, int option);

    public static native int zmq_poll(long items, int count, long timeout);

    // Constants - Socket Types

    private static native int pair();

    public static final int PAIR = pair();

    private static native int pub();

    public static final int PUB = pub();

    private static native int sub();

    public static final int SUB = sub();

    private static native int req();

    public static final int REQ = req();

    private static native int rep();

    public static final int REP = rep();

    private static native int dealer();

    public static final int DEALER = dealer();

    private static native int router();

    public static final int ROUTER = router();

    private static native int pull();

    public static final int PULL = pull();

    private static native int push();

    public static final int PUSH = push();

    private static native int xpub();

    public static final int XPUB = xpub();

    private static native int xsub();

    public static final int XSUB = xsub();

    // Deprecated aliases

    public static final int XREQ = DEALER;

    public static final int XREP = ROUTER;

    // Constants - Errors

    private static native int enotsup();

    public static final int ENOTSUP = enotsup();

    private static native int eprotonosupport();

    public static final int EPROTONOSUPPORT = eprotonosupport();

    private static native int enobufs();

    public static final int ENOBUFS = enobufs();

    private static native int enetdown();

    public static final int ENETDOWN = enetdown();

    private static native int eaddrinuse();

    public static final int EADDRINUSE = eaddrinuse();

    private static native int eaddrnotavail();

    public static final int EADDRNOTAVAIL = eaddrnotavail();

    private static native int econnrefused();

    public static final int ECONNREFUSED = econnrefused();

    private static native int einprogress();

    public static final int EINPROGRESS = einprogress();

    private static native int enotsock();

    public static final int ENOTSOCK = enotsock();

    private static native int emsgsize();

    public static final int EMSGSIZE = emsgsize();

    private static native int eafnosupport();

    public static final int EAFNOSUPPORT = eafnosupport();

    private static native int enetunreach();

    public static final int ENETUNREACH = enetunreach();

    private static native int econnaborted();

    public static final int ECONNABORTED = econnaborted();

    private static native int econnreset();

    public static final int ECONNRESET = econnreset();

    private static native int enotconn();

    public static final int ENOTCONN = enotconn();

    private static native int etimedout();

    public static final int ETIMEDOUT = etimedout();

    private static native int ehostunreach();

    public static final int EHOSTUNREACH = ehostunreach();

    private static native int enetreset();

    public static final int ENETRESET = enetreset();

    private static native int efsm();

    public static final int EFSM = efsm();

    private static native int enocompatproto();

    public static final int ENOCOMPATPROTO = enocompatproto();

    private static native int eterm();

    public static final int ETERM = eterm();

    private static native int emthread();

    public static final int EMTHREAD = emthread();

    // Constants - Socket options

    private static native int affinity();

    public static final int AFFINITY = affinity();

    private static native int identity();

    public static final int IDENTITY = identity();

    private static native int subscribe();

    public static final int SUBSCRIBE = subscribe();

    private static native int unsubscribe();

    public static final int UNSUBSCRIBTE = unsubscribe();

    private static native int rate();

    public static final int RATE = rate();

    private static native int recoveryivl();

    public static final int RECOVERY_IVL = reconnectivl();

    private static native int sndbuf();

    public static final int SNDBUF = sndbuf();

    private static native int rcvbuf();

    public static final int RCVBUF = rcvbuf();

    private static native int rcvmore();

    public static final int RCVMORE = rcvmore();

    private static native int fd();

    public static final int FD = fd();

    private static native int events();

    public static final int EVENTS = events();

    private static native int type();

    public static final int TYPE = type();

    private static native int linger();

    public static final int LINGER = linger();

    private static native int reconnectivl();

    public static final int RECONNECT_IVL = reconnectivl();

    private static native int backlog();

    public static final int BACKLOG = backlog();

    private static native int reconnectivlmax();

    public static final int RECONNECT_IVL_MAX = reconnectivlmax();

    private static native int maxmsgsize();

    public static final int MAXMSGSIZE = maxmsgsize();

    private static native int sndhwm();

    public static final int SNDHWM = sndhwm();

    private static native int rcvhwm();

    public static final int RCVHWM = rcvhwm();

    private static native int multicasthops();

    public static final int MULTICAST_HOPS = multicasthops();

    private static native int rcvtimeo();

    public static final int RCVTIMEO = rcvtimeo();

    private static native int sndtimeo();

    public static final int SNDTIMEO = sndtimeo();

    private static native int ipv4only();

    public static final int IPV4ONLY = ipv4only();

    private static native int lastendpoint();

    public static final int LAST_ENDPOINT = lastendpoint();

    private static native int routermandatory();

    public static final int ROUTER_MANDATORY = routermandatory();

    private static native int tcpkeepalive();

    public static final int TCP_KEEPALIVE = tcpkeepalive();

    private static native int tcpkeepalivecnt();

    public static final int TCP_KEEPALIVE_CNT = tcpkeepalivecnt();

    private static native int tcpkeepaliveidle();

    public static final int TCP_KEEPALIVE_IDLE = tcpkeepaliveidle();

    private static native int tcpkeepaliveintvl();

    public static final int TCP_KEEPALIVE_INTVL = tcpkeepaliveintvl();

    private static native int tcpacceptfilter();

    public static final int TCP_ACCEPT_FILTER = tcpacceptfilter();

    private static native int delayattachonconnect();

    public static final int DELAY_ATTACH_ON_CONNECT = delayattachonconnect();

    private static native int xpubverbose();

    public static final int XPUB_VERBOSE = xpubverbose();
}
