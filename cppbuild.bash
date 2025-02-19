#!/bin/bash
# This build script is designed to work on Linux and Windows. For Windows, run from a bash shell launched with launchBashWindows.bat
pushd .
mkdir cppbuild
cd cppbuild

LLAMACPP_VERSION=b4743
if [ ! -f "llamacpp.tar.gz" ]; then
  curl -o llamacpp.tar.gz https://codeload.github.com/ggml-org/llama.cpp/tar.gz/refs/tags/$LLAMACPP_VERSION
fi

tar -xvf llamacpp.tar.gz

cd llama.cpp-$LLAMACPP_VERSION

make
