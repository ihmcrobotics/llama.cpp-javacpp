package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
//            include = {"ggml.h", "ggml-backend.h", "ggml-backend-impl.h", "ggml-cpp.h", "ggml-alloc.h", "ggml-opt.h", "ggml-cpu.h", "ggml-cuda.h", "gguf.h", "llama.h", "llama-cpp.h"},
            include = {
//                  "ggml.h",
                 "ggml-backend.h",
//                 "ggml-alloc.h",
//                 "ggml-cpp.h",

//                 "ggml-cpu.h",
//                 "ggml-cuda.h",
//                 "gguf.h",
                 "llama.h",
//                 "llama-cpp.h"
            },
            link = {"llama", "ggml", "ggml-base", "ggml-cpu", "ggml-cuda"},
            preload = "jnillamacpp",
            define = {"GGML_USE_CUDA", "GGML_USE_NUMA"}
      ),
      @Platform(
            value = "linux",
            includepath = {"include", "/usr/local/cuda/include", "lib"},
            linkpath = {"include", "/usr/local/cuda/include", "lib"}
      ),
},
      target = "us.ihmc.llamacpp",
      global = "us.ihmc.llamacpp.global.llamacpp"
)
public class LlamaCPPConfig implements InfoMapper {
   @Override
   public void map(InfoMap infoMap) {
      infoMap.put(new Info("LLAMA_API").cppText("#define LLAMA_API").cppTypes());
      infoMap.put(new Info("GGML_API").skip());
      infoMap.put(new Info("GGML_NORETURN").skip());
      infoMap.put(new Info("GGML_RESTRICT").skip());
      infoMap.put(new Info("GGML_BACKEND_API").skip());
      infoMap.put(new Info("llama_numa_init").skip());
      infoMap.put(new Info("llama_attach_threadpool").skip());
      infoMap.put(new Info("llama_log_set").skip());
      infoMap.put(new Info("LLAMA_ROPE_TYPE_NEOX").skip());
      infoMap.put(new Info("LLAMA_ROPE_TYPE_MROPE").skip());
      infoMap.put(new Info("LLAMA_ROPE_TYPE_VISION").skip());
      infoMap.put(new Info("llama_set_abort_callback").skip());
      infoMap.put(new Info("output_tensor_type").skip());
      infoMap.put(new Info("token_embedding_type").skip());
      infoMap.put(new Info("cb_eval").skip());
      infoMap.put(new Info("type_k").skip());
      infoMap.put(new Info("type_v").skip());
      infoMap.put(new Info("abort_callback").skip());
      infoMap.put(new Info("devices").skip());
      infoMap.put(new Info("llama_pooling_type").skip());
      infoMap.put(new Info("llama_vocab_type").skip());

      infoMap.put(new Info("ggml_backend_buffer").skip());
   }
}
