package org.zeromq;

public final class ZMQ {
    static {
        System.loadLibrary("zmqjni");
    }

    /**
     * Return the ZMQ API version
     */
    public static native int version();

    /**
     * Creates a new ØMQ context.
     *
     * @return an opaque handle to the newly created context if
     * successful. Otherwise it shall return -1 and set errno to one
     * of the values defined below.
     */
    public static native long zmq_ctx_new();

    public static native boolean zmq_ctx_set(long context, int name, int value);

    public static native int zmq_ctx_get(long context, int name);

    /**
     * Destroys the ØMQ context
     */
    public static native boolean zmq_ctx_destroy (long context);

    public static native long zmq_socket (long context, int type);

    public static native boolean zmq_bind (long socket, String endpoint);

    public static native boolean zmq_connect (long socket, String endpoint);

    public static native boolean zmq_close (long socket);

    public static native int zmq_errno ();

    public static native String zmq_strerror (int errnum);
    
    public static void main(String[] args) {
        System.out.println("ZeroMQ Version: " + version());
        long ptr = zmq_ctx_new();
        zmq_ctx_set(ptr, 1, 1);
        int value = zmq_ctx_get(ptr, 1);
        System.out.println("IO_THREADS: " + value);
        // push socket
        long sock = zmq_socket(ptr, 8);
        System.out.println("Socket bound: " + zmq_bind(sock, "tcp://*:12345"));
        System.out.println("Socket closed?: " + zmq_close(sock));
        // fail socket
        System.out.println("Socket fail: " + zmq_connect(sock, "tcp://*:12346"));
        int errnum = zmq_errno();
        System.out.println("Errno: " + errnum);
        String errstr = zmq_strerror(errnum);
        System.out.println("String err: " + errstr);
        System.out.println("Destroyed: " + zmq_ctx_destroy(ptr));
    }
}
