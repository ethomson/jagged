#!/bin/sh

if [ "$TMPDIR" = "" ]; then
	export TMPDIR=$HOME/_temp
	mkdir $TMPDIR
fi

git submodule update --init --recursive
(cd src/main/native && make install)
mvn install
