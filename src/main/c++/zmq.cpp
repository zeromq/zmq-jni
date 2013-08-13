#include "zmq.h"
#include "org_zeromq_ZMQ.h"

JNIEXPORT
jint JNICALL
Java_org_zeromq_ZMQ_version (JNIEnv *env, jclass c)
{
    return ZMQ_VERSION;
}

JNIEXPORT
jlong JNICALL
Java_org_zeromq_ZMQ_zmq_1ctx_1new  (JNIEnv *env, jclass c)
{
    void *context = zmq_ctx_new();
    if(context)
        return (long) context;
    return -1;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_ZMQ_zmq_1ctx_1set (JNIEnv *env, jclass c, jlong context, jint name, jint value)
{
    int rc = zmq_ctx_set((void *) context, name, value);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_ZMQ_zmq_1ctx_1get (JNIEnv *env, jclass c, jlong context, jint value)
{
    return zmq_ctx_get((void *) context, value);
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_ZMQ_zmq_1ctx_1destroy (JNIEnv *env, jclass c, jlong context)
{
    int rc = zmq_ctx_destroy((void *) context);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jlong JNICALL
Java_org_zeromq_ZMQ_zmq_1socket (JNIEnv *env, jclass c, jlong context, jint type)
{
    void *socket = zmq_socket((void *) context, type);
    if(socket)
        return (long) socket;
    return -1;
}

JNIEXPORT
jboolean JNICALL
Java_org_zeromq_ZMQ_zmq_1bind (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
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
Java_org_zeromq_ZMQ_zmq_1connect (JNIEnv *env, jclass c, jlong socket, jstring endpoint)
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
Java_org_zeromq_ZMQ_zmq_1close (JNIEnv *env, jclass c, jlong socket)
{
    int rc = zmq_close((void *) socket);
    if(rc == 0)
        return JNI_TRUE;
    return JNI_FALSE;
}

JNIEXPORT
jint JNICALL
Java_org_zeromq_ZMQ_zmq_1errno (JNIEnv *env, jclass c)
{
    return zmq_errno();
}

JNIEXPORT
jstring JNICALL
Java_org_zeromq_ZMQ_zmq_1strerror (JNIEnv *env, jclass c, jint errnum)
{
    const char *str = zmq_strerror (errnum);
    return env->NewStringUTF(str);
}

