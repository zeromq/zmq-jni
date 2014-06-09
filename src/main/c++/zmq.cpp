/*
 * Copyright 2013-2014 Trevor Bernard
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

#include <inttypes.h>
#include "zmq.h"
#include "com_jzeromq_jni_ZMQ.h"

static jmethodID limitMID;
static jmethodID positionMID;
static jmethodID setPositionMID;

JNIEXPORT void JNICALL
Java_com_jzeromq_jni_ZMQ_nativeInit (JNIEnv *env, jclass c)
{
    jclass cls = env->FindClass("java/nio/ByteBuffer");

    limitMID = env->GetMethodID(cls, "limit", "()I");
    positionMID = env->GetMethodID(cls, "position", "()I");
    setPositionMID = env->GetMethodID(cls, "position", "(I)Ljava/nio/Buffer;");

    env->DeleteLocalRef(cls);
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_version (JNIEnv *env, jclass c)
{
    return ZMQ_VERSION;
}

JNIEXPORT jlong JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1ctx_1new  (JNIEnv *env, jclass c)
{
    void *context = zmq_ctx_new();
    if(context)
        return (jlong) context;
    return -1;
}

JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1ctx_1set (JNIEnv *env, jclass c, jlong context, jint name, jint value)
{
    int rc = zmq_ctx_set((void *) context, name, value);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1ctx_1get (JNIEnv *env, jclass c, jlong context, jint value)
{
    return zmq_ctx_get((void *) context, value);
}

JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1ctx_1destroy (JNIEnv *env, jclass c, jlong context)
{
    int rc = zmq_ctx_destroy((void *) context);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT jlong JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1socket (JNIEnv *env, jclass c, jlong context, jint type)
{
    void *socket = zmq_socket((void *) context, type);
    if(socket)
        return (jlong) socket;
    return -1;
}

JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1bind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = (const char *) env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_bind ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1unbind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = (const char *) env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_unbind ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}


JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1connect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_connect ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1disconnect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = (const char *) env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_disconnect ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT jboolean JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1close (JNIEnv *env, jclass c, jlong socket)
{
    int rc = zmq_close((void *) socket);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1errno (JNIEnv *env, jclass c)
{
    return zmq_errno();
}

JNIEXPORT jstring JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1strerror (JNIEnv *env, jclass c, jint errnum)
{
    const char *str = zmq_strerror (errnum);
    return env->NewStringUTF(str);
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1send__J_3BIII (JNIEnv *env, jclass c, jlong socket, jbyteArray buf, jint offset, jint len, jint flags)
{
    jbyte *data = env->GetByteArrayElements (buf, 0);
    int rc = zmq_send((void *) socket, data + offset, len, flags);
    env->ReleaseByteArrayElements (buf, data, 0);
    return rc;
}

JNIEXPORT jbyteArray JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1recv__JI (JNIEnv *env, jclass c, jlong socket, jint flags)
{
    zmq_msg_t msg;
    zmq_msg_init (&msg);
    zmq_recvmsg ((void *) socket, &msg, flags);
    int size = zmq_msg_size (&msg);
    jbyteArray buf = env->NewByteArray (size);
    env->SetByteArrayRegion (buf, 0, size, (jbyte*) zmq_msg_data (&msg));
    zmq_msg_close(&msg);
    return buf;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1recv__J_3BIII (JNIEnv *env, jclass c, jlong socket, jbyteArray buf, jint offset, jint len, jint flags)
{
    jbyte *data = env->GetByteArrayElements (buf, 0);
    int rc = zmq_recv((void *) socket, data + offset, len, flags);
    env->ReleaseByteArrayElements (buf, data, 0);
    return rc;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1send__JLjava_nio_ByteBuffer_2I (JNIEnv *env, jclass c, jlong socket, jobject buf, jint flags)
{
    jbyte* data = (jbyte*) env->GetDirectBufferAddress(buf);
    if(data == NULL)
        return -1;

    int lim = env->CallIntMethod(buf, limitMID);
    int pos = env->CallIntMethod(buf, positionMID);
    int rem = pos <= lim ? lim - pos : 0;

    int written = zmq_send((void *) socket, data + pos, rem, flags);

    if (written > 0)
        env->CallVoidMethod(buf, setPositionMID, pos + written);

    return written;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1recv__JLjava_nio_ByteBuffer_2I (JNIEnv *env, jclass c, jlong socket, jobject buf, jint flags)
{
    jbyte* data = (jbyte*) env->GetDirectBufferAddress(buf);
    if(data == NULL)
        return -1;

    int lim = env->CallIntMethod(buf, limitMID);
    int pos = env->CallIntMethod(buf, positionMID);
    int rem = pos <= lim ? lim - pos : 0;

    int read = zmq_recv((void *) socket, data + pos, rem, flags);
    if (read > 0) {
        read = read > rem ? rem : read;
        env->CallVoidMethod(buf, setPositionMID, pos + read);
        return read;
    }
    return read;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1setsockopt__JII (JNIEnv *env, jclass c, jlong socket, jint option, jint value)
{
    int val = (int) value;
    int rc = zmq_setsockopt ((void *) socket, option, &val, sizeof(val));
    return rc;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1setsockopt__JIJ (JNIEnv *env, jclass c, jlong socket, jint option, jlong value)
{
    uint64_t val = (uint64_t) value;
    int rc = zmq_setsockopt ((void *) socket, option, &val, sizeof(val));
    return rc;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1setsockopt__JI_3B (JNIEnv *env, jclass c, jlong socket, jint option, jbyteArray value)
{
    jbyte *data = env->GetByteArrayElements (value, 0);
    size_t size = env->GetArrayLength (value);
    int rc = zmq_setsockopt ((void *) socket, option, data, size);
    env->ReleaseByteArrayElements (value, data, 0);
    return rc;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1getsockopt_1int (JNIEnv *env, jclass c, jlong socket, jint option)
{
    int val;
    size_t size = sizeof (val);
    zmq_getsockopt ((void *) socket, option, &val, &size);
    return val;
}

JNIEXPORT jlong JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1getsockopt_1long (JNIEnv *env, jclass c, jlong socket, jint option)
{
    uint64_t val;
    size_t size = sizeof (val);
    zmq_getsockopt ((void *) socket, option, &val, &size);
    return val;
}

JNIEXPORT jbyteArray JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1getsockopt_1bytes (JNIEnv *env, jclass c, jlong socket, jint option)
{
    char val[255];
    size_t size = 255;
    zmq_getsockopt ((void *) socket, option, val, &size);
    jbyteArray buf = env->NewByteArray (size);
    env->SetByteArrayRegion (buf, 0, size, (jbyte*) val);
    return buf;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmq_1poll (JNIEnv *env, jclass c, jlong items, jint count, jlong timeout)
{
    return zmq_poll ((zmq_pollitem_t *) items, count, timeout);
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmqiothreads (JNIEnv *env, jclass c)
{
    return ZMQ_IO_THREADS;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_zmqmaxsockets (JNIEnv *env, jclass c)
{
    return ZMQ_MAX_SOCKETS;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_pair (JNIEnv *env, jclass c)
{
    return ZMQ_PAIR;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_pub (JNIEnv *env, jclass c)
{
    return ZMQ_PUB;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_sub (JNIEnv *env, jclass c)
{
    return ZMQ_SUB;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_req (JNIEnv *env, jclass c)
{
    return ZMQ_REQ;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_rep (JNIEnv *env, jclass c)
{
    return ZMQ_REP;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_dealer (JNIEnv *env, jclass c)
{
    return ZMQ_DEALER;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_router (JNIEnv *env, jclass c)
{
    return ZMQ_ROUTER;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_pull (JNIEnv *env, jclass c)
{
    return ZMQ_PULL;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_push (JNIEnv *env, jclass c)
{
    return ZMQ_PUSH;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_xpub (JNIEnv *env, jclass c)
{
    return ZMQ_XPUB;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_xsub (JNIEnv *env, jclass c)
{
    return ZMQ_XSUB;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_sndmore (JNIEnv *env, jclass c)
{
    return ZMQ_SNDMORE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_dontwait (JNIEnv *env, jclass c)
{
    return ZMQ_DONTWAIT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enotsup (JNIEnv *env, jclass c)
{
    return ENOTSUP;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_eprotonosupport (JNIEnv *env, jclass c)
{
    return EPROTONOSUPPORT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enobufs (JNIEnv *env, jclass c)
{
    return ENOBUFS;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enetdown (JNIEnv *env, jclass c)
{
    return ENETDOWN;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_eaddrinuse (JNIEnv *env, jclass c)
{
    return EADDRINUSE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_eaddrnotavail (JNIEnv *env, jclass c)
{
    return EADDRNOTAVAIL;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_econnrefused (JNIEnv *env, jclass c)
{
    return ECONNREFUSED;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_einprogress (JNIEnv *env, jclass c)
{
    return EINPROGRESS;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enotsock (JNIEnv *env, jclass c)
{
    return ENOTSOCK;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_emsgsize (JNIEnv *env, jclass c)
{
    return EMSGSIZE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_eafnosupport (JNIEnv *env, jclass c)
{
    return EAFNOSUPPORT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enetunreach (JNIEnv *env, jclass c)
{
    return ENETUNREACH;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_econnaborted (JNIEnv *env, jclass c)
{
    return ECONNABORTED;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_econnreset (JNIEnv *env, jclass c)
{
    return ECONNRESET;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enotconn (JNIEnv *env, jclass c)
{
    return ENOTCONN;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_etimedout (JNIEnv *env, jclass c)
{
    return ETIMEDOUT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_ehostunreach (JNIEnv *env, jclass c)
{
    return EHOSTUNREACH;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enetreset (JNIEnv *env, jclass c)
{
    return ENETRESET;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_efsm (JNIEnv *env, jclass c)
{
    return EFSM;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_enocompatproto (JNIEnv *env, jclass c)
{
    return ENOCOMPATPROTO;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_eterm (JNIEnv *env, jclass c)
{
    return ETERM;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_emthread (JNIEnv *env, jclass c)
{
    return EMTHREAD;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_affinity (JNIEnv *env, jclass c)
{
    return ZMQ_AFFINITY;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_identity (JNIEnv *env, jclass c)
{
    return ZMQ_IDENTITY;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_subscribe (JNIEnv *env, jclass c)
{
    return ZMQ_SUBSCRIBE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_unsubscribe (JNIEnv *env, jclass c)
{
    return ZMQ_UNSUBSCRIBE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_rate (JNIEnv *env, jclass c)
{
    return ZMQ_RATE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_recoveryivl (JNIEnv *env, jclass c)
{
    return ZMQ_RECOVERY_IVL;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_sndbuf (JNIEnv *env, jclass c)
{
    return ZMQ_SNDBUF;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_rcvbuf (JNIEnv *env, jclass c)
{
    return ZMQ_RCVBUF;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_rcvmore (JNIEnv *env, jclass c)
{
    return ZMQ_RCVMORE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_fd (JNIEnv *env, jclass c)
{
    return ZMQ_FD;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_events (JNIEnv *env, jclass c)
{
    return ZMQ_EVENTS;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_type (JNIEnv *env, jclass c)
{
    return ZMQ_TYPE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_linger (JNIEnv *env, jclass c)
{
    return ZMQ_LINGER;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_reconnectivl (JNIEnv *env, jclass c)
{
    return ZMQ_RECONNECT_IVL;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_backlog (JNIEnv *env, jclass c)
{
    return ZMQ_BACKLOG;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_reconnectivlmax (JNIEnv *env, jclass c)
{
    return ZMQ_RECONNECT_IVL_MAX;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_maxmsgsize (JNIEnv *env, jclass c)
{
    return ZMQ_MAXMSGSIZE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_sndhwm (JNIEnv *env, jclass c)
{
    return ZMQ_SNDHWM;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_rcvhwm (JNIEnv *env, jclass c)
{
    return ZMQ_RCVHWM;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_multicasthops (JNIEnv *env, jclass c)
{
    return ZMQ_MULTICAST_HOPS;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_rcvtimeo (JNIEnv *env, jclass c)
{
    return ZMQ_RCVTIMEO;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_sndtimeo (JNIEnv *env, jclass c)
{
    return ZMQ_SNDTIMEO;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_ipv4only (JNIEnv *env, jclass c)
{
    return ZMQ_IPV4ONLY;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_lastendpoint (JNIEnv *env, jclass c)
{
    return ZMQ_LAST_ENDPOINT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_routermandatory (JNIEnv *env, jclass c)
{
    return ZMQ_ROUTER_MANDATORY;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_tcpkeepalive (JNIEnv *env, jclass c)
{
    return ZMQ_TCP_KEEPALIVE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_tcpkeepalivecnt (JNIEnv *env, jclass c)
{
    return ZMQ_TCP_KEEPALIVE_CNT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_tcpkeepaliveidle (JNIEnv *env, jclass c)
{
    return ZMQ_TCP_KEEPALIVE_IDLE;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_tcpkeepaliveintvl (JNIEnv *env, jclass c)
{
    return ZMQ_TCP_KEEPALIVE_INTVL;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_tcpacceptfilter (JNIEnv *env, jclass c)
{
    return ZMQ_TCP_ACCEPT_FILTER;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_delayattachonconnect (JNIEnv *env, jclass c)
{
    return ZMQ_DELAY_ATTACH_ON_CONNECT;
}

JNIEXPORT jint JNICALL
Java_com_jzeromq_jni_ZMQ_xpubverbose (JNIEnv *env, jclass c)
{
    return ZMQ_XPUB_VERBOSE;
}
