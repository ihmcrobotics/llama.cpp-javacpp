package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
            include = {
                 "ggml-backend.h",
                 "ggml.h",
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
      infoMap.put(new Info("GGML_API").cppText("#define GGML_API extern").cppTypes());
//      infoMap.put(new Info("GGML_DEPRECATED").cppText("#define GGML_DEPRECATED").cppTypes());

//      infoMap.put(new Info("GGML_ATTRIBUTE_FORMAT").cppText("#define GGML_ATTRIBUTE_FORMAT").cppTypes());
//      infoMap.put(new Info("GGML_BACKEND_API").cppText("#define GGML_BACKEND_API extern").cppTypes());
      infoMap.put(new Info("GGML_NORETURN").skip());
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

      infoMap.put(new Info("GGML_RESTRICT").skip());
      infoMap.put(new Info("ggml_to_float_t").skip());
      infoMap.put(new Info("ggml_from_float_t").skip());

      infoMap.put(new Info("ggml_backend_buffer_t").pointerTypes("ggml_backend_buffer_type"));
      infoMap.put(new Info("ggml_backend_buffer_type_t").pointerTypes("ggml_backend_buffer_type"));
      infoMap.put(new Info("ggml_backend_dev_t").pointerTypes("ggml_backend_device"));
      infoMap.put(new Info("ggml_backend_t").pointerTypes("ggml_backend"));
      infoMap.put(new Info("ggml_backend_sched_t").pointerTypes("ggml_backend_sched"));
      infoMap.put(new Info("ggml_backend_reg_t").pointerTypes("ggml_backend_reg"));
      infoMap.put(new Info("ggml_backend_event_t").pointerTypes("ggml_backend_event"));

      // TODO:
      infoMap.put(new Info("ggml_guid").cast().valueTypes("byte[]").pointerTypes("BytePointer"));
      infoMap.put(new Info("ggml_guid_t").cast().valueTypes("BytePointer").pointerTypes("PointerPointer"));

      infoMap.put(new Info("ggml_status").enumerate());
   }
}
