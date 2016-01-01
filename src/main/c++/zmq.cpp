/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

#include <sodium.h>
#include <inttypes.h>
#include "zmq.h"
#include "org_zeromq_jni_ZMQ.h"

static jmethodID limitMID;
static jmethodID positionMID;
static jmethodID setPositionMID;

static jmethodID charBufferPutMID;
static jmethodID charBufferFlipMID;

JNIEXPORT void JNICALL
Java_org_zeromq_jni_ZMQ_nativeInit (JNIEnv *env, jclass c)
{
    jclass cls = env->FindClass("java/nio/ByteBuffer");
    limitMID = env->GetMethodID(cls, "limit", "()I");
    positionMID = env->GetMethodID(cls, "position", "()I");
    setPositionMID = env->GetMethodID(cls, "position", "(I)Ljava/nio/Buffer;");
    env->DeleteLocalRef(cls);

    cls = env->FindClass("java/nio/CharBuffer");
    charBufferPutMID = env->GetMethodID(cls, "put", "(Ljava/lang/String;)Ljava/nio/CharBuffer;");
    charBufferFlipMID = env->GetMethodID(cls, "flip", "()Ljava/nio/Buffer;");
    env->DeleteLocalRef(cls);
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_version (JNIEnv *env, jclass c)
{
    return ZMQ_VERSION;
}

JNIEXPORT jlong JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1new  (JNIEnv *env, jclass c)
{
    void *context = zmq_ctx_new();
    if(context)
        return (jlong) context;
    return -1;
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1set (JNIEnv *env, jclass c, jlong context, jint name, jint value)
{
    int rc = zmq_ctx_set((void *) context, name, value);
    return rc == 0;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1get (JNIEnv *env, jclass c, jlong context, jint value)
{
    return zmq_ctx_get((void *) context, value);
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1ctx_1destroy (JNIEnv *env, jclass c, jlong context)
{
    int rc = zmq_ctx_destroy((void *) context);
    return rc == 0;
}

JNIEXPORT jlong JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1socket (JNIEnv *env, jclass c, jlong context, jint type)
{
    void *socket = zmq_socket((void *) context, type);
    if(socket)
        return (jlong) socket;
    return -1;
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1bind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = (const char *) env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_bind ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    return rc == 0;
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1unbind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = (const char *) env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_unbind ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    return rc == 0;
}


JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1connect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_connect ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    return rc == 0;
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1disconnect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
{
    const char *ep = (const char *) env->GetStringUTFChars (endpoint, NULL);
    int rc = zmq_disconnect ((void *) socket, ep);
    env->ReleaseStringUTFChars(endpoint, ep);
    return rc == 0;
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1close (JNIEnv *env, jclass c, jlong socket)
{
    int rc = zmq_close((void *) socket);
    return rc == 0;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1errno (JNIEnv *env, jclass c)
{
    return zmq_errno();
}

JNIEXPORT jstring JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1strerror (JNIEnv *env, jclass c, jint errnum)
{
    const char *str = zmq_strerror (errnum);
    return env->NewStringUTF(str);
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1send__J_3BIII (JNIEnv *env, jclass c, jlong socket, jbyteArray buf, jint offset, jint len, jint flags)
{
    jbyte *data = env->GetByteArrayElements (buf, 0);
    int rc = zmq_send((void *) socket, data + offset, len, flags);
    env->ReleaseByteArrayElements (buf, data, 0);
    return rc;
}

JNIEXPORT jbyteArray JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1recv__JI (JNIEnv *env, jclass c, jlong socket, jint flags)
{
    zmq_msg_t msg;
    zmq_msg_init (&msg);
#if ZMQ_VERSION >= ZMQ_MAKE_VERSION(3,0,0)
    zmq_recvmsg ((void *) socket, &msg, flags);
#else
    zmq_recv ((void *) socket, &msg, flags);
#endif
    int size = zmq_msg_size (&msg);
    jbyteArray buf = env->NewByteArray (size);
    env->SetByteArrayRegion (buf, 0, size, (jbyte*) zmq_msg_data (&msg));
    zmq_msg_close(&msg);
    return buf;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1recv__J_3BIII (JNIEnv *env, jclass c, jlong socket, jbyteArray buf, jint offset, jint len, jint flags)
{
    jbyte *data = env->GetByteArrayElements (buf, 0);
    int rc = zmq_recv((void *) socket, data + offset, len, flags);
    env->ReleaseByteArrayElements (buf, data, 0);
    return rc;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1send__JLjava_nio_ByteBuffer_2I (JNIEnv *env, jclass c, jlong socket, jobject buf, jint flags)
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
Java_org_zeromq_jni_ZMQ_zmq_1recv__JLjava_nio_ByteBuffer_2I (JNIEnv *env, jclass c, jlong socket, jobject buf, jint flags)
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
Java_org_zeromq_jni_ZMQ_zmq_1setsockopt__JII (JNIEnv *env, jclass c, jlong socket, jint option, jint value)
{
    int val = (int) value;
    int rc = zmq_setsockopt ((void *) socket, option, &val, sizeof(val));
    return rc;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1setsockopt__JIJ (JNIEnv *env, jclass c, jlong socket, jint option, jlong value)
{
    uint64_t val = (uint64_t) value;
    int rc = zmq_setsockopt ((void *) socket, option, &val, sizeof(val));
    return rc;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1setsockopt__JI_3B (JNIEnv *env, jclass c, jlong socket, jint option, jbyteArray value)
{
    jbyte *data = env->GetByteArrayElements (value, 0);
    size_t size = env->GetArrayLength (value);
    int rc = zmq_setsockopt ((void *) socket, option, data, size);
    env->ReleaseByteArrayElements (value, data, 0);
    return rc;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1getsockopt_1int (JNIEnv *env, jclass c, jlong socket, jint option)
{
    int val;
    size_t size = sizeof (val);
    zmq_getsockopt ((void *) socket, option, &val, &size);
    return val;
}

JNIEXPORT jlong JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1getsockopt_1long (JNIEnv *env, jclass c, jlong socket, jint option)
{
    uint64_t val;
    size_t size = sizeof (val);
    zmq_getsockopt ((void *) socket, option, &val, &size);
    return val;
}

JNIEXPORT jbyteArray JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1getsockopt_1bytes (JNIEnv *env, jclass c, jlong socket, jint option)
{
    char val[255];
    size_t size = 255;
    zmq_getsockopt ((void *) socket, option, val, &size);
    jbyteArray buf = env->NewByteArray (size);
    env->SetByteArrayRegion (buf, 0, size, (jbyte*) val);
    return buf;
}

JNIEXPORT jint JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1poll (JNIEnv *env, jclass c, jlong items, jint count, jlong timeout)
{
    return zmq_poll ((zmq_pollitem_t *) items, count, timeout);
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1z85_1encode (JNIEnv *env, jclass c, jobject dest, jbyteArray data)
{
#if ZMQ_VERSION >= ZMQ_MAKE_VERSION(4,0,0)
    jbyte *public_key = env->GetByteArrayElements (data, 0);
    size_t size = env->GetArrayLength (data);
    char encoded [41];
    zmq_z85_encode (encoded, (uint8_t *) public_key, size);
    env->ReleaseByteArrayElements (data, public_key, 0);
    jstring result = env->NewStringUTF(encoded);
    env->CallObjectMethod(dest, charBufferPutMID, result);
    env->CallObjectMethod(dest, charBufferFlipMID);
    return true;
#else
    return false;
#endif
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1z85_1decode (JNIEnv *env, jclass c, jbyteArray dest, jstring data)
{
#if ZMQ_VERSION >= ZMQ_MAKE_VERSION(4,0,0)
    jbyte *buf = env->GetByteArrayElements (dest, 0);
    const char *encoded = env->GetStringUTFChars (data, NULL);
    zmq_z85_decode((uint8_t *) buf, const_cast<char *>(encoded));
    env->ReleaseByteArrayElements (dest, buf, 0);
    return true;
#else
    return false;
#endif
}

JNIEXPORT jboolean JNICALL
Java_org_zeromq_jni_ZMQ_zmq_1curve_1keypair (JNIEnv *env, jclass c, jobject pub, jobject secret)
{
#if ZMQ_VERSION >= ZMQ_MAKE_VERSION(4,0,5)
    char public_key [41];
    char secret_key [41];
    int rc = zmq_curve_keypair (public_key, secret_key);
    jstring r1 = env->NewStringUTF(public_key);
    env->CallObjectMethod(pub, charBufferPutMID, r1);
    env->CallObjectMethod(pub, charBufferFlipMID);
    jstring r2 = env->NewStringUTF(secret_key);
    env->CallObjectMethod(secret, charBufferPutMID, r2);
    env->CallObjectMethod(secret, charBufferFlipMID);
    return rc == 0;
#else
    errno = ENOTSUP;
    return false;
#endif
}
