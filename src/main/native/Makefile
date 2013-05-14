
include libjagged/arch.mk

#
# Determine maven targets directory
#

NATIVE_INSTALL=../../../native

NATIVE_TARGET=../../../target/native
LIBGIT2_TARGET=$(NATIVE_TARGET)/libgit2
LIBJAGGED_TARGET=$(NATIVE_TARGET)/libjagged

LIBGIT2_SRC=${CURDIR}/libgit2
LIBJAGGED_SRC=${CURDIR}/libjagged

CMAKE_CONFIG=RelWithDebInfo

all: build

build: build_libjagged

install: install_libgit2 install_libjagged

clean: clean_libgit2 clean_libjagged

build_libgit2:
	@mkdir -p $(LIBGIT2_TARGET)
	(cd $(LIBGIT2_TARGET) && \
	 cmake $(LIBGIT2_SRC) -DSONAME=OFF -DTHREADSAFE=ON -DBUILD_CLAR=OFF \
	 -DCMAKE_C_FLAGS="$(CMAKE_C_FLAGS) $(ARCH_CFLAGS)")
	cmake --build $(LIBGIT2_TARGET) --config $(CMAKE_CONFIG)

$(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION): build_libgit2

install_libgit2: $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION)
	if [ ! -f $(NATIVE_INSTALL)/$(ARCH_PATH) ]; then \
		mkdir -p $(NATIVE_INSTALL)/$(ARCH_PATH) ; \
	fi
	cp $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION) $(NATIVE_INSTALL)/$(ARCH_PATH)

clean_libgit2:
	cmake --build $(LIBGIT2_TARGET) --target clean


build_libjagged: $(LIBGIT2_TARGET)/libgit2.$(SO_EXTENSION)
	@mkdir -p $(LIBJAGGED_TARGET)
	(cd $(LIBJAGGED_TARGET) && \
	 cmake $(LIBJAGGED_SRC) \
	 -DINCLUDE_LIBGIT2="$(LIBGIT2_SRC)\include" \
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
	cmake --build $(LIBJAGGED_TARGET) --target clean

