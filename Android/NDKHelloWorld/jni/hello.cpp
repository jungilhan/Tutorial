#include "com_tutorial_ndkhelloworld_NDKHelloWorldActivity.h"
#include <string.h>

JNIEXPORT jstring JNICALL Java_com_tutorial_ndkhelloworld_NDKHelloWorldActivity_userName(JNIEnv * env, jobject obj, jstring string)
{
	const char* str = env->GetStringUTFChars(string, 0);
	char cap[128];

	strcpy(cap, str);
	strcat(cap, " [jihan]");
	env->ReleaseStringUTFChars(string, str);
	return env->NewStringUTF(cap);
}



