
include scripts/arch.mk

#
# Determine maven targets directory
#

NATIVE_INSTALL=${CURDIR}/native
NATIVE_TARGET=${CURDIR}/target/native

LIBGIT2_TARGET=${CURDIR}/$(NATIVE_TARGET)/libgit2
LIBJAGGED_TARGET=${CURDIR}/$(NATIVE_TARGET)/libjagged
LIBJAGGED_TEST_TARGET=${CURDIR}/$(NATIVE_TARGET)/libjagged_test

LIBGIT2_SRC=${CURDIR}/src/main/native/libgit2
LIBJAGGED_SRC=${CURDIR}/src/main/native/libjagged
LIBJAGGED_TEST_SRC=${CURDIR}/src/test/native/libjagged_test

ROOT_SRC+=${CURDIR}/../../..

CMAKE_CONFIG=RelWithDebInfo

all: build

build: build_libgit2 build_libjagged build_libjagged_test

install: install_libgit2 install_libjagged install_libjagged_test

clean: clean_libgit2 clean_libjagged clean_libjagged_test

build_libgit2: $(LIBGIT2_SRC)/src
	@mkdir -p $(LIBGIT2_TARGET)
	(cd $(LIBGIT2_TARGET) && \
	 cmake $(LIBGIT2_SRC) -DSONAME=OFF -DTHREADSAFE=ON -DBUILD_CLAR=OFF \
	 -DCMAKE_C_FLAGS="$(CMAKE_C_FLAGS) $(ARCH_CFLAGS)")
	cmake --build $(LIBGIT2_TARGET) --config $(CMAKE_CONFIG)

$(LIBGIT2_SRC)/src:
	(cd $(ROOT_SRC) && git submodule init && git submodule update)

$(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION): build_libgit2

install_libgit2: $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION)
	if [ ! -f $(NATIVE_INSTALL)/$(ARCH_PATH) ]; then \
		mkdir -p $(NATIVE_INSTALL)/$(ARCH_PATH) ; \
	fi
	cp $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION) $(NATIVE_INSTALL)/$(ARCH_PATH)

clean_libgit2:
	if [ -d $(LIBGIT2_TARGET) ]; then \
		cmake --build $(LIBGIT2_TARGET) --target clean ; \
	fi


build_libjagged: $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION)
	@mkdir -p $(LIBJAGGED_TARGET)
	(cd $(LIBJAGGED_TARGET) && \
	 cmake $(LIBJAGGED_SRC) \
	 -DINCLUDE_LIBGIT2="$(LIBGIT2_SRC)/include" \
	 -DLINK_LIBGIT2="$(LIBGIT2_TARGET)" \
	 -DCMAKE_C_FLAGS="$(CMAKE_C_FLAGS) $(ARCH_CFLAGS)")
	cmake --build $(LIBJAGGED_TARGET) --config $(CMAKE_CONFIG)

$(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION): build_libjagged

install_libjagged: $(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION)
	if [ ! -f $(NATIVE_INSTALL)/$(ARCH_PATH) ]; then \
		mkdir -p $(NATIVE_INSTALL)/$(ARCH_PATH) ; \
	fi
	cp $(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION) $(NATIVE_INSTALL)/$(ARCH_PATH)

clean_libjagged:
	if [ -d $(LIBJAGGED_TARGET) ]; then \
		cmake --build $(LIBJAGGED_TARGET) --target clean ; \
	fi


build_libjagged_test: $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION) $(LIBJAGGED_TARGET)/libjagged.$(SO_EXTENSION)
	@mkdir -p $(LIBJAGGED_TEST_TARGET)
	(cd $(LIBJAGGED_TEST_TARGET) && \
	 cmake $(LIBJAGGED_TEST_SRC) \
	 -DINCLUDE_LIBGIT2="$(LIBGIT2_SRC)/include" \
	 -DLINK_LIBGIT2="$(LIBGIT2_TARGET)" \
	 -DINCLUDE_LIBJAGGED="$(LIBJAGGED_SRC)" \
	 -DLINK_LIBJAGGED="$(LIBJAGGED_TARGET)" \
	 -DCMAKE_C_FLAGS="$(CMAKE_C_FLAGS) $(ARCH_CFLAGS)")
	cmake --build $(LIBJAGGED_TEST_TARGET) --config $(CMAKE_CONFIG)

$(LIBJAGGED_TEST_TARGET)/libjagged_test.$(SO_EXTENSION): build_libjagged_test

install_libjagged_test: $(LIBJAGGED_TEST_TARGET)/libjagged_test.$(SO_EXTENSION)
	if [ ! -f $(NATIVE_INSTALL)/$(ARCH_PATH) ]; then \
		mkdir -p $(NATIVE_INSTALL)/$(ARCH_PATH) ; \
	fi
	cp $(LIBJAGGED_TEST_TARGET)/libjagged_test.$(SO_EXTENSION) $(NATIVE_INSTALL)/$(ARCH_PATH)

clean_libjagged_test:
	if [ -d $(LIBJAGGED_TEST_TARGET) ]; then \
		cmake --build $(LIBJAGGED_TEST_TARGET) --target clean ; \
	fi

