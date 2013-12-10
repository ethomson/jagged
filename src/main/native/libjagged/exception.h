#ifndef GIT_JAVA_EXCEPTION_H
#define GIT_JAVA_EXCEPTION_H

#include <jni.h>

#ifndef GIT_WIN32
# include <errno.h>
# include <string.h>
#endif

#include "util.h"

#define GIT_JAVA_CLASS_EXCEPTION "org/libgit2/jagged/core/GitException"

GIT_INLINE(void) git_java_exception_throw_giterr(
	JNIEnv *env,
	int error_type)
{
	const git_error *error = giterr_last();
	const char *classname;
	jclass exception_class;

	assert(env);
	assert(error);

	/* TODO: switch based on type */
	GIT_UNUSED(error_type);

	classname = GIT_JAVA_CLASS_EXCEPTION;

	if ((exception_class = (*env)->FindClass(env, classname)) == NULL ||
		(*env)->ThrowNew(env, exception_class, error->message) != 0)
		(*env)->FatalError(env, error->message);
}

GIT_INLINE(void) git_java_exception_throw_errno(
	JNIEnv *env)
{
	jclass exception_class;

#ifdef GIT_WIN32
	char message[256];

	if (strerror_s(message, 256, errno) < 0)
		(*env)->FatalError(env, "Unknown JNI error occurred");
	else
#else
	char *message = strerror(errno);
#endif
	if((exception_class = (*env)->FindClass(env, GIT_JAVA_CLASS_EXCEPTION)) == NULL ||
		(*env)->ThrowNew(env, exception_class, message) != 0)
		(*env)->FatalError(env, message);
}

GIT_INLINE(void) git_java_exception_throw_oom(JNIEnv *env)
{
	jclass exception_class;

	assert(env);

	if ((exception_class = (*env)->FindClass(env, "java/lang/OutOfMemoryError")) == NULL ||
		(*env)->ThrowNew(env, exception_class, "Native memory space") != 0)
		(*env)->FatalError(env, "Out of memory");
}

#ifdef __GNUC__
GIT_INLINE(void) git_java_exception_throw(JNIEnv *env, const char *fmt, ...)
	__attribute__ ((format (printf, 2, 3)));
#endif

GIT_INLINE(void) git_java_exception_throw(JNIEnv *env, const char *fmt, ...)
{
	jclass exception_class;
	va_list ap;
	char *message = NULL;
	int ret, size;

	assert(env);

	if (fmt == NULL) {
		(*env)->FatalError(env, "Unknown JNI error occurred");
		return;
	}

#ifdef _MSC_VER
	va_start(ap, fmt);

	if ((size = _vscprintf(fmt, ap)) < 0) {
		git_java_exception_throw_errno(env);
		goto done;
	}

	va_end(ap);
#else
	size = strlen(fmt);
#endif

	if ((message = malloc(++size)) == NULL) {
		git_java_exception_throw_oom(env);
		return;
	}

	while (1) {
		va_start(ap, fmt);
#ifdef _MSC_VER
		ret = vsnprintf_s(message, size, _TRUNCATE, fmt, ap);
#else
		ret = vsnprintf(message, size, fmt, ap);
#endif
		va_end(ap);

		if (ret < 0) {
			git_java_exception_throw_errno(env);
			goto done;
		}

		if (ret < size)
			break;

		size = ret + 1;

#ifdef _MSC_VER
# pragma warning(disable:6308)
#endif
		if ((message = realloc(message, size)) == NULL) {
			git_java_exception_throw_oom(env);
			return;
		}
	}

	if ((exception_class = (*env)->FindClass(env, GIT_JAVA_CLASS_EXCEPTION)) == NULL ||
		(*env)->ThrowNew(env, exception_class, message) != 0)
		(*env)->FatalError(env, message);

done:
	free(message);
}

#endif /* GIT_JAVA_EXCEPTION_H */
