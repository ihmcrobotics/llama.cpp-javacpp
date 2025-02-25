#!/bin/bash
set -e -o xtrace

# Clean
rm -rf cppbuild/us
find src/main/java/us/ihmc/llamacpp -maxdepth 1 -type f -not \( -name "LlamaCPPConfig.java" \) -delete

# This build script is designed to work on Linux and Windows. For Windows, run from a bash shell launched with launchBashWindows.bat
pushd .
mkdir -p cppbuild
cd cppbuild

LLAMACPP_VERSION=b4743

# Non-shallow clone required for llama.cpp build
if [ ! -d llama.cpp-$LLAMACPP_VERSION ]; then
  git -c advice.detachedHead=false clone -b $LLAMACPP_VERSION https://github.com/ggml-org/llama.cpp.git llama.cpp-$LLAMACPP_VERSION
fi

# Ubuntu 20.04 ships with cmake 3.16.3, where llama.cpp ggml-cuda requires at least 3.18
# This patch file currently not working yet. nvcc error during build
# cp ../patches/CMakeLists.txt.gguf-cuda.patch llama.cpp-$LLAMACPP_VERSION/CMakeLists.txt.gguf-cuda.patch

INSTALL_DIR=$(pwd)

cd llama.cpp-$LLAMACPP_VERSION

# patch ggml/src/ggml-cuda/CMakeLists.txt CMakeLists.txt.gguf-cuda.patch

cmake -B build -DGGML_CUDA=ON \
               -DCMAKE_CUDA_COMPILER=/usr/local/cuda/bin/nvcc \
               -DCMAKE_INSTALL_INCLUDEDIR=$INSTALL_DIR/include \
               -DCMAKE_INSTALL_LIBDIR=$INSTALL_DIR/lib \
               -DCMAKE_INSTALL_BINDIR=$INSTALL_DIR/bin
cmake --build build --config Release -j $(nproc) --target install

popd
### Java generation ####
cd cppbuild

cp -r ../src/main/java/* .

JAVACPP_VERSION=1.5.11
JAVACPP_CUDA_VERSION=12.6-9.5-1.5.11
if [ ! -f javacpp.jar ]; then
  curl -L https://github.com/bytedeco/javacpp/releases/download/$JAVACPP_VERSION/javacpp-platform-$JAVACPP_VERSION-bin.zip -o javacpp-platform-$JAVACPP_VERSION-bin.zip
  unzip -j javacpp-platform-$JAVACPP_VERSION-bin.zip
fi
if [ ! -f cuda-$JAVACPP_CUDA_VERSION.jar ]; then
    curl -O https://repo.maven.apache.org/maven2/org/bytedeco/cuda/$JAVACPP_CUDA_VERSION/cuda-$JAVACPP_CUDA_VERSION.jar
fi

CP_SEPARATOR=":"
UNAME=$( command -v uname)
case $( "${UNAME}" | tr '[:upper:]' '[:lower:]') in
  msys*|cygwin*|mingw*|nt|win*)
    CP_SEPARATOR=";"
    ;;
  *)
    CP_SEPARATOR=":"
    ;;
esac

java -cp "javacpp.jar"$CP_SEPARATOR"cuda-$JAVACPP_CUDA_VERSION.jar" org.bytedeco.javacpp.tools.Builder us/ihmc/llamacpp/LlamaCPPConfig.java

cp us/ihmc/llamacpp/*.java ../src/main/java/us/ihmc/llamacpp
cp us/ihmc/llamacpp/global/*.java ../src/main/java/us/ihmc/llamacpp/global/

#### JNI compilation ####
java -cp "javacpp.jar"$CP_SEPARATOR"cuda-$JAVACPP_CUDA_VERSION.jar" \
     org.bytedeco.javacpp.tools.Builder us/ihmc/llamacpp/*.java us/ihmc/llamacpp/global/*.java -d javainstall -nocompile

prepend_missing() {
    local prefix="$1"
    local upper_prefix="${prefix^^}"
    local word="$2"
    local upper_word="${word^^}"  # Convert to uppercase
    sed -i "s/$prefix $word/__${upper_prefix}_${upper_word}__/g; s/$word/$prefix $word/g; s/__${upper_prefix}_${upper_word}__/$prefix $word/g" javainstall/jnillamacpp.cpp
}

prepend_missing struct ggml_backend_graph_copy
prepend_missing struct ggml_backend_buffer_type
prepend_missing struct ggml_backend_buffer
prepend_missing struct ggml_backend_event
prepend_missing struct ggml_backend
prepend_missing struct ggml_backend_reg
prepend_missing struct ggml_backend_device

sed -i "s/struct ggml_backend_dev_type/enum ggml_backend_dev_type/g" javainstall/jnillamacpp.cpp
sed -i "s/struct ggml_backend_buffer_usage/enum ggml_backend_buffer_usage/g" javainstall/jnillamacpp.cpp

g++ -I/tmp/llama.cpp-javacpp/cppbuild/include \
-I/usr/local/cuda-12.8/targets/x86_64-linux/include \
-I/tmp/llama.cpp-javacpp/cppbuild/lib \
-I/usr/lib/jvm/java-17-openjdk-amd64/include \
-I/usr/lib/jvm/java-17-openjdk-amd64/include/linux \
/tmp/llama.cpp-javacpp/cppbuild/javainstall/jnillamacpp.cpp \
/tmp/llama.cpp-javacpp/cppbuild/javainstall/jnijavacpp.cpp \
-march=x86-64 -m64 -O3 -s -Wl,-rpath,$ORIGIN/ -Wl,-z,noexecstack -Wl,-Bsymbolic -Wall -fPIC -pthread -shared -o libjnillamacpp.so \
-L/tmp/llama.cpp-javacpp/cppbuild/include -Wl,-rpath,/tmp/llama.cpp-javacpp/cppbuild/include \
-L/usr/local/cuda-12.8/targets/x86_64-linux/include -Wl,-rpath,/usr/local/cuda-12.8/targets/x86_64-linux/include \
-L/tmp/llama.cpp-javacpp/cppbuild/lib -Wl,-rpath,/tmp/llama.cpp-javacpp/cppbuild/lib -lggml-cuda -lggml-cpu -lggml-base -lggml -lllama

##### Copy shared libs to resources ####
# Linux
mkdir -p ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
if [ -f "lib/libggml.so" ]; then
  cp lib/libggml.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
if [ -f "lib/libggml-base.so" ]; then
  cp lib/libggml-base.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
if [ -f "lib/libggml-cpu.so" ]; then
  cp lib/libggml-cpu.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
if [ -f "lib/libggml-cuda.so" ]; then
  cp lib/libggml-cuda.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
if [ -f "lib/libllama.so" ]; then
  cp lib/libllama.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
if [ -f "lib/libllama_shared.so" ]; then
  cp lib/libllama_shared.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
if [ -f "javainstall/libjnillamacpp.so" ]; then
  cp javainstall/libjnillamacpp.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
fi
