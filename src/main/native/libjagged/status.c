#include <assert.h>

#include <jni.h>
#include <git2.h>

#include "oid.h"

#define STATUS_SHOW_INDEX_ONLY "INDEX_ONLY"
#define STATUS_SHOW_WORKDIR_ONLY "WORKDIR_ONLY"
#define STATUS_SHOW_INDEX_AND_WORKDIR "INDEX_AND_WORKDIR"

#pragma warning ( disable : 4090 )
#pragma warning ( disable : 6386 )

jobject create_status_java(const git_status_entry *status, jni_constructor *status_constructor, int populate_oids, JNIEnv *env) {
	jint status_flags = (jint) status->status;
	const char *head_path = NULL;
	jlong head_size = 0;
	jint head_flags = 0;
	jint head_mode = 0;
	jobject head_id = NULL;

	const char *index_path = NULL;
	jlong index_size = 0;
	jint index_flags = 0;
	jint index_mode = 0;
	jobject index_id = NULL;

	const char *wt_path = NULL;
	jlong wt_size = 0;
	jint wt_flags = 0;
	jint wt_mode = 0;
	jobject wt_id = NULL;

	int head_index_flags = 0;
	int head_index_similarity = 0;
	int head_index_nfiles = 0;

	int index_wt_flags = 0;
	int index_wt_similarity = 0;
	int index_wt_nfiles = 0;

	git_diff_delta *head_to_index = status->head_to_index;
	if (head_to_index) {
		head_path = head_to_index->old_file.path;
		head_size = head_to_index->old_file.size;
		head_flags = (jint) head_to_index->old_file.flags;
		head_mode = head_to_index->old_file.mode;
		head_id = populate_oids ? git_java_objectid_init(env, &(head_to_index->old_file.id)) : NULL;
		index_path = head_to_index->new_file.path;
		index_size = head_to_index->new_file.size;
		index_flags = (jint) head_to_index->new_file.flags;
		index_mode = head_to_index->new_file.mode;
		index_id = populate_oids ? git_java_objectid_init(env, &(head_to_index->new_file.id)) : NULL;
	}

	git_diff_delta *index_to_workdir = status->index_to_workdir;
	if (index_to_workdir) {
		if (!head_to_index) {
			index_path = index_to_workdir->old_file.path;
			index_size = index_to_workdir->old_file.size;
			index_flags = (jint) index_to_workdir->old_file.flags;
			index_mode = index_to_workdir->old_file.mode;
			index_id = populate_oids ? git_java_objectid_init(env, &(index_to_workdir->old_file.id)) : NULL;
		}
		wt_path = index_to_workdir->new_file.path;
		wt_size = index_to_workdir->new_file.size;
		wt_flags = (jint) index_to_workdir->new_file.flags;
		wt_mode = index_to_workdir->new_file.mode;
		wt_id = populate_oids ? git_java_objectid_init(env, &(index_to_workdir->new_file.id)) : NULL;
	}

	jobject head_path_java = head_path != NULL ? (*env)->NewStringUTF(env, head_path) : NULL;
	jobject index_path_java;
	if (index_path == NULL) {
		index_path_java = NULL;
	}
	else if (head_path != NULL && strcmp(head_path, index_path) == 0) {
		index_path_java = head_path_java;
	}
	else {
		index_path_java = (*env)->NewStringUTF(env, index_path);
	}

	jobject wt_path_java;
	if (wt_path == NULL) {
		wt_path_java = NULL;
	}
	else if (index_path != NULL && strcmp(index_path, wt_path) == 0) {
		wt_path_java = index_path_java;
	}
	else {
		wt_path_java = (*env)->NewStringUTF(env, wt_path);
	}

	// We are using only one large status-object and we are populating oids only if requested,
	// because creating Java objects contributes a significant amount to the overall execution
	// time. For example, using MSVC compiler and testing against https://github.com/varigit/linux-2.6-imx
	// (~46K files), I'm seeing following average timings:
	// - 822 ms when creating no Java objects at all
	// - 875 ms with one big Status object without populating OIDs
	// - 1132 ms with one big Status object and populating OIDs (1 + 3 objects)
	// - 1266 ms with one Status object, 2 DiffDelta objects, 4 DiffFile objects and 4 ObjectId objects
	jobject status_java = jni_constructor_invoke(status_constructor, env, status_flags,
	                                             head_path_java, head_size, head_flags, head_mode, head_id,
	                                             index_path_java, index_size, index_flags, index_mode, index_id,
	                                             wt_path_java, wt_size, wt_flags, wt_mode, wt_id,
	                                             head_index_flags, head_index_similarity, head_index_nfiles,
	                                             index_wt_flags, index_wt_similarity, index_wt_nfiles);
	return status_java;
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_statusOptionsInit(
		JNIEnv *env,
		jclass class) {
	assert(env);
	assert(class);

	int error;
	git_status_options options;
	if ((error = git_status_init_options(&options, GIT_STATUS_OPTIONS_VERSION)) < 0) {
		git_java_exception_throw_giterr(env, error);
		return NULL;
	}

	char *enum_name;
	if (options.show == GIT_STATUS_SHOW_INDEX_ONLY) {
		enum_name = STATUS_SHOW_INDEX_ONLY;
	}
	else if (options.show == GIT_STATUS_SHOW_WORKDIR_ONLY) {
		enum_name = STATUS_SHOW_WORKDIR_ONLY;
	}
	else {
		enum_name = STATUS_SHOW_INDEX_AND_WORKDIR;
	}

	jclass status_options_class;
	jmethodID status_options_init;
	jobject status_show_options_class;
	jobject status_show_options;
	if (!jni_get_class(&status_options_class, "org/libgit2/jagged/status/StatusOptions", env) ||
	    !jni_get_method_id_by_class(&status_options_init, status_options_class, "<init>",
	                                "(Lorg/libgit2/jagged/status/StatusShowOptions;I[Ljava/lang/String;)V",
	                                env) ||
	    !jni_get_class(&status_show_options_class, "org/libgit2/jagged/status/StatusShowOptions", env) ||
	    !jni_get_static_object_field(&status_show_options, status_show_options_class, enum_name, "Lorg/libgit2/jagged/status/StatusShowOptions;", env))
		return NULL;

	int count = (int) options.pathspec.count;
	jobjectArray pathspecs_array;
	if (!jni_create_object_array(&pathspecs_array, "java/lang/String", count, env))
		return NULL;

	jsize i;
	for (i = 0; i < count; ++i) {
		(*env)->SetObjectArrayElement(env, pathspecs_array, i, (*env)->NewStringUTF(env, options.pathspec.strings[i]));
	}

	return (*env)->NewObject(env, status_options_class, status_options_init, status_show_options, options.flags, pathspecs_array);
}

JNIEXPORT jobject JNICALL
Java_org_libgit2_jagged_core_NativeMethods_statusListNew(
		JNIEnv *env,
		jclass class,
		jobject repo_java,
		jobject options_java,
		jboolean populate_oids) {
	git_repository *repo;
	int error;
	git_status_options options;
	git_status_list *status = NULL;
	jobjectArray status_array = NULL;

	assert(env);
	assert(class);
	assert(repo_java);

	if ((error = git_status_init_options(&options, GIT_STATUS_OPTIONS_VERSION)) < 0) {
		git_java_exception_throw_giterr(env, error);
		return NULL;
	}

	jobject status_show_options;
	jobject status_show_options_name;
	jint flags;
	jni_constructor oid_constructor;
	jni_constructor status_constructor;
	if (!jni_get_object_field(&status_show_options, options_java, "show", "Lorg/libgit2/jagged/status/StatusShowOptions;", env) ||
	    !jni_call_object_method(&status_show_options_name, status_show_options, "java/lang/Enum", "name", "()Ljava/lang/String;", env) ||
	    !jni_get_int_field(&flags, options_java, "flags", env) ||
	    !git_java_objectid_constructor(env, &oid_constructor) ||
	    !jni_constructor_init(&status_constructor, "org/libgit2/jagged/status/Status",
	                          "(I"
			                          "Ljava/lang/String;JIILorg/libgit2/jagged/ObjectId;"
			                          "Ljava/lang/String;JIILorg/libgit2/jagged/ObjectId;"
			                          "Ljava/lang/String;JIILorg/libgit2/jagged/ObjectId;"
			                          "III"
			                          "III)V", env))
		return NULL;

	const char *show_name = git_java_jstring_to_utf8(env, status_show_options_name);
	if (strcmp(show_name, STATUS_SHOW_INDEX_ONLY) == 0) {
		options.show = GIT_STATUS_SHOW_INDEX_ONLY;
	}
	else if (strcmp(show_name, STATUS_SHOW_WORKDIR_ONLY) == 0) {
		options.show = GIT_STATUS_SHOW_WORKDIR_ONLY;
	}
	else if (strcmp(show_name, STATUS_SHOW_INDEX_AND_WORKDIR) == 0) {
		options.show = GIT_STATUS_SHOW_INDEX_AND_WORKDIR;
	}
	else {
		goto done;
	}

	options.flags = (unsigned int) flags;

	jobject pathSpecs_field;
	if (!jni_get_object_field(&pathSpecs_field, options_java, "pathSpecs", "[Ljava/lang/String;", env))
		goto done;

	if (pathSpecs_field != NULL) {
		jsize count = (*env)->GetArrayLength(env, pathSpecs_field);
		options.pathspec.count = (size_t) count;
		options.pathspec.strings = malloc(sizeof(char *) * count);

		int i;
		for (i = 0; i < count; i++) {
			jobject pathspec_java = (*env)->GetObjectArrayElement(env, pathSpecs_field, i);
			const char *pathspec = git_java_jstring_to_utf8(env, pathspec_java);
			// TODO: what's wrong with this line so we have to disable warnings (see #pragma)?
#ifdef _WIN32
			options.pathspec.strings[i] = _strdup(pathspec);
#else
			options.pathspec.strings[i] = strdup(pathspec);
#endif
			git_java_utf8_free(env, pathspec_java, pathspec);
		}
	}

	repo = git_java_handle_get(env, repo_java);

	if ((error = git_status_list_new(&status, repo, &options)) < 0) {
		git_java_exception_throw_giterr(env, error);
		goto done;
	}

	size_t i, count = git_status_list_entrycount(status);

	status_array = (*env)->NewObjectArray(env, (jsize) count, status_constructor.class, NULL);
	if (status_array == NULL)
		goto done;


	for (i = 0; i < count; ++i) {
		const git_status_entry *entry = git_status_byindex(status, i);
		jobject status_java = create_status_java(entry, &status_constructor, (int)populate_oids, env);
		(*env)->SetObjectArrayElement(env, status_array, (jsize) i, status_java);
	}

	done:
	if (status != NULL)
		git_status_list_free(status);
	if (show_name != NULL)
		git_java_utf8_free(env, status_show_options_name, show_name);
	git_strarray_free(&options.pathspec);
	return status_array;
}

