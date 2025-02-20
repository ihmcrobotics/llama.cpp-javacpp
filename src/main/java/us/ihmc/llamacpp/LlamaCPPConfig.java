package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
            include = {"ggml.h", "ggml-backend.h", "ggml-cpu.h", "ggml-cuda.h", "gguf.h", "llama.h"},
            link = {"llama", "ggml", "ggml-base", "ggml-cpu", "ggml-cuda"},
            preload = "jnillamacpp",
            define = {"GGML_USE_CUDA", "GGML_USE_NUMA"}
      ),
      @Platform(
            value = "linux",
            includepath = {"include", "/usr/local/cuda/include"}
      ),
},
      target = "us.ihmc.llamacpp",
      global = "us.ihmc.llamacpp.global.llamacpp"
)
public class LlamaCPPConfig implements InfoMapper {
   @Override
   public void map(InfoMap infoMap) {
      // Skip problematic macros and attributes
      infoMap.put(new Info("GGML_NORETURN").skip());
      infoMap.put(new Info("GGML_RESTRICT").skip());
      infoMap.put(new Info("GGML_BACKEND_API").skip());

      // Map C++ typedefs to their corresponding Java classes
      infoMap.put(new Info("ggml_backend_buffer_type_t").pointerTypes("ggml_backend_buffer_type"));
      infoMap.put(new Info("ggml_backend_buffer_t").pointerTypes("ggml_backend_buffer"));
      infoMap.put(new Info("ggml_backend_t").pointerTypes("ggml_backend"));
      infoMap.put(new Info("ggml_backend_dev_t").pointerTypes("ggml_backend_device"));
      infoMap.put(new Info("ggml_backend_event_t").pointerTypes("ggml_backend_event"));
      infoMap.put(new Info("ggml_backend_reg_t").pointerTypes("ggml_backend_reg"));
      infoMap.put(new Info("ggml_backend_sched_t").pointerTypes("ggml_backend_sched"));
      infoMap.put(new Info("ggml_threadpool_t").pointerTypes("ggml_threadpool"));
   }
}
