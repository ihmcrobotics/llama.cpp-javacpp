FROM nvidia/cuda:12.8.0-devel-ubuntu22.04

RUN apt-get --quiet 2 --yes update  \
 && apt-get --quiet 2 --yes install \
    git \
    wget \
    curl \
    cmake \
    build-essential \
    > /dev/null \
 && rm -rf /var/lib/apt/lists/*
