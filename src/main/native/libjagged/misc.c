
#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "util.h"
#include "reference.h"
#include "oid.h"

#define GIT_JAVA_CLASS_VERSION "org/libgit2/jagged/Version"
#define GIT_JAVA_CLASS_OPTION_CACHED_STATISTICS "org/libgit2/jagged/Options$CacheStatistics"

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getCapabilities(
	JNIEnv *env,
	jclass class)
{
	GIT_UNUSED(env);
	GIT_UNUSED(class);

	return git_libgit2_capabilities();
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getLibGit2Version(
	JNIEnv *env,
	jclass class)
{
	jclass version_class;
	jmethodID version_ctormethod;
	int major, minor, revision;

	GIT_UNUSED(class);

	git_libgit2_version(&major, &minor, &revision);

	if ((version_class = (*env)->FindClass(env, GIT_JAVA_CLASS_VERSION)) == NULL ||
		(version_ctormethod = (*env)->GetMethodID(env, version_class, "<init>", "(III)V")) == NULL)
		return NULL;

	return (*env)->NewObject(env, version_class, version_ctormethod, major, minor, revision);
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setOptionMmapWindowSize(
	JNIEnv *env,
	jclass class,
	jlong size)
{
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_SET_MWINDOW_SIZE, (size_t)size)) < 0)
		git_java_exception_throw_giterr(env, error);
}

JNIEXPORT jlong JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getOptionMmapWindowSize(
	JNIEnv *env,
	jclass class)
{
	size_t size = 0;
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_GET_MWINDOW_SIZE, &size)) < 0)
		git_java_exception_throw_giterr(env, error);

	return size;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setOptionMmapWindowMappedLimit(
	JNIEnv *env,
	jclass class,
	jlong size)
{
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_SET_MWINDOW_MAPPED_LIMIT, (size_t)size)) < 0)
		git_java_exception_throw_giterr(env, error);
}

JNIEXPORT jlong JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getOptionMmapWindowMappedLimit(
	JNIEnv *env,
	jclass class)
{
	size_t size = 0;
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_GET_MWINDOW_MAPPED_LIMIT, &size)) < 0)
		git_java_exception_throw_giterr(env, error);

	return size;
}

JNIEXPORT jstring JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getOptionSearchPath(
	JNIEnv *env,
	jclass class,
	jint level)
{
	char buf[4096];
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_GET_SEARCH_PATH, level, buf, 4096)) < 0 &&
		error == GIT_EBUFS) {
		git_java_exception_throw(env, "search path too large");
		return NULL;
	} else if (error < 0) {
		git_java_exception_throw_giterr(env, error);
		return NULL;
	}

	return git_java_utf8_to_jstring(env, buf);
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setOptionSearchPath(
	JNIEnv *env,
	jclass class,
	jint level,
	jstring path_java)
{
	const char *path = NULL;
	int error = 0;

	GIT_UNUSED(class);

	if (path_java != NULL &&
		(path = git_java_jstring_to_utf8(env, path_java)) == NULL)
		return;

	if ((error = git_libgit2_opts(GIT_OPT_SET_SEARCH_PATH, level, path)) < 0)
		git_java_exception_throw_giterr(env, error);
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setOptionEnableCaching(
	JNIEnv *env,
	jclass class,
	jboolean enabled)
{
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_ENABLE_CACHING, (int)enabled)) < 0)
		git_java_exception_throw_giterr(env, error);
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setOptionCacheObjectLimit(
	JNIEnv *env,
	jclass class,
	jint type,
	jlong size)
{
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_SET_CACHE_OBJECT_LIMIT, type, (size_t)size)) < 0)
		git_java_exception_throw_giterr(env, error);
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setOptionCacheMaxSize(
	JNIEnv *env,
	jclass class,
	jlong max)
{
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_SET_CACHE_MAX_SIZE, max)) < 0)
		git_java_exception_throw_giterr(env, error);
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getOptionCachedStatistics(
	JNIEnv *env,
	jclass class)
{
	jclass statistics_class;
	jmethodID statistics_ctormethod;
	ssize_t used, max;
	int error = 0;

	GIT_UNUSED(class);

	if ((error = git_libgit2_opts(GIT_OPT_GET_CACHED_MEMORY, &used, &max)) < 0)
		git_java_exception_throw_giterr(env, error);

	if ((statistics_class = (*env)->FindClass(env, GIT_JAVA_CLASS_OPTION_CACHED_STATISTICS)) == NULL ||
		(statistics_ctormethod = (*env)->GetMethodID(env, statistics_class, "<init>", "(JJ)V")) == NULL)
		return NULL;

	return (*env)->NewObject(env, statistics_class, statistics_ctormethod, used, max);
}
