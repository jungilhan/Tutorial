#include "first.h"
#include <jni.h>

/*
 * Class:     com_tutorial_ndk_NDKExamActivity
 * Method:    add
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_tutorial_ndk_NDKExamActivity_add
  (JNIEnv *env, jobject this, jint x, jint y)
{
	return first(x, y);
}

