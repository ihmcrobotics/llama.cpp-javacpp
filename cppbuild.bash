#!/bin/bash
# This build script is designed to work on Linux and Windows. For Windows, run from a bash shell launched with launchBashWindows.bat
pushd .
mkdir cppbuild
cd cppbuild

LLAMACPP_VERSION=b4743

# Non-shallow clone required for llama.cpp build
git -c advice.detachedHead=false clone -b $LLAMACPP_VERSION https://github.com/ggml-org/llama.cpp.git llama.cpp-$LLAMACPP_VERSION

# Ubuntu 20.04 ships with cmake 3.16.3, where llama.cpp ggml-cuda requires at least 3.18
# This patch file currently not working yet. nvcc error during build
# cp ../patches/CMakeLists.txt.gguf-cuda.patch llama.cpp-$LLAMACPP_VERSION/CMakeLists.txt.gguf-cuda.patch

cd llama.cpp-$LLAMACPP_VERSION

# patch ggml/src/ggml-cuda/CMakeLists.txt CMakeLists.txt.gguf-cuda.patch

cmake -B build -DCMAKE_LIBRARY_PATH=/usr/local/cuda/lib64/stubs -DGGML_CUDA=ON
cmake --build build --config Release
