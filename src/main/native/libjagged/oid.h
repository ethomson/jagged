#ifndef GIT_JAVA_OID_H
#define GIT_JAVA_OID_H

#include <jni.h>

#define GIT_JAVA_CLASS_OID "org/libgit2/jagged/ObjectId"

GIT_INLINE(jobject) git_java_objectid_init(JNIEnv *env, const git_oid *oid)
{
	jclass oid_class;
	jmethodID oid_initmethod;
	jbyteArray oid_bytearray;

	if ((oid_class = (*env)->FindClass(env, GIT_JAVA_CLASS_OID)) == NULL ||
		(oid_initmethod = (*env)->GetMethodID(env, oid_class, "<init>", "([B)V")) == NULL ||
		(oid_bytearray = (*env)->NewByteArray(env, 20)) == NULL)
		return NULL;

	(*env)->SetByteArrayRegion(env, oid_bytearray, 0, 20, (const jbyte *)oid->id);

	return (*env)->NewObject(env, oid_class, oid_initmethod, oid_bytearray);
}

GIT_INLINE(int) git_java_objectid_tonative(git_oid *out, JNIEnv *env, jobject oid_java)
{
	jclass oid_class;
	jmethodID oid_getmethod;
	jbyteArray oid_bytearray;
	jbyte *oid_byteptr;

	if ((oid_class = (*env)->GetObjectClass(env, oid_java)) == NULL ||
		(oid_getmethod = (*env)->GetMethodID(env, oid_class, "getOid", "()[B")) == NULL ||
		(oid_bytearray = (*env)->CallObjectMethod(env, oid_java, oid_getmethod)) == NULL)
		return -1;

	oid_byteptr = (*env)->GetByteArrayElements(env, oid_bytearray, NULL);
	memcpy(out->id, oid_byteptr, GIT_OID_RAWSZ);
	(*env)->ReleaseByteArrayElements(env, oid_bytearray, oid_byteptr, JNI_ABORT);

	return 0;
}

#endif /* GIT_JAVA_OID_H */
