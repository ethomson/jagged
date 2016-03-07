#ifndef GIT_JAVA_JNI_UTIL_H
#define GIT_JAVA_JNI_UTIL_H

#include <jni.h>

typedef struct {
	jclass class;
	jmethodID init;
} jni_constructor;

int jni_get_class(jclass *class, char* class_name, JNIEnv *env);
int jni_get_method_id_by_class(jmethodID *methodId, jclass class, char* method_name, char* method_signature, JNIEnv* env);
int jni_get_method_id_by_object(jmethodID *methodId, jobject object, char* method_name, char* method_signature, JNIEnv* env);
int jni_get_object_field(jobject *field, jobject object, char* field_name, char* field_type, JNIEnv *env);
int jni_get_static_object_field(jobject *field, jclass class, char* field_name, char* field_type, JNIEnv *env);
int jni_get_int_field(jint *field, jobject object, char *field_name, JNIEnv *env);
int jni_call_object_method(jobject *result, jobject object, char *class_name, char *method_name, char *method_signature, JNIEnv *env);
int jni_create_object_array(jobjectArray *result, char *class_name, jint size, JNIEnv *env);
int jni_constructor_init(jni_constructor *constructor, char *class_name, char *signature, JNIEnv *env);
jobject jni_constructor_invoke(jni_constructor *constructor, JNIEnv *env, ...);

#endif /* GIT_JAVA_JNI_UTIL_H */
