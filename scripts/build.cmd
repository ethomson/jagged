@echo off

git submodule update --init --recursive
make install
mvn install
