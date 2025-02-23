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

# Setup a robotlab user as the development user, to avoid using root.
# Use the user and group ID of the builder so access is maintained.
ARG USER_UID
ARG USER_GID
ENV USER_UID=$USER_UID
ENV USER_GID=$USER_GID
RUN addgroup --gid $USER_GID robotlab \
 && adduser --uid $USER_UID --home /home/robotlab --gecos "Rosie Robot,1117,1234567,2345678" --ingroup robotlab --disabled-password robotlab \
 && chown -R robotlab /home/robotlab \
 && adduser robotlab sudo \
 && echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers
# Make sure the .config folder exists and is owned by the robotlab user.
# This is useful for later installed apps.
RUN mkdir -p /home/robotlab/.config
RUN chown -R robotlab:robotlab /home/robotlab/.config

WORKDIR /tmp

RUN curl -sL https://download.jetbrains.com/cpp/CLion-2024.3.3.tar.gz -o clion.tar.gz \
 && tar -xzf clion.tar.gz \
 && mv clion-2024.3.3/ /opt/clion \
 && ln -s /opt/clion/bin/clion /usr/local/bin/clion
RUN mkdir -p /home/robotlab/.cache
RUN chown -R robotlab:robotlab /home/robotlab/.cache

USER robotlab
