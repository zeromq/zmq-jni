package org.zeromq.jni;

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

    public static native boolean zmq_unbind (long socket, String endpoint);

    public static native boolean zmq_connect (long socket, String endpoint);

    public static native boolean zmq_disconnect (long socket, String endpoint);
    
    public static native boolean zmq_close (long socket);

    public static native int zmq_errno ();

    public static native String zmq_strerror (int errnum);

    public static native int zmq_send (long socket, byte[] buf, int offset, int len, int flags);

    public static native int zmq_recv (long socket, byte[] buf, int offset, int len, int flags);
    
    public static void main(String[] args) {
        System.out.println("ZeroMQ Version: " + version());
        long ptr = zmq_ctx_new();
        zmq_ctx_set(ptr, 1, 1);
        int value = zmq_ctx_get(ptr, 1);
        System.out.println("IO_THREADS: " + value);
        // push socket
        long pull = zmq_socket(ptr, 7);
        System.out.println("Socket bound?: " + zmq_bind(pull, "tcp://*:12345"));
        long push = zmq_socket(ptr, 8);
        System.out.println("Socket connected?: " + zmq_connect(push, "tcp://localhost:12345"));

        System.out.println("Sending message: " + zmq_send(push, "Hello".getBytes(), 0, 5, 0));
        byte [] buf = new byte[5];
        zmq_recv(pull, buf, 0, 5, 0);
        System.out.println("Receiving message: " + new String(buf));
        
        System.out.println("PULL closed?: " + zmq_close(pull));
        System.out.println("PUSH closed?: " + zmq_close(push));
        System.out.println("Destroyed: " + zmq_ctx_destroy(ptr));
    }
}
