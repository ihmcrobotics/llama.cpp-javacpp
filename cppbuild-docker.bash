#!/bin/bash
set -e -o xtrace

docker build . -t llama.cpp-javacpp

docker run \
       --rm \
       --volume $(pwd):/tmp/llama.cpp-javacpp \
       --workdir /tmp/llama.cpp-javacpp \
       llama.cpp-javacpp bash cppbuild.bash
