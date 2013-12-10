
#include <assert.h>

#include <jni.h>

#include <git2.h>

#include "util.h"

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeTestMethods_throwOutOfMemory(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(class);

	git_java_exception_throw_oom(env);
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeTestMethods_throwConstantString(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(class);

	git_java_exception_throw(env, "This is an exception");
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeTestMethods_throwFormattedString1(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(class);

	git_java_exception_throw(env, "This is a formatted %s string, using %s-like syntax", "exception", "printf");
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeTestMethods_throwFormattedString2(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(class);

	git_java_exception_throw(env, "%d Another %3d formatted %03d exception %1.3f message", 7, 7, 7, ((float)15.3/(float)3));
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeTestMethods_throwFormattedString3(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(class);

	git_java_exception_throw(env, "%s, %s",
		"This is a formatted exception string", "using printf-like syntax");
}
