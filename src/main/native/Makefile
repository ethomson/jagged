
include libjagged/arch.mk

#
# Determine maven targets directory
#

NATIVE_INSTALL=../../../native

NATIVE_TARGET=../../../target/native
LIBGIT2_TARGET=$(NATIVE_TARGET)/libgit2
LIBJAGGED_TARGET=$(NATIVE_TARGET)/libjagged

LIBGIT2_SRC+=${CURDIR}/libgit2

all: build

build: build_libjagged

install: install_libgit2 install_libjagged

clean: clean_libgit2 clean_libjagged

build_libgit2:
	@mkdir -p $(LIBGIT2_TARGET)
	(cd $(LIBGIT2_TARGET) && \
	 cmake $(LIBGIT2_SRC) -DSONAME=OFF \
	 -DCMAKE_C_FLAGS="$(CMAKE_C_FLAGS) $(ARCH_CFLAGS)")
	cmake --build $(LIBGIT2_TARGET) --config RelWithDebInfo

$(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION): build_libgit2

install_libgit2: $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION)
	cp $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION) $(NATIVE_INSTALL)/$(ARCH_PATH)

clean_libgit2:
	cmake --clean $(LIBGIT2_TARGET)

build_libjagged: build_libgit2
	@mkdir -p $(LIBJAGGED_TARGET)
	(cd libjagged && \
	 make OUTPUT=../$(LIBJAGGED_TARGET) LIBGIT2_DIR=../$(LIBGIT2_TARGET) \
	 TARGET_DIR=$(LIBJAGGED_TARGET))

$(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION): build_libjagged

install_libjagged: $(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION)
	cp $(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION) $(NATIVE_INSTALL)/$(ARCH_PATH)

clean_libjagged:
	(cd libjagged && make clean)

