#ifndef GIT_JAVA_REFERENCE_H
#define GIT_JAVA_REFERENCE_H

#include <jni.h>

#include "oid.h"
#include "util.h"

#define GIT_JAVA_CLASS_REFERENCE_DIRECT "org/libgit2/jagged/Reference$DirectReference"
#define GIT_JAVA_CLASS_REFERENCE_SYMBOLIC "org/libgit2/jagged/Reference$SymbolicReference"

GIT_INLINE(jobject) git_java_reference_init(JNIEnv *env, jobject repo_java, git_reference *ref)
{
	int ref_type;
	jstring name_java;
	jclass ref_class;
	jmethodID ref_initmethod;

	ref_type = git_reference_type(ref);

	if ((name_java = git_java_utf8_to_jstring(env, git_reference_name(ref))) == NULL)
		return NULL;

	if (ref_type == GIT_REF_OID) {
		jobject target_java;

		if ((ref_class = (*env)->FindClass(env, GIT_JAVA_CLASS_REFERENCE_DIRECT)) == NULL ||
			(ref_initmethod = (*env)->GetMethodID(env, ref_class, "<init>", "(Lorg/libgit2/jagged/Repository;Ljava/lang/String;Lorg/libgit2/jagged/Oid;)V")) == NULL ||
			(target_java = git_java_oid_init(env, git_reference_target(ref))) == NULL)
			return NULL;

		return (*env)->NewObject(env, ref_class, ref_initmethod, repo_java, name_java, target_java);
	} else if (ref_type == GIT_REF_SYMBOLIC) {
		jstring target_java;

		if ((ref_class = (*env)->FindClass(env, GIT_JAVA_CLASS_REFERENCE_SYMBOLIC)) == NULL ||
			(ref_initmethod = (*env)->GetMethodID(env, ref_class, "<init>", "(Lorg/libgit2/jagged/Repository;Ljava/lang/String;Ljava/lang/String;)V")) == NULL ||
			(target_java = git_java_utf8_to_jstring(env, git_reference_symbolic_target(ref))) == NULL)
			return NULL;

		return (*env)->NewObject(env, ref_class, ref_initmethod, repo_java, name_java, target_java);
	}

	git_java_exception_throw(env, "unknown reference type");
	return NULL;
}

#endif /* GIT_JAVA_REFERENCE_H */
