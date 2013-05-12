#ifndef GIT_JAVA_EXCEPTION_H
#define GIT_JAVA_EXCEPTION_H

#include <jni.h>

#include "util.h"

#define GIT_JAVA_CLASS_EXCEPTION "org/libgit2/jagged/core/GitException"

GIT_INLINE(void) git_java_exception_throw_from_giterr(JNIEnv *env, int errno)
{
	const git_error *error = giterr_last();
	const char *classname;
	jclass exception_class;

	assert(env);
	assert(error);

	/* TODO: switch based on type */
	GIT_UNUSED(errno);

	classname = GIT_JAVA_CLASS_EXCEPTION;

	if ((exception_class = (*env)->FindClass(env, classname)) == NULL ||
		(*env)->ThrowNew(env, exception_class, error->message) != 0)
		(*env)->FatalError(env, error->message);
}

GIT_INLINE(void) git_java_exception_throw(JNIEnv *env, const char *message)
{
	jclass exception_class;

	assert(env);

	/* TODO: this should take a printf style arg */
	if (message == NULL)
		(*env)->FatalError(env, "Unknown JNI error occurred");

	if ((exception_class = (*env)->FindClass(env, GIT_JAVA_CLASS_EXCEPTION)) == NULL ||
		(*env)->ThrowNew(env, exception_class, message) != 0)
		(*env)->FatalError(env, message);
}

#endif /* GIT_JAVA_EXCEPTION_H */
