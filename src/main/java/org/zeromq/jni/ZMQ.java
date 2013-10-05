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
}
