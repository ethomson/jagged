#ifndef GIT_JAVA_COMMON_H
#define GIT_JAVA_COMMON_H

#include <jni.h>

#include "util.h"

#define GIT_JAVA_CLASS_COMMON_VERSION "org/libgit2/jagged/Common$Version"

#ifndef git__malloc
#define git__malloc(Z) malloc(Z)
#endif
#ifndef git__free
#define git__free(P) free(P)
#endif

#endif /* GIT_JAVA_COMMON_H */
