#
# Determine operating system / architecture name in OSGI format
#

# Update OS names to canonical format
OS=$(shell sh -c 'uname -s 2>/dev/null || echo not')

ifeq ($(OS),Darwin)
	OS=macosx
endif
ifeq ($(OS),Linux)
	OS=linux
endif

# Update processor names to canonical format
ARCH=$(shell uname -m)

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

