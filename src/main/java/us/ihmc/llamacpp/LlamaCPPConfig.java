package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
            include = {"llama.cpp-b4743/include/llama.h", "llama.cpp-b4743/include/llama-cpp.h"},
            linkpath = "lib",
            link = {"llama", "ggml", "ggml-base", "ggml-cpu", "ggml-cuda", "llava_shared"},
            preload = "jnillamacpp"
      ),
      @Platform(
            value = "linux",
            includepath = {"include", "/usr/local/cuda/include"}
      ),
},
      target = "us.ihmc.llama",
      global = "us.ihmc.llama.global"
)
public class LlamaCPPConfig implements InfoMapper {
   @Override
   public void map(InfoMap infoMap) {

   }
}
