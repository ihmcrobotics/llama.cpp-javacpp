FROM nvidia/cuda:12.8.0-devel-ubuntu22.04

RUN apt-get --quiet 2 --yes update  \
 && apt-get --quiet 2 --yes install \
    git \
    wget \
    curl \
    unzip \
    cmake \
    build-essential \
    openjdk-17-jdk \
    > /dev/null \
 && rm -rf /var/lib/apt/lists/*
