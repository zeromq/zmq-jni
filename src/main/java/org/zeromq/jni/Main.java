/*
 * Copyright © 2013 Trevor Bernard
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

import static org.zeromq.jni.ZMQ.*;

public class Main {
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
