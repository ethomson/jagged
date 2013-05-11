CC=gcc

#
# Update OS / arch names to the canonical format used by OSGI
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

#
# Set machine-specific configuration
#

# default is to put library in arch-specific directory
NATIVE_DIR_NAME=$(OS)/$(ARCH)

CFLAGS=-Wall -Wextra -g

ifeq ($(OS),macosx)
	JAVA_INCLUDE=/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers
	CFLAGS+=-arch i386 -arch x86_64
	LDFLAGS+=-dylib -bundle -framework JavaVM -mmacosx-version-min=10.5
	
	ifeq ($(ARCH),ppc)
		CFLAGS+=-arch ppc
	endif
	
	ifeq ($(ARCH),ppc64)
		CFLAGS+=-arch ppc -arch ppc64
	endif
	
	LIBJAGGED=libjagged.dylib
	LIBGIT2=libgit2.dylib

	NATIVE_DIR_NAME=$(OS)
endif

ifeq ($(OS),linux)
	LIBJAGGED=libjagged.so
	LIBGIT2=libgit2.so
endif

CFLAGS+=-I$(JAVA_INCLUDE) -I../libgit2/include
LDFLAGS+=-L../../../../native/$(NATIVE_DIR_NAME) -lgit2

OBJS=error.o reference.o repository.o

all: $(LIBJAGGED)

$(LIBJAGGED): $(LIBGIT2_LIB) $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS) $(LDFLAGS)

clean:
	@rm -f $(LIBJAGGED) $(OBJS)

