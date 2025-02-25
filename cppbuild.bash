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

# Add missing struct keywords
prepend_missing struct ggml_backend_graph_copy
prepend_missing struct ggml_backend_buffer_type
prepend_missing struct ggml_backend_buffer
prepend_missing struct ggml_backend_event
prepend_missing struct ggml_backend
prepend_missing struct ggml_backend_reg
prepend_missing struct ggml_backend_device

# Replace struct with enum for enums
sed -i "s/struct ggml_backend_dev_type/enum ggml_backend_dev_type/g" javainstall/jnillamacpp.cpp
sed -i "s/struct ggml_backend_buffer_usage/enum ggml_backend_buffer_usage/g" javainstall/jnillamacpp.cpp

remove_struct() {
    local word="$1"
    sed -i "s/struct $word/$word/g" javainstall/jnillamacpp.cpp
}

# Remove struct keywords where they don't belong
remove_struct ggml_backend_tensor_copy
remove_struct ggml_backend_guid
remove_struct ggml_backend_get_default_buffer_type
remove_struct ggml_backend_alloc_buffer
remove_struct ggml_backend_get_alignment
remove_struct ggml_backend_get_max_size
remove_struct ggml_backend_tensor_set_async
remove_struct ggml_backend_tensor_get_async
remove_struct ggml_backend_tensor_set
remove_struct ggml_backend_tensor_get
remove_struct ggml_backend_tensor_memset
remove_struct ggml_backend_tensor_alloc
remove_struct ggml_backend_graph_plan_create
remove_struct ggml_backend_graph_plan_free
remove_struct ggml_backend_graph_plan_compute
remove_struct ggml_backend_sched_set_eval_callback
remove_struct ggml_backend_sched_graph_compute_async
remove_struct ggml_backend_sched_graph_compute
remove_struct ggml_backend_sched_alloc_graph
remove_struct ggml_backend_sched_get_tensor_backend
remove_struct ggml_backend_sched_set_tensor_backend
remove_struct ggml_backend_sched_get_buffer_size
remove_struct ggml_backend_sched_get_n_copies
remove_struct ggml_backend_sched_get_n_splits
remove_struct ggml_backend_sched_get_backend
remove_struct ggml_backend_sched_get_n_backends
remove_struct ggml_backend_sched_reserve
remove_struct ggml_backend_sched_new
remove_struct ggml_backend_load_all_from_path
remove_struct ggml_backend_load_all
remove_struct ggml_backend_load
remove_struct ggml_backend_init_best
remove_struct ggml_backend_init_by_type
remove_struct ggml_backend_init_by_name
remove_struct ggml_backend_dev_by_type
remove_struct ggml_backend_dev_by_name
remove_struct ggml_backend_dev_get
remove_struct ggml_backend_dev_count
remove_struct ggml_backend_reg_by_name
remove_struct ggml_backend_reg_get
remove_struct ggml_backend_reg_count
remove_struct ggml_backend_reg_get_proc_address
remove_struct ggml_backend_reg_dev_get
remove_struct ggml_backend_reg_dev_count
remove_struct ggml_backend_reg_name
remove_struct ggml_backend_dev_offload_op
remove_struct ggml_backend_dev_supports_buft
remove_struct ggml_backend_dev_supports_op
remove_struct ggml_backend_dev_buffer_from_host_ptr
remove_struct ggml_backend_dev_host_buffer_type
remove_struct ggml_backend_dev_buffer_type
remove_struct ggml_backend_dev_init
remove_struct ggml_backend_dev_backend_reg
remove_struct ggml_backend_dev_get_props
remove_struct ggml_backend_dev_memory
remove_struct ggml_backend_dev_description
remove_struct ggml_backend_dev_name
remove_struct ggml_backend_event_wait
remove_struct ggml_backend_event_record
remove_struct ggml_backend_event
remove_struct ggml_backend_device
remove_struct ggml_backend_offload_op
remove_struct ggml_backend_supports_buft
remove_struct ggml_backend_supports_op
remove_struct ggml_backend_graph_compute_async
remove_struct ggml_backend_graph_compute
remove_struct ggml_backend_compare_graph_backend

remove_struct ggml_backend_buft_name
remove_struct ggml_backend_buft_alloc_buffer
remove_struct ggml_backend_buft_get_alignment
remove_struct ggml_backend_buft_get_max_size
remove_struct ggml_backend_buft_get_alloc_size
remove_struct ggml_backend_buft_is_host
remove_struct ggml_backend_buft_get_device

remove_struct ggml_backend_buffer_name
remove_struct ggml_backend_buffer_get_base
remove_struct ggml_backend_buffer_get_size
remove_struct ggml_backend_buffer_init_tensor
remove_struct ggml_backend_buffer_get_alignment
remove_struct ggml_backend_buffer_get_max_size
remove_struct ggml_backend_buffer_get_alloc_size
remove_struct ggml_backend_buffer_clear
remove_struct ggml_backend_buffer_is_host
remove_struct ggml_backend_buffer_set_usage
remove_struct ggml_backend_buffer_get_type

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
