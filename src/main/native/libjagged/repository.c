
#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "util.h"

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryOpen(
	JNIEnv *env,
	jclass class,
	jobject out_handle,
	jstring path_java)
{
	const char *path_utf8 = NULL;
	git_repository *repo;
	int error = 0;

	assert(env);
	assert(class);
	assert(out_handle);
	assert(path_java);

	path_utf8 = git_java_jstring_to_utf8(env, path_java);
	GITERR_CHECK_ALLOC(path_utf8);

	if ((error = git_repository_open(&repo, path_utf8)) >= 0)
		error = git_java_handle_set(env, out_handle, repo);

	git_java_utf8_free(env, path_java, path_utf8);

	return error;
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryInit(
	JNIEnv *env,
	jclass class,
	jobject out_handle,
	jstring path_java,
	jboolean bare)
{
	const char *path_utf8 = NULL;
	git_repository *repo;
	int error = 0;

	assert(env);
	assert(class);
	assert(out_handle);
	assert(path_java);

	path_utf8 = git_java_jstring_to_utf8(env, path_java);
	GITERR_CHECK_ALLOC(path_utf8);

	if ((error = git_repository_init(&repo, path_utf8, bare ? 1 : 0)) >= 0)
		error = git_java_handle_set(env, out_handle, repo);

	git_java_utf8_free(env, path_java, path_utf8);

	return error;
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryClone(
	JNIEnv *env,
	jclass class,
	jobject out_handle,
	jstring sourceurl_java,
	jstring path_java)
{
	const char *sourceurl_utf8 = NULL, *path_utf8 = NULL;
	git_repository *repo;
	int error = 0;

	assert(env);
	assert(class);
	assert(out_handle);
	assert(sourceurl_java);
	assert(path_java);

	sourceurl_utf8 = git_java_jstring_to_utf8(env, sourceurl_java);
	GITERR_CHECK_ALLOC(sourceurl_utf8);

	path_utf8 = git_java_jstring_to_utf8(env, path_java);
	GITERR_CHECK_ALLOC(path_utf8);

	if ((error = git_clone(&repo, sourceurl_utf8, path_utf8, NULL)) >= 0)
		error = git_java_handle_set(env, out_handle, repo);

	git_java_utf8_free(env, sourceurl_java, sourceurl_utf8);
	git_java_utf8_free(env, path_java, path_utf8);

	return error;
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryIsBare(
	JNIEnv *env,
	jclass class,
	void *repo_ptr)
{
	git_repository *repo = repo_ptr;

	assert(env);
	assert(class);
	assert(repo);

	return git_repository_is_bare(repo);
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryHead(
	JNIEnv *env,
	jclass class,
	jobject out_handle,
	void *repo_ptr)
{
	git_repository *repo = repo_ptr;
	git_reference *ref;
	int error;

	assert(env);
	assert(class);
	assert(repo);

	if ((error = git_repository_head(&ref, repo)) < 0)
		return error;

	if ((error = git_java_handle_set(env, out_handle, ref)) < 0) {
		git_reference_free(ref);
		return error;
	}

	return 0;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryFree(
	JNIEnv *env,
	jclass class,
	void *repo_ptr)
{
	git_repository *repo = repo_ptr;

	assert(env);
	assert(class);
	assert(repo);

	git_repository_free(repo);
}

