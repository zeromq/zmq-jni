package org.zeromq.jni;

import java.nio.ByteBuffer;

public final class ZMQ {
    static {
        System.loadLibrary("zmqjni");
    }

    public static native int version();

    public static native long zmq_ctx_new();

    public static native boolean zmq_ctx_set(long context, int name, int value);

    public static native int zmq_ctx_get(long context, int name);

    public static native boolean zmq_ctx_destroy (long context);

    public static native long zmq_socket (long context, int type);

    public static native boolean zmq_bind (long socket, String endpoint);

    public static native boolean zmq_unbind (long socket, String endpoint);

    public static native boolean zmq_connect (long socket, String endpoint);

    public static native boolean zmq_disconnect (long socket, String endpoint);
    
    public static native boolean zmq_close (long socket);

    public static native int zmq_errno ();

    public static native String zmq_strerror (int errnum);

    public static native int zmq_send (long socket, byte[] buf, int offset, int len, int flags);

    public static native int zmq_send (long socket, ByteBuffer buf, int flags);

    public static native byte[] zmq_recv (long socket, int flags);

    public static native int zmq_recv (long socket, byte[] buf, int offset, int len, int flags);

    public static native int zmq_recv (long socket, ByteBuffer buf, int flags);

    public static native int zmq_setsockopt (long socket, int option, int value);
    
    public static native int zmq_setsockopt (long socket, int option, long value);

    public static native int zmq_setsockopt (long socket, int option, byte[] value);
}
