package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
//            include = {"ggml.h", "ggml-backend.h", "ggml-backend-impl.h", "ggml-cpp.h", "ggml-alloc.h", "ggml-opt.h", "ggml-cpu.h", "ggml-cuda.h", "gguf.h", "llama.h", "llama-cpp.h"},
            include = {"ggml.h", "ggml-backend.h", "ggml-cpp.h", "ggml-alloc.h", "ggml-cpu.h", "ggml-cuda.h", "gguf.h", "llama.h", "llama-cpp.h"},
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
//      infoMap.put(new Info().enumerate().friendly());

      // Skip problematic macros and attributes
      infoMap.put(new Info().cppNames("GGML_NORETURN").skip());
      infoMap.put(new Info().cppNames("GGML_RESTRICT").skip());
      infoMap.put(new Info().cppNames("GGML_BACKEND_API").skip());

      // Map internal types to Pointer
      infoMap.put(new Info().cppNames("ggml_backend_device").pointerTypes("Pointer"));
      infoMap.put(new Info().cppNames("ggml_backend_event").pointerTypes("Pointer"));
      infoMap.put(new Info().cppNames("ggml_backend_sched").pointerTypes("Pointer"));
      infoMap.put(new Info().cppNames("ggml_backend_reg").pointerTypes("Pointer"));

      // This makes sure to prefix "struct " in the generated .cpp
      infoMap.put(new Info().cppNames("struct ggml_backend_graph_copy").pointerTypes("ggml_backend_graph_copy"));

      infoMap.put(new Info().cppNames("ggml_backend_dev_type").enumerate());

//      String[] aliasedStructs = {"ggml_backend_buffer_type",
//                                 "ggml_backend_buffer",
//                                 "ggml_backend_event",
//                                 "ggml_backend",
//                                 "ggml_backend_reg",
//                                 "ggml_backend_sched",
//                                 "ggml_gallocr",
//                                 "ggml_threadpool"};
//      for (String aliasedStruct : aliasedStructs)
//      {
//         String alias = aliasedStruct + "_t";
//         infoMap.put(new Info(aliasedStruct).pointerTypes(alias));
//         infoMap.put(new Info(alias).valueTypes(alias).pointerTypes("@Cast(\"%1$s*\") PointerPointer".formatted(alias), "@ByPtrPtr %1$s".formatted(alias)));
//      }
//
//      // This one's different
//      String alias = "ggml_backend_dev_t";
//      infoMap.put(new Info("ggml_backend_device").pointerTypes(alias));
//      infoMap.put(new Info(alias).valueTypes(alias).pointerTypes("@Cast(\"%1$s*\") PointerPointer".formatted(alias), "@ByPtrPtr %1$s".formatted(alias)));



      // Map C++ typedefs to their corresponding Java classes
//      infoMap.put(new Info().cppNames("struct ggml_backend_device").pointerTypes("ggml_backend_device"));
//      infoMap.put(new Info().cppNames("ggml_backend_device").skip());

//      infoMap.put(new Info().cppNames("ggml_backend_buffer_type_t").pointerTypes("ggml_backend_buffer_type"));
//      infoMap.put(new Info().cppNames("ggml_backend_buffer_t").pointerTypes("ggml_backend_buffer"));
//      infoMap.put(new Info().cppNames("ggml_backend_event_t").pointerTypes("ggml_backend_event"));
//      infoMap.put(new Info().cppNames("ggml_backend_t").pointerTypes("ggml_backend"));
//      infoMap.put(new Info().cppNames("ggml_backend_reg_t").pointerTypes("ggml_backend_reg"));
//      infoMap.put(new Info().cppNames("ggml_backend_dev_t").pointerTypes("ggml_backend_device"));
//      infoMap.put(new Info().cppNames("ggml_backend_sched_t").pointerTypes("ggml_backend_sched"));

//      infoMap.put(new Info().cppNames("ggml_threadpool_t").pointerTypes("ggml_threadpool"));
//      infoMap.put(new Info().cppNames("ggml_gallocr_t").pointerTypes("ggml_gallocr"));
//      infoMap.put(new Info().cppNames("ggml_opt_context_t").pointerTypes("ggml_opt_context"));
//      infoMap.put(new Info().cppNames("ggml_opt_dataset_t").pointerTypes("ggml_opt_dataset"));
//      infoMap.put(new Info().cppNames("ggml_opt_result_t").pointerTypes("ggml_opt_result"));

   }
}
