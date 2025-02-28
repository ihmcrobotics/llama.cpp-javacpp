#!/bin/bash
set -e -o xtrace

docker build  -f Dockerfile-linux-cuda-x86_64 --build-arg USER_UID=$(id -u) --build-arg USER_GID=$(id -g) . -t llama.cpp-javacpp

xhost +local:docker

mkdir -p $HOME/.cache/JetBrainsDocker
mkdir -p $HOME/.config/JetBrainsDocker

docker run \
       --tty \
       --interactive \
       --rm \
       --network host \
       --dns=1.1.1.1 \
       --user $(id -u):$(id -g) \
       --volume $(pwd):/tmp/llama.cpp-javacpp \
       --workdir /tmp/llama.cpp-javacpp \
       --privileged \
       --gpus all \
       --device /dev/dri:/dev/dri \
       --env DISPLAY \
       --volume /tmp/.X11-unix:/tmp/.X11-unix \
       --volume /usr/share/fonts:/usr/share/fonts \
       --volume $HOME/.cache/JetBrainsDocker:/home/robotlab/.cache/JetBrains \
       --volume $HOME/.config/JetBrainsDocker:/home/robotlab/.config/JetBrains \
       llama.cpp-javacpp bash