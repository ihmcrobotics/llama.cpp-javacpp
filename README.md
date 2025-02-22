# JavaCPP Bindings for LLaMA C++

Java support for [llama.cpp](https://github.com/ggml-org/llama.cpp).

Supported platforms:
- Linux (Ubuntu 20.04+ or similar x86_64)

Requires Java 17.
### Gradle
```
dependencies {
  implementation("us.ihmc:llamacpp-javacpp:b4743")
}
```
### Maven
```
<dependencies>
  <dependency>
    <groupId>us.ihmc</groupId>
    <artifactId>llamacpp-javacpp</artifactId>
    <version>b4743</version>
  </dependency>
</dependencies>
```
### Setup
You must manually load the library first before using it.
```
LlamaCPPNativeLibrary.load();
```
Ensure [this test](https://github.com/ihmcrobotics/llama.cpp-javacpp/blob/main/src/test/java/us/ihmc/llamacpp/test/TestNativeLibraryLoads.java) runs on your machine before proceeding.

### Debugging JavaCPP Builder

In IntelliJ, create a run configuration for `org.bytedeco.javacpp.tools.Builder` with the arguments
spit out when running `cppbuild.bash`. Use JDK 17 but set language level and bytecode version to 8 in the IDE.
Native library loader dependency is compiled with 17, so can't use JDK 8.

### Developing JavaCPP Fixes

Outside of docker, clone javacpp:
```
llama.cpp-javacpp $ git -b 1.5.11 https://github.com/bytedeco/javacpp.git
```
