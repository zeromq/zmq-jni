#include <inttypes.h>
#include "zmq.h"
#include "org_zeromq_jni_ZMQ.h"

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_version (JNIEnv *env, jclass c)
{
    return ZMQ_VERSION;
}

JNIEXPORT
jlong JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1new  (JNIEnv *env, jclass c)
{
    void *context = zmq_ctx_new();
    if(context)
        return (long) context;
    return -1;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1set (JNIEnv *env, jclass c, jlong context, jint name, jint value)
{
    int rc = zmq_ctx_set((void *) context, name, value);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1get (JNIEnv *env, jclass c, jlong context, jint value)
{
    return zmq_ctx_get((void *) context, value);
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1destroy (JNIEnv *env, jclass c, jlong context)
{
    int rc = zmq_ctx_destroy((void *) context);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jlong JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1socket (JNIEnv *env, jclass c, jlong context, jint type)
{
    void *socket = zmq_socket((void *) context, type);
    if(socket)
        return (long) socket;
    return -1;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1bind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_bind ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1unbind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_unbind ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}


JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1connect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_connect ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1disconnect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_disconnect ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1close (JNIEnv *env, jclass c, jlong socket)
{
    int rc = zmq_close((void *) socket);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1errno (JNIEnv *env, jclass c)
{
    return zmq_errno();
}

JNIEXPORT
jstring JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1strerror (JNIEnv *env, jclass c, jint errnum)
{
    const char *str = zmq_strerror (errnum);
    return env->NewStringUTF(str);
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1send__J_3BIII (JNIEnv *env, jclass c, jlong socket, jbyteArray buf, jint offset, jint len, jint flags)
{
    jbyte *data = env->GetByteArrayElements (buf, 0);
    int rc = zmq_send((void *) socket, data + offset, len, flags);
    env->ReleaseByteArrayElements (buf, data, 0);
    return rc;
}

JNIEXPORT
jbyteArray JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1recv__JI (JNIEnv *env, jclass c, jlong socket, jint flags)
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

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1recv__J_3BIII (JNIEnv *env, jclass c, jlong socket, jbyteArray buf, jint offset, jint len, jint flags)
{
    jbyte *data = env->GetByteArrayElements (buf, 0);
    int rc = zmq_recv((void *) socket, data + offset, len, flags);
    env->ReleaseByteArrayElements (buf, data, 0);
    return rc;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1send__JLjava_nio_ByteBuffer_2I (JNIEnv *env, jclass c, jlong socket, jobject buf, jint flags)
{
    jbyte* data = (jbyte*) env->GetDirectBufferAddress(buf);
    if(data == NULL)
        return -1;
    // Cache me
    jclass cls = env->GetObjectClass(buf);

    jmethodID limitHandle = env->GetMethodID(cls, "limit", "()I");
    jmethodID positionHandle = env->GetMethodID(cls, "position", "()I");
    jmethodID setPositionHandle = env->GetMethodID(cls, "position", "(I)Ljava/nio/Buffer;");

    env->DeleteLocalRef(cls);

    int lim = env->CallIntMethod(buf, limitHandle);
    int pos = env->CallIntMethod(buf, positionHandle);
    int rem = pos <= lim ? lim - pos : 0;

    int written = zmq_send((void *) socket, data + pos, rem, flags);

    if (written > 0)
        env->CallVoidMethod(buf, setPositionHandle, pos + written);
    return written;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1recv__JLjava_nio_ByteBuffer_2I (JNIEnv *env, jclass c, jlong socket, jobject buf, jint flags)
{
    jbyte* data = (jbyte*) env->GetDirectBufferAddress(buf);
    if(data == NULL)
        return -1;
    // Cache me
    jclass cls = env->GetObjectClass(buf);

    jmethodID limitHandle = env->GetMethodID(cls, "limit", "()I");
    jmethodID positionHandle = env->GetMethodID(cls, "position", "()I");
    jmethodID setPositionHandle = env->GetMethodID(cls, "position", "(I)Ljava/nio/Buffer;");

    env->DeleteLocalRef(cls);

    int lim = env->CallIntMethod(buf, limitHandle);
    int pos = env->CallIntMethod(buf, positionHandle);
    int rem = pos <= lim ? lim - pos : 0;

    int received = zmq_recv((void *) socket, data + pos, rem, flags);
    if (received > 0)
        env->CallVoidMethod(buf, setPositionHandle, pos + received);
    return received;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1setsockopt__JII (JNIEnv *env, jclass c, jlong socket, jint option, jint value)
{
    int val = (int) value;
    int rc = zmq_setsockopt ((void *) socket, option, &val, sizeof(val));
    return rc;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1setsockopt__JIJ (JNIEnv *env, jclass c, jlong socket, jint option, jlong value)
{
    uint64_t val = (uint64_t) value;
    int rc = zmq_setsockopt ((void *) socket, option, &val, sizeof(val));
    return rc;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1setsockopt__JI_3B (JNIEnv *env, jclass c, jlong socket, jint option, jbyteArray value)
{
    jbyte *data = env->GetByteArrayElements (value, 0);
    size_t size = env->GetArrayLength (value);
    int rc = zmq_setsockopt ((void *) socket, option, data, size);
    env->ReleaseByteArrayElements (value, data, 0);
    return rc;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1poll (JNIEnv *env, jclass c, jlong items, jint count, jlong timeout)
{
    return zmq_poll ((zmq_pollitem_t *) items, count, timeout);
}
