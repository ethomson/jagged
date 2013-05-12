#
# Determine operating system / architecture name in OSGI format
#

# Update OS names to canonical format
OS=$(shell uname -s)

ifeq ($(OS),Darwin)
	OS=macosx
endif
ifeq ($(OS),Linux)
	OS=linux
endif

# Update processor names to canonical format
ARCH=$(shell uname -p)

ifeq ($(ARCH),i386)
	ARCH=x86
endif
ifeq ($(ARCH),amd64)
	ARCH=x86_64
endif

#  What to call shared libraries
SO_EXTENSION=so

ifeq ($(OS),macosx)
	SO_EXTENSION=dylib
endif

ifeq ($(OS),macosx)
	ARCH_PATH=$(OS)
else
	ARCH_PATH=$(OS)/$(ARCH)
endif

# Create quad-fat binaries on Mac OS
ifeq ($(OS),macosx)
	ARCH_CFLAGS=-arch i386 -arch x86_64

	ifeq ($(ARCH),ppc)
		ARCH_CFLAGS+=-arch ppc
	endif

	ifeq ($(ARCH),ppc64)
		ARCH_CFLAGS+=-arch ppc -arch ppc64
	endif
endif

