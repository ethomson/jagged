#ifndef GIT_JAVA_UTIL_H
#define GIT_JAVA_UTIL_H

#include <stdint.h>
#include <limits.h>
#include <string.h>

#include <jni.h>

#include <git2.h>

#define GIT_UNUSED(x) ((void)(x))

#define GITERR_CHECK_ALLOC(ptr) \
	if (ptr == NULL) { \
		giterr_set_oom(); \
		return -1; \
	}

#define GITERR_JAVA INT_MAX

#ifdef _WIN32
typedef WCHAR native_char;
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

	if (FAILED(StringCchLength(native, STRSAFE_MAX_CCH, &len)))
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

GIT_INLINE(int) git_java_handle_set(
	JNIEnv *env, jobject handle, void *ptr)
{
	jlong value;
	jclass handle_class;
	jmethodID handle_setmethod;

	memset(&value, 0, sizeof(jlong));
	memcpy(&value, &ptr, sizeof(void *));

	/* TODO: can we cache this in the env somewhere? */

	handle_class = (*env)->GetObjectClass(env, handle);

	if (handle_class == NULL) {
		giterr_set_str(GITERR_JAVA, "Native.Handle class not found");
		return -1;
	}

	handle_setmethod = (*env)->GetMethodID(env, handle_class, "set", "(J)V");

	if (handle_setmethod == NULL) {
		giterr_set_str(GITERR_JAVA, "Native.Handle.set method not found");
		return -1;
	}

	(*env)->CallVoidMethod(env, handle, handle_setmethod, value);

	return 0;
}

#endif /* GIT_JAVA_UTIL_H */
