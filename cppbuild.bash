#!/bin/bash
set -e -o xtrace

# Clean
rm -rf cppbuild/us
find src -type f -not \( -name "LlamaCPPConfig.java" -o -name "llamacpp.java" -o -name "LlamaCPPNativeLibrary.java" \) -delete

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
               -DCMAKE_INSTALL_INCLUDEDIR=$INSTALL_DIR/include \
               -DCMAKE_INSTALL_LIBDIR=$INSTALL_DIR/lib \
               -DCMAKE_INSTALL_BINDIR=$INSTALL_DIR/bin
cmake --build build --config Release -j 8 --target install

cp ggml/include/ggml-cpp.h $INSTALL_DIR/include/
#cp ggml/src/ggml-backend-impl.h $INSTALL_DIR/include/

popd
### Java generation ####
cd cppbuild

# Fix up header files
sed -i '/typedef struct ggml_backend_buffer_type \* ggml_backend_buffer_type_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_backend_buffer \* ggml_backend_buffer_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_backend_event \* ggml_backend_event_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_backend \* ggml_backend_t;/d' include/ggml-backend.h
sed -i '/typedef void \* ggml_backend_graph_plan_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_backend_reg \* ggml_backend_reg_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_backend_device \* ggml_backend_dev_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_backend_sched \* ggml_backend_sched_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_gallocr \* ggml_gallocr_t;/d' include/ggml-backend.h
sed -i '/typedef struct ggml_threadpool \* ggml_threadpool_t;/d' include/ggml-backend.h
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_buffer_type_t /struct ggml_backend_buffer_type * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_buffer_t /struct ggml_backend_buffer * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_event_t /struct ggml_backend_event * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_t /struct ggml_backend * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_graph_plan_t /void * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_reg_t /struct ggml_backend_reg * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_dev_t /struct ggml_backend_device * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_backend_sched_t /struct ggml_backend_sched * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_gallocr_t /struct ggml_gallocr * /g' {} \;
find include -type f \( -name "*.c" -o -name "*.h" \) -exec sed -i 's/ggml_threadpool_t /struct ggml_threadpool * /g' {} \;

cp -r ../src/main/java/* .

# TODO: Use ihmc-2?
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

# Remove problematic occurrences of GGML_RESTRICT in java files
find us/ihmc/llamacpp -type f -name "*.java" ! -name "LlamaCPPConfig.java" -exec sed -i 's/GGML_RESTRICT//g' {} \;
find us/ihmc/llamacpp -type f -name "*.java" -exec sed -i 's/\/\*\([a-z]\)\*\//\1/g' {} \;

cp us/ihmc/llamacpp/*.java ../src/main/java/us/ihmc/llamacpp
cp us/ihmc/llamacpp/global/*.java ../src/main/java/us/ihmc/llamacpp/global/

#### JNI compilation ####
java -cp "javacpp.jar"$CP_SEPARATOR"cuda-$JAVACPP_CUDA_VERSION.jar" org.bytedeco.javacpp.tools.Builder us/ihmc/llamacpp/*.java us/ihmc/llamacpp/global/*.java -d javainstall

##### Copy shared libs to resources ####
# Linux
mkdir -p ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
if [ -f "javainstall/libjnillamacpp.so" ]; then
  if [ "$LINUX_CROSS_COMPILE_ARM" == "1" ]; then
    cp javainstall/libjnillamacpp.so ../src/main/resources/llamacpp-javacpp/native/linux-arm64
  else
    cp javainstall/libjnillamacpp.so ../src/main/resources/llamacpp-javacpp/native/linux-x86_64
  fi
fi
