#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_suixue_edu_college_util_JniUtil_getPrivateKey(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
