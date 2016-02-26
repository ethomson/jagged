#ifndef GIT_JAVA_UTIL_H
#define GIT_JAVA_UTIL_H

#include <stdint.h>
#include <limits.h>
#include <string.h>

#ifdef GIT_WIN32
# include <windows.h>
# ifdef __CRT__NO_INLINE
# undef __CRT__NO_INLINE
#  include <strsafe.h>
# define __CRT__NO_INLINE
# else
#  include <strsafe.h>
# endif
#endif

#include <jni.h>

#include <git2.h>

#define GIT_UNUSED(x) ((void)(x))

#ifdef _MSC_VER
# define GIT_INLINE(type) static __inline type
#else
# define GIT_INLINE(type) static inline type
#endif

#include "exception.h"

#define GITERR_CHECK_ALLOC(ptr) \
	if ((ptr) == NULL) { \
		giterr_set_oom(); \
		return -1; \
	}

#define GITERR_JAVA INT_MAX

#ifdef _WIN32
typedef WCHAR native_char;

typedef SSIZE_T ssize_t;
#else
typedef char native_char;
#endif

GIT_INLINE(jstring) git_java_utf8_to_jstring(
	JNIEnv *env,
	const char *utf8str)
{
	return (*env)->NewStringUTF(env, utf8str);
}

GIT_INLINE(jstring) git_java_native_to_jstring(
	JNIEnv *env,
	const native_char *nativestr)
{
#if defined(_WIN32)
	size_t len;

	if (FAILED(StringCchLength(nativestr, STRSAFE_MAX_CCH, &len)))
		return NULL;

	/*
	 * size_t is larger than jsize on 64-bit Windows (64-bit vs. 32-bit),
	 * but STRSAFE_MAX_CCH is always INT_MAX or smaller so we're safe to cast.
	 *
	 * strsafe.h says: "The user may override STRSAFE_MAX_CCH, but it must
	 * always be less than INT_MAX"
	*/
	return (*env)->NewString(env, nativestr, (jsize) len);
#else
	return (*env)->NewStringUTF(env, nativestr);
#endif
}

GIT_INLINE(const char *) git_java_jstring_to_utf8(
	JNIEnv *env,
	jstring javastr)
{
	return (*env)->GetStringUTFChars(env, javastr, NULL);
}

GIT_INLINE(const native_char *) git_java_jstring_to_native(
	JNIEnv *env,
	jstring javastr)
{
#if defined(_WIN32)
	return (*env)->GetStringChars(env, javastr, NULL);
#else
	return (*env)->GetStringUTFChars(env, javastr, NULL);
#endif
}

GIT_INLINE(void) git_java_utf8_free(
	JNIEnv *env,
	jstring javastr,
	const char *utf8str)
{
	(*env)->ReleaseStringUTFChars(env, javastr, utf8str);
}

GIT_INLINE(void) git_java_native_free(
	JNIEnv *env,
	jstring javastr,
	const native_char *nativestr)
{
	if (nativestr == NULL)
		return;

#if defined(_WIN32)
	(*env)->ReleaseStringChars(env, javastr, nativestr);
#else
	(*env)->ReleaseStringUTFChars(env, javastr, nativestr);
#endif
}

GIT_INLINE(jobjectArray)
git_java_utf8_array_to_jstring_array(
	JNIEnv *env,
	jsize len,
	const char **strarray_utf8)
{
	jobjectArray strarray_java;
	jclass string_class;
	jstring str;
	jsize i;

	if ((string_class = (*env)->FindClass(env, "java/lang/String")) == NULL ||
		(strarray_java = (*env)->NewObjectArray(env, len, string_class, NULL)) == NULL)
		return NULL;

	for (i = 0; i < len; i++) {
		if ((str = git_java_utf8_to_jstring(env, strarray_utf8[i])) == NULL)
			return NULL;

		(*env)->SetObjectArrayElement(env, strarray_java, i, str);
	}

	return strarray_java;
}

GIT_INLINE(void *)
git_java_ptr_from_jlong(jlong handle)
{
	void *ptr = 0;

	memcpy(&ptr, &handle, sizeof(void *));

	return ptr;
}

GIT_INLINE(jlong)
git_java_jlong_from_ptr(void *ptr)
{
	jlong handle;

	memset(&handle, 0, sizeof(jlong));
	memcpy(&handle, &ptr, sizeof(void *));

	return handle;
}

GIT_INLINE(void *)
git_java_handle_get(JNIEnv *env, jobject obj)
{
	jlong handle;
	jclass obj_class;
	jmethodID obj_gethandlemethod;

	obj_class = (*env)->GetObjectClass(env, obj);

	if (obj_class == NULL) {
		git_java_exception_throw(env, "object class not found");
		return 0;
	}

	obj_gethandlemethod = (*env)->GetMethodID(env, obj_class, "getHandle", "()J");

	if (obj_gethandlemethod == NULL) {
		git_java_exception_throw(env, "object class getHandle method not found");
		return 0;
	}

	handle = (*env)->CallLongMethod(env, obj, obj_gethandlemethod);

	return git_java_ptr_from_jlong(handle);
}

GIT_INLINE(void)
git_java_handle_set(JNIEnv *env, jobject obj, void *ptr)
{
	jlong handle;
	jclass obj_class;
	jmethodID obj_sethandlemethod;

	handle = git_java_jlong_from_ptr(ptr);
	obj_class = (*env)->GetObjectClass(env, obj);

	if (obj_class == NULL) {
		git_java_exception_throw(env, "object class not found");
		return;
	}

	obj_sethandlemethod = (*env)->GetMethodID(env, obj_class, "setHandle", "(J)V");

	if (obj_sethandlemethod == NULL) {
		git_java_exception_throw(env, "object class setHandle method not found");
		return;
	}

	(*env)->CallVoidMethod(env, obj, obj_sethandlemethod, handle);
}

#endif /* GIT_JAVA_UTIL_H */
