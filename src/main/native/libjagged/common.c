
#include <assert.h>

#include <jni.h>
#include <git2.h>
#include <git2/common.h>

#include "common.h"
#include "util.h"

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getLibgit2Version(
	JNIEnv *env,
	jclass class)
{

    GIT_UNUSED(class);

    int major, minor, rev;

	git_libgit2_version(&major, &minor, &rev);

    jclass versionCls = (*env)->FindClass(env, GIT_JAVA_CLASS_COMMON_VERSION);

    jmethodID midInit = (*env)->GetMethodID(env, versionCls, "<init>", "(III)V");
    if (NULL == midInit) return NULL;
    jobject versionObj = (*env)->NewObject(env, versionCls, midInit, major, minor, rev);
    return versionObj;
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getLibgit2Capabilities(
	JNIEnv *env,
	jclass class)
{

    GIT_UNUSED(env);
    GIT_UNUSED(class);

    return git_libgit2_capabilities();
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getMmapWindowSize(
	JNIEnv *env,
	jclass class)
{

    GIT_UNUSED(class);

	int error = 0;
    size_t size;
	if ((error = git_libgit2_opts(GIT_OPT_GET_MWINDOW_SIZE, &size)) < 0)
		git_java_exception_throw_from_giterr(env, error);
    return size;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setMmapWindowSize(
	JNIEnv *env,
	jclass class,
	jint size)
{

    GIT_UNUSED(class);

	int error = 0;
    if ((error = git_libgit2_opts(GIT_OPT_SET_MWINDOW_SIZE, (size_t)size)) < 0)
        git_java_exception_throw_from_giterr(env, error);
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getMmapWindowMappedLimit(
	JNIEnv *env,
	jclass class)
{

    GIT_UNUSED(class);

	int error = 0;
    size_t size;
	if ((error = git_libgit2_opts(GIT_OPT_GET_MWINDOW_MAPPED_LIMIT, &size)) < 0)
		git_java_exception_throw_from_giterr(env, error);
    return size;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setMmapWindowMappedLimit(
	JNIEnv *env,
	jclass class,
	jint size)
{

    GIT_UNUSED(class);

	int error = 0;
    if ((error = git_libgit2_opts(GIT_OPT_SET_MWINDOW_MAPPED_LIMIT, (size_t)size)) < 0)
        git_java_exception_throw_from_giterr(env, error);
}

#define BUFFER_SIZE 4096

JNIEXPORT jstring JNICALL
Java_org_libgit2_jagged_core_NativeMethods_getSearchPath(
	JNIEnv *env,
	jclass class,
	jint level)
{

    GIT_UNUSED(class);

	int error = 0;
    char *buffer = NULL;
    jstring path_java = NULL;

    buffer = git__malloc(BUFFER_SIZE);
    if (buffer == NULL)
    	goto done;

	if ((error = git_libgit2_opts(GIT_OPT_GET_SEARCH_PATH, level, buffer, BUFFER_SIZE)) < 0)
	{
		git_java_exception_throw_from_giterr(env, error);
		goto done;
	}

    path_java = git_java_utf8_to_jstring(env, buffer);

done:
    git__free(buffer);
    return path_java;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_setSearchPath(
	JNIEnv *env,
	jclass class,
    jint level,
    jstring path_java)
{

    GIT_UNUSED(class);

	int error = 0;
    const char *path = NULL;

    if ((path = git_java_jstring_to_utf8(env, path_java)) == NULL)
        goto done;

    if ((error = git_libgit2_opts(GIT_OPT_SET_SEARCH_PATH, level, path)) < 0)
        git_java_exception_throw_from_giterr(env, error);

done:
	git_java_utf8_free(env, path_java, path);

}
