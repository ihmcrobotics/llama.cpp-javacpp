#!/bin/bash
# Regenerate ONLY the JavaCPP bindings + JNI from an already-built cppbuild/ tree.
# Use this to iterate on LlamaCPPConfig.java without recompiling the native llama.cpp libraries.
# Run cppbuild-cuda.bash first to produce cppbuild/include, cppbuild/lib and cppbuild/javacpp.jar.
set -e

# Clean previously generated Java (keep the hand-written config + loader).
find src/main/java/us/ihmc/llamacpp -maxdepth 1 -type f -not \( -name "LlamaCPPConfig.java" \) -delete
rm -rf cppbuild/us

cd cppbuild
cp -r ../src/main/java/* .

#### Java generation ####
java -cp "javacpp.jar" org.bytedeco.javacpp.tools.Builder us/ihmc/llamacpp/LlamaCPPConfig.java

cp us/ihmc/llamacpp/*.java ../src/main/java/us/ihmc/llamacpp
cp us/ihmc/llamacpp/global/*.java ../src/main/java/us/ihmc/llamacpp/global/

#### JNI compilation ####
java -cp "javacpp.jar" org.bytedeco.javacpp.tools.Builder us/ihmc/llamacpp/*.java us/ihmc/llamacpp/global/*.java -d javainstall

#### Copy shared libs to resources (Linux) ####
mkdir -p ../src/main/resources/llamacpp/native/linux-x86_64
for lib in libggml.so libggml-base.so libggml-cpu.so libggml-cuda.so libllama.so; do
  [ -f "lib/$lib" ] && cp "lib/$lib" ../src/main/resources/llamacpp/native/linux-x86_64
done
[ -f "javainstall/libjnillamacpp.so" ] && cp javainstall/libjnillamacpp.so ../src/main/resources/llamacpp/native/linux-x86_64

echo "REGEN_OK"
