#ifndef GIT_JAVA_REPOSITORY_H
#define GIT_JAVA_REPOSITORY_H

#include <jni.h>

#include "oid.h"
#include "util.h"

#define GIT_JAVA_CLASS_REPOSITORY "org/libgit2/jagged/Repository"

GIT_INLINE(jobject) git_java_repository_init(JNIEnv *env, git_repository *repo)
{
	jclass repo_class;
	jmethodID repo_initmethod;

	if ((repo_class = (*env)->FindClass(env, GIT_JAVA_CLASS_REPOSITORY)) == NULL ||
		(repo_initmethod = (*env)->GetMethodID(env, repo_class, "<init>", "(JZ)V")) == NULL)
		return NULL;

	return (*env)->NewObject(env, repo_class, repo_initmethod, git_java_jlong_from_ptr(repo), git_repository_is_bare(repo));
}

#endif /* GIT_JAVA_REPOSITORY_H */
