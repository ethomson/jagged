
#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "util.h"

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_errorLast(
	JNIEnv *env,
	jclass class)
{
	jclass error_class;
	jstring msg_java;
	jmethodID error_initmethod;
	jobject error_java;
	const git_error *error;

	GIT_UNUSED(class);

	if ((error = giterr_last()) == NULL)
		return NULL;

	error_class = (*env)->FindClass(env,
		"org/libgit2/jagged/core/GitError");

	if (error_class == NULL)
		return NULL;

	error_initmethod = (*env)->GetMethodID(env, error_class,
		"<init>", "(ILjava/lang/String;)V");

	if (error_initmethod == NULL)
		return NULL;
	
	msg_java = git_java_utf8_to_jstring(env, error->message);

	if (msg_java == NULL)
		return NULL;

	error_java = (*env)->NewObject(env, error_class, error_initmethod,
		(jint) error->klass, msg_java);

	return error_java;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_threadsInit(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(env);
	GIT_UNUSED(class);

	git_threads_init();
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_threadsShutdown(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(env);
	GIT_UNUSED(class);

	git_threads_shutdown();
}

