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
    void * context = zmq_ctx_new();
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
