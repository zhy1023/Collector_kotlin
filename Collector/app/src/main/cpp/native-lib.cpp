#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_zhy_collector_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello JNI";
    return env->NewStringUTF(hello.c_str());
}
