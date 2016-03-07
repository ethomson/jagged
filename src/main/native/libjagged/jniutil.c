#include <assert.h>

#include <jni.h>

#include "jniutil.h"

int jni_get_class(jclass *class, char *class_name, JNIEnv *env) {
	*class = (*env)->FindClass(env, class_name);
	return *class != NULL ? 1 : 0;
}

int jni_get_method_id_by_class(jmethodID *methodId, jclass clazz, char *method_name, char *method_signature,
                               JNIEnv *env) {
	*methodId = (*env)->GetMethodID(env, clazz, method_name, method_signature);
	return *methodId != NULL ? 1 : 0;
}

int jni_get_method_id_by_object(jmethodID *methodId, jobject object, char *method_name, char *method_signature,
                                JNIEnv *env) {
	jclass class = (*env)->GetObjectClass(env, object);
	*methodId = (*env)->GetMethodID(env, class, method_name, method_signature);
	return *methodId != NULL ? 1 : 0;
}

int jni_get_static_object_field(jobject *field, jclass class, char *field_name, char *field_type, JNIEnv *env) {
	jfieldID fieldId = (*env)->GetStaticFieldID(env, class, field_name, field_type);
	if (fieldId == NULL) {
		return 0;
	}

	*field = (*env)->GetStaticObjectField(env, class, fieldId);
	return 1;
}

int jni_get_object_field(jobject *field, jobject object, char *field_name, char *field_type, JNIEnv *env) {
	jclass class = (*env)->GetObjectClass(env, object);
	jfieldID fieldId = (*env)->GetFieldID(env, class, field_name, field_type);
	if (fieldId == NULL) {
		return 0;
	}

	*field = (*env)->GetObjectField(env, object, fieldId);
	return 1;
}

int jni_get_int_field(jint *field, jobject object, char *field_name, JNIEnv *env) {
	jclass class = (*env)->GetObjectClass(env, object);
	jfieldID fieldId = (*env)->GetFieldID(env, class, field_name, "I");
	if (fieldId == NULL) {
		return 0;
	}

	*field = (*env)->GetIntField(env, object, fieldId);
	return 1;
}

int jni_constructor_init(jni_constructor *constructor, char *class_name, char *signature, JNIEnv *env) {
	if (!jni_get_class(&(constructor->class), class_name, env) ||
	    !jni_get_method_id_by_class(&(constructor->init), constructor->class, "<init>", signature, env))
		return 0;

	return 1;
}

jobject jni_constructor_invoke(jni_constructor *constructor, JNIEnv *env, ...) {
	assert(constructor->class && constructor->init);

	va_list args;
	va_start(args, env);
	jobject object = (*env)->NewObjectV(env, constructor->class, constructor->init, args);
	va_end(args);
	return object;
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

int jni_create_object_array(jobjectArray *result, char *class_name, jint size, JNIEnv *env) {
	jclass class = (*env)->FindClass(env, class_name);
	if (class == NULL) {
		return 0;
	}

	*result = (*env)->NewObjectArray(env, size, class, NULL);
	return *result != NULL ? 1 : 0;
}

