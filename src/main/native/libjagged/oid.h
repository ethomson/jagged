#ifndef GIT_JAVA_OID_H
#define GIT_JAVA_OID_H

#include <jni.h>

#define GIT_JAVA_CLASS_OID "org/libgit2/jagged/Oid"

GIT_INLINE(jobject) git_java_oid_init(JNIEnv *env, const git_oid *oid)
{
	jclass oid_class;
	jmethodID oid_initmethod;
	jbyteArray oid_java;

	if ((oid_class = (*env)->FindClass(env, GIT_JAVA_CLASS_OID)) == NULL ||
		(oid_initmethod = (*env)->GetMethodID(env, oid_class, "<init>", "([B)V")) == NULL ||
		(oid_java = (*env)->NewByteArray(env, 20)) == NULL)
		return NULL;

	(*env)->SetByteArrayRegion(env, oid_java, 0, 20, (const jbyte *)oid->id);

	return (*env)->NewObject(env, oid_class, oid_initmethod, oid_java);
}

#endif /* GIT_JAVA_OID_H */
