#!/bin/bash
set -e -o xtrace

docker build -f Dockerfile-linux-cuda-x86_64 --build-arg USER_UID=$(id -u) --build-arg USER_GID=$(id -g) . -t llama.cpp-javacpp

docker run \
       --rm \
       --user $(id -u):$(id -g) \
       --volume $(pwd):/tmp/llama.cpp-javacpp \
       --workdir /tmp/llama.cpp-javacpp \
       llama.cpp-javacpp bash cppbuild-cuda.bash
