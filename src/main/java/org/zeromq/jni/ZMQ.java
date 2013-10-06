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

}
