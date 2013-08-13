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
    
    public static void main(String[] args) {
        System.out.println("ZeroMQ Version: " + version());
        long ptr = zmq_ctx_new();
        zmq_ctx_set(ptr, 1, 2);
        int value = zmq_ctx_get(ptr, 1);
        System.out.println("IO_THREADS: " + value);
    }
}
