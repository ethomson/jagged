
#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "repository.h"
#include "reference.h"
#include "util.h"

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryOpen(
	JNIEnv *env,
	jclass class,
	jobject repo_java,
	jstring path_java)
{
	const char *path = NULL;
	git_repository *repo;
	int error = 0;

	assert(env);
	assert(class);
	assert(repo_java);
	assert(path_java);

	if ((path = git_java_jstring_to_utf8(env, path_java)) == NULL)
		return;

	if ((error = git_repository_open(&repo, path)) < 0)
		git_java_exception_throw_giterr(env, error);
	else
		git_java_handle_set(env, repo_java, repo);

	git_java_utf8_free(env, path_java, path);
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryInit(
	JNIEnv *env,
	jclass class,
	jstring path_java,
	jboolean bare)
{
	const char *path = NULL;
	git_repository *repo = NULL;
	jobject repo_java = NULL;
	int error = 0;

	assert(env);
	assert(class);
	assert(path_java);

	if ((path = git_java_jstring_to_utf8(env, path_java)) == NULL)
		return NULL;

	if ((error = git_repository_init(&repo, path, bare ? 1 : 0)) < 0)
		git_java_exception_throw_giterr(env, error);
	else
		repo_java = git_java_repository_init(env, repo);

	git_java_utf8_free(env, path_java, path);

	return repo_java;
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryClone(
	JNIEnv *env,
	jclass class,
	jstring sourceurl_java,
	jstring path_java)
{
	const char *sourceurl = NULL, *path = NULL;
	git_repository *repo;
	jobject repo_java = NULL;
	int error = 0;
	
	assert(env);
	assert(class);
	assert(sourceurl_java);
	assert(path_java);

	if ((path = git_java_jstring_to_utf8(env, path_java)) == NULL ||
		(sourceurl = git_java_jstring_to_utf8(env, sourceurl_java)) == NULL)
		return NULL;
		
	if ((error = git_clone(&repo, sourceurl, path, NULL)) < 0)
		git_java_exception_throw_giterr(env, error);
	else
		repo_java = git_java_repository_init(env, repo);

	git_java_utf8_free(env, sourceurl_java, sourceurl);
	git_java_utf8_free(env, path_java, path);

	return repo_java;
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryHead(
	JNIEnv *env,
	jclass class,
	jobject repo_java)
{
	git_repository *repo;
	git_reference *ref = NULL;
	jobject ref_java;
	int error;

	assert(env);
	assert(class);
	assert(repo_java);

	repo = git_java_handle_get(env, repo_java);

	if ((error = git_repository_head(&ref, repo)) < 0) {
		git_java_exception_throw_giterr(env, error);
		return NULL;
	}

	ref_java = git_java_reference_init(env, repo_java, ref);

	git_reference_free(ref);

	return ref_java;
}

JNIEXPORT jboolean JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryIsBare(
	JNIEnv *env,
	jclass class,
	jobject repo_java)
{
	git_repository *repo;
	int is_bare;

	assert(env);
	assert(class);
	assert(repo_java);

	repo = git_java_handle_get(env, repo_java);

	is_bare = git_repository_is_bare(repo);

	return is_bare ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_repositoryFree(
	JNIEnv *env,
	jclass class,
	jobject repo_java)
{
	git_repository *repo;

	assert(env);
	assert(class);
	assert(repo_java);

	repo = git_java_handle_get(env, repo_java);

	git_repository_free(repo);
}

