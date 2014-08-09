/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.zeromq.jni;

import java.nio.ByteBuffer;

public final class ZMQ
{
    private static native void nativeInit();

    static
    {
        Loader.loadLibrary("zmqjni");
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

    // Constants - Message options
    public static final int MORE = 1;

    // Constants - Send/recv options
    public static final int DONTWAIT = 1;
    public static final int SNDMORE = 2;

    // Constants - Security Mechanisms
    public static final int NULL = 0;
    public static final int PLAIN = 1;
    public static final int CURVE = 2;

    // Constants - Deprecated options and aliases
    public static final int ZMQ_IPV4ONLY                = 31;
    public static final int ZMQ_DELAY_ATTACH_ON_CONNECT = IMMEDIATE;
    public static final int ZMQ_NOBLOCK                 = DONTWAIT;
    public static final int ZMQ_FAIL_UNROUTABLE         = MANDATORY;
    public static final int ZMQ_ROUTER_BEHAVIOR         = MANDATORY;

    // Constants - Context options
    public static final int IO_THREADS = 1;
    public static final int MAX_SOCKETS = 2;

    // Constants - Default for new contexts
    public static final int IO_THREADS_DFLT  = 1;
    public static final int MAX_SOCKETS_DFLT = 1023;

    // Constants - Socket Types
    public static final int PAIR = 0;
    public static final int PUB = 1;
    public static final int SUB = 2;
    public static final int REQ = 3;
    public static final int REP = 4;
    public static final int DEALER = 5;
    public static final int ROUTER = 6;
    public static final int PULL = 7;
    public static final int PUSH = 8;
    public static final int XPUB = 9;
    public static final int XSUB = 10;
    public static final int STREAM = 11;

    // Deprecated aliases
    public static final int XREQ = DEALER;
    public static final int XREP = ROUTER;

    // Constants - I/O multiplexing
    public static final int POLLIN = 1;
    public static final int POLLOUT = 2;
    public static final int POLLERR = 4;

    // Constants - Socket options
    public static final int AFFINITY = 4;
    public static final int IDENTITY = 5;
    public static final int SUBSCRIBE = 6;
    public static final int UNSUBSCRIBE = 7;
    public static final int RATE = 8;
    public static final int RECOVERY_IVL = 9;
    public static final int SNDBUF = 11;
    public static final int RCVBUF = 12;
    public static final int RCVMORE = 13;
    public static final int FD = 14;
    public static final int EVENTS = 15;
    public static final int TYPE = 16;
    public static final int LINGER = 17;
    public static final int RECONNECT_IVL = 18;
    public static final int BACKLOG = 19;
    public static final int RECONNECT_IVL_MAX = 21;
    public static final int MAXMSGSIZE = 22;
    public static final int SNDHWM = 23;
    public static final int RCVHWM = 24;
    public static final int MULTICAST_HOPS = 25;
    public static final int RCVTIMEO = 27;
    public static final int SNDTIMEO = 28;
    public static final int LAST_ENDPOINT = 32;
    public static final int ROUTER_MANDATORY = 33;
    public static final int TCP_KEEPALIVE = 34;
    public static final int TCP_KEEPALIVE_CNT = 35;
    public static final int TCP_KEEPALIVE_IDLE = 36;
    public static final int TCP_KEEPALIVE_INTVL = 37;
    public static final int TCP_ACCEPT_FILTER = 38;
    public static final int IMMEDIATE = 39;
    public static final int XPUB_VERBOSE = 40;
    public static final int ROUTER_RAW = 41;
    public static final int IPV6 = 42;
    public static final int MECHANISM = 43;
    public static final int PLAIN_SERVER = 44;
    public static final int PLAIN_USERNAME = 45;
    public static final int PLAIN_PASSWORD = 46;
    public static final int CURVE_SERVER = 47;
    public static final int CURVE_PUBLICKEY = 48;
    public static final int CURVE_SECRETKEY = 49;
    public static final int CURVE_SERVERKEY = 50;
    public static final int PROBE_ROUTER = 51;
    public static final int REQ_CORRELATE = 52;
    public static final int REQ_RELAXED = 53;
    public static final int CONFLATE = 54;
    public static final int ZAP_DOMAIN = 55;
}
