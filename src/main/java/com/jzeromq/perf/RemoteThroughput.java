package com.jzeromq.perf;

import static com.jzeromq.jni.ZMQ.PUSH;
import static com.jzeromq.jni.ZMQ.zmq_close;
import static com.jzeromq.jni.ZMQ.zmq_connect;
import static com.jzeromq.jni.ZMQ.zmq_ctx_destroy;
import static com.jzeromq.jni.ZMQ.zmq_ctx_new;
import static com.jzeromq.jni.ZMQ.zmq_errno;
import static com.jzeromq.jni.ZMQ.zmq_send;
import static com.jzeromq.jni.ZMQ.zmq_socket;
import static com.jzeromq.jni.ZMQ.zmq_strerror;

import java.nio.ByteBuffer;

public class RemoteThroughput {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("usage: RemoteThroughput <connect-to> <message-size> <message-count>");
            System.exit(0);
        }
        String connectTo = args[0];
        int size = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);

        long ctx = zmq_ctx_new();
        if (ctx == -1) {
            String error = zmq_strerror(zmq_errno());
            System.out.println("error in zmq_ctx_new: " + error);
            System.exit(-1);
        }

        long s = zmq_socket(ctx, PUSH);
        if (s == -1) {
            System.out.println("error in zmq_socket: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }

        boolean result = zmq_connect(s, connectTo);
        if (!result) {
            System.out.println("error in zmq_connect: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }

        int rc;
        for (int i = 0; i != count; i++) {
            sendByteArray(s, size);
            // sendByteBuffer(s, size);
        }
        result = zmq_close(s);

        if (!result) {
            System.out.println("error in zmq_close: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }

        result = zmq_ctx_destroy(ctx);
        if (!result) {
            System.out.println("error in zmq_term: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }
    }

    public static final void sendByteBuffer(long s, int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size); // This might be the bottle neck
        int rc = zmq_send(s, bb, 0);
        if (rc < 0) {
            System.out.println("error in zmq_sendmsg: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }
    }

    public static final void sendByteArray(long s, int size) {
        byte[] bb = new byte[size];
        int rc = zmq_send(s, bb, 0, size, 0);
        if (rc < 0) {
            System.out.println("error in zmq_sendmsg: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }
    }
}
