package org.zeromq.jni.perf;

import static org.zeromq.jni.ZMQ.PULL;
import static org.zeromq.jni.ZMQ.zmq_bind;
import static org.zeromq.jni.ZMQ.zmq_close;
import static org.zeromq.jni.ZMQ.zmq_ctx_destroy;
import static org.zeromq.jni.ZMQ.zmq_ctx_new;
import static org.zeromq.jni.ZMQ.zmq_errno;
import static org.zeromq.jni.ZMQ.zmq_recv;
import static org.zeromq.jni.ZMQ.zmq_socket;
import static org.zeromq.jni.ZMQ.zmq_strerror;

import java.nio.ByteBuffer;

public class LocalThroughput {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("usage: LocalThroughput <bind-to> <message-size> <message-count>");
            System.exit(0);
        }
        String bindTo = args[0];
        int size = Integer.parseInt(args[1]);
        int count = Integer.parseInt(args[2]);

        long ctx = zmq_ctx_new();
        if (ctx == -1) {
            String error = zmq_strerror(zmq_errno());
            System.out.println("error in zmq_ctx_new: " + error);
            System.exit(-1);
        }

        long s = zmq_socket(ctx, PULL);
        if (s == -1) {
            System.out.println("error in zmq_socket: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }

        boolean result = zmq_bind(s, bindTo);
        if (!result) {
            System.out.println("error in zmq_bind: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(size);
        int rc = zmq_recv(s, bb, 0);
        if (rc < 0) {
            System.out.println("error in zmq_recv: " + zmq_strerror(zmq_errno()));
            System.exit(-1);
        }
        if (bb.limit() != size) {
            System.out.println("message of incorrect size received");
            System.exit(-1);
        }
        bb.clear();
        long start = System.currentTimeMillis();
        for (int i = 0; i != count - 1; i++) {
            rc = zmq_recv(s, bb, 0);
            if (rc < 0) {
                System.out.println("error in zmq_recv: " + zmq_strerror(zmq_errno()));
                System.exit(-1);
            }
            if (bb.limit() != size) {
                System.out.println("message of incorrect size received");
                System.exit(-1);
            }
            bb.clear();
        }
        long elapsed = System.currentTimeMillis() - start;
        if (elapsed == 0) {
            elapsed = 1;
        }
        elapsed *= 1000;
        long throughput = (long) ((double) count / (double) elapsed * 1000000);
        double megabits = (double) (throughput * size * 8) / 1000000;

        System.out.format("message size: %d [B]\n", size);
        System.out.format("message count: %d\n", count);
        System.out.format("mean throughput: %d [msg/s]\n", throughput);
        System.out.format("mean throughput: %.3f [Mb/s]\n", megabits);

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
}
