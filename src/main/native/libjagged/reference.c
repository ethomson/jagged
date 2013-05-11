
#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "util.h"

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_referenceType(
	JNIEnv *env,
	jclass class,
	void *ref_ptr)
{
	const git_reference *ref = ref_ptr;

	assert(env);
	assert(class);
	assert(ref);

	return git_reference_type(ref);
}

JNIEXPORT jint JNICALL
Java_org_libgit2_jagged_core_NativeMethods_referenceResolve(
	JNIEnv *env,
	jclass class,
	jobject out_handle,
	void *ref_ptr)
{
	const git_reference *ref = ref_ptr;
	git_reference *out;
	int error = 0;

	assert(env);
	assert(class);
	assert(out_handle);
	assert(ref);

	if ((error = git_reference_resolve(&out, ref)) >= 0)
		error = git_java_handle_set(env, out_handle, out);

	return error;
}

JNIEXPORT void JNICALL
Java_org_libgit2_jagged_core_NativeMethods_referenceFree(
	JNIEnv *env,
	jclass class,
	void *ref_ptr)
{
	git_reference *ref = ref_ptr;

	assert(env);
	assert(class);
	assert(ref);

	git_reference_free(ref);
}

