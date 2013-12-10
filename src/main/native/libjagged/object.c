
#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "repository.h"
#include "util.h"
#include "oid.h"

#define GIT_JAVA_CLASS_COMMIT "org/libgit2/jagged/Commit"

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_objectLookup(
	JNIEnv *env,
	jclass class,
	jobject repo_java,
	jobject oid_java,
	jint type)
{
	git_repository *repo;
	git_oid oid;
	git_object *object = NULL;
	jclass object_class;
	jmethodID object_initmethod;
	jobject object_java = NULL;
	const char *object_classname;
	int error = 0;

	assert(env);
	assert(class);
	assert(repo_java);
	assert(oid_java);

	if ((repo = git_java_handle_get(env, repo_java)) == NULL ||
		git_java_objectid_tonative(&oid, env, oid_java) < 0)
		goto done;

	if ((error = git_object_lookup(&object, repo, &oid, type)) < 0) {
		git_java_exception_throw_giterr(env, error);
		goto done;
	}

	type = git_object_type(object);

	switch(type)
	{
	case GIT_OBJ_COMMIT:
		object_classname = GIT_JAVA_CLASS_COMMIT;
		break;
	default:
		git_java_exception_throw(env, "unknown object type: %d", type);
		break;
	}

	if ((object_class = (*env)->FindClass(env, object_classname)) == NULL ||
		(object_initmethod = (*env)->GetMethodID(env, object_class, "<init>", "(Lorg/libgit2/jagged/Repository;Lorg/libgit2/jagged/ObjectId;)V")) == NULL)
		return NULL;

	object_java = (*env)->NewObject(env, object_class, object_initmethod, repo_java, oid_java);

done:
	git_object_free(object);
	return object_java;
}
