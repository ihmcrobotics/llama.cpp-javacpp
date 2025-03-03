#!/bin/bash
# This build script is designed to work on Linux and Windows. For Windows, run from a bash shell launched with launchBashWindows.bat

# Clean
rm -rf cppbuild/us
find src/main/java/us/ihmc/llamacpp -maxdepth 1 -type f -not \( -name "LlamaCPPConfig.java" \) -delete

pushd .
mkdir -p cppbuild
cd cppbuild

LLAMACPP_VERSION=b4743

# Non-shallow clone required for llama.cpp build
if [ ! -d llama.cpp-$LLAMACPP_VERSION ]; then
  git -c advice.detachedHead=false clone -b $LLAMACPP_VERSION https://github.com/ggml-org/llama.cpp.git llama.cpp-$LLAMACPP_VERSION
fi

INSTALL_DIR=$(pwd)

cd llama.cpp-$LLAMACPP_VERSION

if [ "$(uname)" == "Linux" ]; then
  cmake -B build -DGGML_CUDA=ON \
    -DCMAKE_CUDA_COMPILER=/usr/local/cuda/bin/nvcc \
    -DCMAKE_INSTALL_INCLUDEDIR=$INSTALL_DIR/include \
    -DCMAKE_INSTALL_LIBDIR=$INSTALL_DIR/lib \
    -DCMAKE_INSTALL_BINDIR=$INSTALL_DIR/bin
else # Windows
  cmake -B build -DGGML_CUDA=ON \
    -DCMAKE_INSTALL_INCLUDEDIR=$INSTALL_DIR/include \
    -DCMAKE_INSTALL_LIBDIR=$INSTALL_DIR/lib \
    -DCMAKE_INSTALL_BINDIR=$INSTALL_DIR/bin
fi

cmake --build build --config Release -j 8 --target install

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
java -cp "javacpp.jar"$CP_SEPARATOR"cuda-$JAVACPP_CUDA_VERSION.jar" org.bytedeco.javacpp.tools.Builder us/ihmc/llamacpp/*.java us/ihmc/llamacpp/global/*.java -d javainstall

##### Copy shared libs to resources ####
# Linux
mkdir -p ../src/main/resources/llamacpp/native/linux-x86_64
if [ -f "lib/libggml.so" ]; then
  cp lib/libggml.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
if [ -f "lib/libggml-base.so" ]; then
  cp lib/libggml-base.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
if [ -f "lib/libggml-cpu.so" ]; then
  cp lib/libggml-cpu.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
if [ -f "lib/libggml-cuda.so" ]; then
  cp lib/libggml-cuda.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
if [ -f "lib/libllama.so" ]; then
  cp lib/libllama.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
if [ -f "lib/libllama_shared.so" ]; then
  cp lib/libllama_shared.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
if [ -f "javainstall/libjnillamacpp.so" ]; then
  cp javainstall/libjnillamacpp.so ../src/main/resources/llamacpp/native/linux-x86_64
fi
# Windows
mkdir -p ../src/main/resources/llamacpp/native/windows-x86_64
if [ -f "bin/ggml.dll" ]; then
  cp bin/ggml.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
if [ -f "bin/ggml-base.dll" ]; then
  cp bin/ggml-base.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
if [ -f "bin/ggml-cpu.dll" ]; then
  cp bin/ggml-cpu.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
if [ -f "bin/ggml-cuda.dll" ]; then
  cp bin/ggml-cuda.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
if [ -f "bin/llama.dll" ]; then
  cp bin/llama.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
if [ -f "bin/llama_shared.dll" ]; then
  cp bin/llama_shared.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
if [ -f "javainstall/jnillamacpp.dll" ]; then
  cp javainstall/jnillamacpp.dll ../src/main/resources/llamacpp/native/windows-x86_64
fi
