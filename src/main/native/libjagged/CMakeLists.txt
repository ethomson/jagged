# CMake build script for the jagged project

PROJECT(libjagged)
CMAKE_MINIMUM_REQUIRED(VERSION 2.6)

CMAKE_POLICY(SET CMP0015 OLD)

FIND_PACKAGE(JNI)

IF (NOT JNI_FOUND)
	message(FATAL_ERROR "Could not find JNI")
ENDIF()

IF (MSVC)
	OPTION(USE_SDL "Turn on SDL recommended checks" ON)
ENDIF()

SET(BUILD_SHARED_LIBS ON)

# Platform specific compilation flags
IF (MSVC)
	STRING(REPLACE "/Zm1000" " " CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")

	SET(CMAKE_C_FLAGS "/nologo /TC /DUNICODE /D_UNICODE /W3 /WX ${CMAKE_C_FLAGS}")

	# SDL requirements:
	# /GS - buffers security check
	# /D"_SDL_BANNER_RECOMMENDED=1" - banned functions
	# /ANALYZE - code analysis (PREfast)
	IF (USE_SDL)
		SET(CMAKE_C_FLAGS "/GS /D\"_SDL_BANNED_RECOMMENDED=1\" /analyze ${CMAKE_C_FLAGS} /D\"SECURITY_WIN32\"")
	ENDIF()

	# /Zi - Create debugging information
	# /Od - Disable optimization
	# /D_DEBUG - #define _DEBUG
	# /MTd - Statically link the multithreaded debug version of the CRT
	# /MDd - Dynamically link the multithreaded debug version of the CRT
	# /RTC1 - Run time checks
	SET(CMAKE_C_FLAGS_DEBUG "/Zi /Od /D_DEBUG /RTC1 ${CRT_FLAG_DEBUG}")

	# /DNDEBUG - Disables asserts
	# /MT - Statically link the multithreaded release version of the CRT
	# /MD - Dynamically link the multithreaded release version of the CRT
	# /O2 - Optimize for speed
	# /Oy - Enable frame pointer omission (FPO) (otherwise CMake will automatically turn it off)
	# /GL - Link time code generation (whole program optimization)
	# /Gy - Function-level linking
	SET(CMAKE_C_FLAGS_RELEASE "/DNDEBUG /O2 /Oy /GL /Gy ${CRT_FLAG_RELEASE}")

	# /Oy- - Disable frame pointer omission (FPO)
	SET(CMAKE_C_FLAGS_RELWITHDEBINFO "/DNDEBUG /Zi /O2 /Oy- /GL /Gy ${CRT_FLAG_RELEASE}")

	# /O1 - Optimize for size
	SET(CMAKE_C_FLAGS_MINSIZEREL "/DNDEBUG /O1 /Oy /GL /Gy ${CRT_FLAG_RELEASE}")

	# /DYNAMICBASE - Address space load randomization (ASLR)
	# /NXCOMPAT - Data execution prevention (DEP)
	# /LARGEADDRESSAWARE - >2GB user address space on x86
	# /VERSION - Embed version information in PE header
	SET(CMAKE_SHARED_LINKER_FLAGS "/DYNAMICBASE /NXCOMPAT /LARGEADDRESSAWARE")

	IF (CMAKE_SIZEOF_VOID_P EQUAL 4)
		# /SAFESEH - Safe exception handlers
		SET(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} /SAFESEH")
	ENDIF()

	# /DEBUG - Create a PDB
	# /LTCG - Link time code generation (whole program optimization)
	# /OPT:REF /OPT:ICF - Fold out duplicate code at link step
	# /INCREMENTAL:NO - Required to use /LTCG
	# /DEBUGTYPE:cv,fixup - Additional data embedded in the PDB (requires /INCREMENTAL:NO, so not on for Debug)
	SET(CMAKE_SHARED_LINKER_FLAGS_DEBUG "/DEBUG")
	SET(CMAKE_SHARED_LINKER_FLAGS_RELEASE "/RELEASE /LTCG /OPT:REF /OPT:ICF /INCREMENTAL:NO")
	SET(CMAKE_SHARED_LINKER_FLAGS_RELWITHDEBINFO "/DEBUG /RELEASE /LTCG /OPT:REF /OPT:ICF /INCREMENTAL:NO /DEBUGTYPE:cv,fixup")
	SET(CMAKE_SHARED_LINKER_FLAGS_MINSIZEREL "/RELEASE /LTCG /OPT:REF /OPT:ICF /INCREMENTAL:NO")

ELSE ()
	SET(CMAKE_C_FLAGS "-D_GNU_SOURCE -Wall -Wextra -Wno-missing-field-initializers -Wstrict-aliasing=2 -Wstrict-prototypes ${CMAKE_C_FLAGS}")

	IF (WIN32 AND NOT CYGWIN)
		SET(CMAKE_C_FLAGS_DEBUG "-D_DEBUG")
	ENDIF ()
ENDIF()

FILE(GLOB SRC_H *.h)
FILE(GLOB SRC_C *.c)

INCLUDE_DIRECTORIES(${JNI_INCLUDE_DIRS})

IF (INCLUDE_LIBGIT2)
	INCLUDE_DIRECTORIES("${INCLUDE_LIBGIT2}")
ENDIF()

IF (LINK_LIBGIT2)
	LINK_DIRECTORIES("${LINK_LIBGIT2}")
ENDIF()

ADD_LIBRARY(jagged ${SRC_C})
TARGET_LINK_LIBRARIES(jagged git2)

