#include <assert.h>
#include <string.h>

#include <jni.h>

#include <git2.h>
#include <sys/stat.h>
#include <direct.h>

#include "testutil.h"
#include "util.h"

int jni_get_method_id_by_class(jmethodID *methodId, jclass clazz, char *method_name, char *method_signature,
                               JNIEnv *env) {
	*methodId = (*env)->GetMethodID(env, clazz, method_name, method_signature);
	return *methodId != NULL ? 1 : 0;
}

int jni_call_object_method(jobject *result, jobject object, char *class_name, char *method_name, char *method_signature, JNIEnv *env) {
	jclass java_class;
	if (class_name != NULL) {
		java_class = (*env)->FindClass(env, class_name);
	}
	else {
		java_class = (*env)->GetObjectClass(env, object);
	}
	if (java_class == NULL) {
		return 0;
	}

	jmethodID methodId;
	if (!jni_get_method_id_by_class(&methodId, java_class, method_name, method_signature, env)) {
		return 0;
	}

	*result = (*env)->CallObjectMethod(env, object, methodId, object);
	return 1;
}

int make_test_dir(const char *root, char *relpath) {
	char abspath[256];
	concat(abspath, root, relpath);

	struct stat st = {0};
	return stat(abspath, &st) == -1 ? _mkdir(abspath) : 0;
}

int write_test_file(const char *root, char *relpath, char *content) {
	char abspath[256];
	concat(abspath, root, relpath);

	FILE *f;
	fopen_s(&f, abspath, "w");
	if (f == NULL) {
		return -1;
	}

	fprintf(f, "%s", content);
	fclose(f);
	return 0;
}

int create_test_repository(const char *path, const git_signature *author) {
	git_repository *repo = NULL;
	int error = 0;
	if ((error = git_repository_init(&repo, path, 0)) < 0)
		return error;

	make_test_dir(path, "a");
	write_test_file(path, ".gitattributes", "* text=auto\n");
	write_test_file(path, "one.txt", "This is file one!\n");
	write_test_file(path, "two.txt", "This is file two!\n");
	write_test_file(path, "three.txt", "This is file three!\n");
	write_test_file(path, "a/four.txt", "This is file four!\n");
	write_test_file(path, "a/five.txt", "This is file five!\n");

	git_index *index;
	if ((error = git_repository_index(&index, repo)) < 0)
		return error;

	git_index_read(index, 0);

	char *match_all = "*";
	git_strarray arr;
	arr.strings = &match_all;
	arr.count = 1;

	if ((error = git_index_add_all(index, &arr, GIT_INDEX_ADD_DEFAULT,
	                               NULL, NULL)) < 0)
		return error;
	if ((error = git_index_write(index)) < 0)
		return error;

	git_oid commit_id, tree_id;
	git_tree *tree = NULL;

	git_signature *sig;
	git_signature_dup(&sig, author);

	if ((error = git_index_write_tree(&tree_id, index)) < 0)
		return error;
	if ((error = git_tree_lookup(&tree, repo, &tree_id)) < 0)
		return error;

	sig->when.time = 0;
	sig->when.offset = 0;
	if ((error = git_commit_create_v(&commit_id, repo, "refs/heads/master", sig, sig, NULL, "Initial revision", tree, 0)) < 0)
		return error;

	git_commit *parent;
	git_commit_lookup(&parent, repo, &commit_id);

	write_test_file(path, "one.txt", "This is file one!!\n");
	write_test_file(path, "two.txt", "This is file two!!\n");
	write_test_file(path, "three.txt", "This is file three!!\n");

	if ((error = git_index_add_all(index, &arr, GIT_INDEX_ADD_DEFAULT,
	                               NULL, NULL)) < 0)
		return error;
	if ((error = git_index_write(index)) < 0)
		return error;

	if ((error = git_index_write_tree(&tree_id, index)) < 0)
		return error;
	if ((error = git_tree_lookup(&tree, repo, &tree_id)) < 0)
		return error;

	sig->when.time = 3600;
	if ((error = git_commit_create_v(&commit_id, repo, "refs/heads/master", sig, sig, NULL, "Updated!", tree, 1, parent)) < 0)
		return error;

	git_repository_free(repo);
	return 0;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeTestMethods_createTestRepository(
		JNIEnv *env,
		jclass class,
		jobject path_java,
		jobject signature_java) {
	assert(env);
	assert(class);
	assert(path_java);

	const char *path = NULL;
	if ((path = git_java_jstring_to_utf8(env, path_java)) == NULL)
		return;

	jobject name_java, email_java;
	if (jni_call_object_method(&name_java, signature_java, "org/libgit2/jagged/Signature", "getName", "()Ljava/lang/String;", env) < 0 ||
	    jni_call_object_method(&email_java, signature_java, "org/libgit2/jagged/Signature", "getEmail", "()Ljava/lang/String;", env) < 0)
		return;

	const char *name, *email;
	if ((name = git_java_jstring_to_utf8(env, name_java)) == NULL ||
	    (email = git_java_jstring_to_utf8(env, email_java)) == NULL)
		return;

	int error = 0;
	git_signature *signature;
	git_time_t time = 0;
	if ((error = git_signature_new(&signature, name, email, time, 0)) < 0)
		git_java_exception_throw_giterr(env, error);

	if ((error = create_test_repository(path, signature) < 0))
		git_java_exception_throw_giterr(env, error);

	git_java_utf8_free(env, path_java, path);
}
