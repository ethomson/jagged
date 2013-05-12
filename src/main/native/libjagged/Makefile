CC=gcc

include arch.mk

#
# Set machine-specific configuration
#

CFLAGS=-Wall -Wextra -g $(ARCH_CFLAGS)

ifeq ($(LIBGIT2_DIR),)
	LIBGIT2=libgit2.a
else
	LIBGIT2=$(LIBGIT2_DIR)/libgit2.a
endif

ifeq ($(OS),macosx)
	JAVA_INCLUDE=/System/Library/Frameworks/JavaVM.framework/Versions/Current/Headers
	LDFLAGS+=-dylib -bundle -framework JavaVM -mmacosx-version-min=10.5
endif

ifeq ($(LIBGIT2_DIR),)
	LIBGIT2_DIR=.
endif

ifeq ($(OUTPUT),)
	OUTPUT=.
endif

LIBJAGGED=libjagged.$(SO_EXTENSION)

CFLAGS+=-I$(JAVA_INCLUDE) -I../libgit2/include
LDFLAGS+=-L$(LIBGIT2_DIR) -lgit2 -lssl

OBJS=error.o reference.o repository.o

all: $(OUTPUT)/$(LIBJAGGED)

$(OUTPUT)/$(LIBJAGGED): $(LIBGIT2_LIB) $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS) $(LDFLAGS)

clean:
	@rm -f $(LIBJAGGED) $(OBJS)

