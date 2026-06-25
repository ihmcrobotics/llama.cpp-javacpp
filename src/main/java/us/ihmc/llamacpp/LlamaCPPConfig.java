package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
            includepath = "include",
            linkpath = "lib",
            include = {
               "ggml-backend.h",
               "ggml.h",
               "ggml-alloc.h",
               "ggml-cpu.h",
               "llama.h",
            },
            link = {
               "llama",
               "ggml",
               "ggml-base",
               "ggml-cpu",
               "ggml-cuda"
            },
            preload = "jnillamacpp"
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
      infoMap.put(new Info().enumerate());

      // llamacpp
      infoMap.put(new Info("LLAMA_API").cppText("#define LLAMA_API").cppTypes());
      infoMap.put(new Info("llama_numa_init").skip());
      infoMap.put(new Info("llama_attach_threadpool").skip());
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

      // --- Newer llama.cpp API (post-b4829) ---
      // The KV-cache accessors were replaced by an opaque memory handle (llama_memory_t).
      infoMap.put(new Info("llama_memory_t").valueTypes("llama_memory_i").pointerTypes("llama_memory_i"));
      // New interleaved M-RoPE constant aliases a GGML enum value that isn't in scope here.
      infoMap.put(new Info("LLAMA_ROPE_TYPE_IMROPE").skip());
      // gguf_context is declared in gguf.h (not parsed); the only consumer is llama_model_init_from_user.
      infoMap.put(new Info("gguf_context").skip());
      infoMap.put(new Info("llama_model_init_from_user").skip());
      // The training/optimization API pulls in ggml-opt.h types we don't parse; we don't use it.
      infoMap.put(new Info("ggml_opt_dataset_t",
                           "ggml_opt_result_t",
                           "ggml_opt_epoch_callback",
                           "ggml_opt_get_optimizer_params",
                           "ggml_opt_optimizer_type").skip());
      infoMap.put(new Info("llama_opt_params", "llama_opt_init", "llama_opt_epoch").skip());
      // These take pointer-to-handle arrays (e.g. ggml_backend_t *), which JavaCPP mis-binds against
      // our value-type handle mappings. We don't use them (single-handle variants remain available).
      infoMap.put(new Info("ggml_backend_sched_new",
                           "ggml_backend_meta_device",
                           "ggml_gallocr_new_n").skip());

      // ggml
      infoMap.put(new Info("GGML_NORETURN").skip());
      infoMap.put(new Info("GGML_BACKEND_API").skip());
      infoMap.put(new Info("GGML_TENSOR_LOCALS_1").skip());
      infoMap.put(new Info("GGML_TENSOR_LOCALS_2").skip());
      infoMap.put(new Info("GGML_TENSOR_LOCALS_3").skip());
      infoMap.put(new Info("GGML_TENSOR_LOCALS").skip());
      infoMap.put(new Info("GGML_TENSOR_UNARY_OP_LOCALS").skip());
      infoMap.put(new Info("GGML_TENSOR_BINARY_OP_LOCALS").skip());
      infoMap.put(new Info("GGML_TENSOR_BINARY_OP_LOCALS01").skip());
      infoMap.put(new Info("GGML_TENSOR_TERNARY_OP_LOCALS").skip());
      infoMap.put(new Info("GGML_RESTRICT").cppTypes().annotations());
      infoMap.put(new Info("GGML_API").cppTypes().annotations());

      infoMap.put(new Info("ggml_backend_t").valueTypes("ggml_backend"));
      infoMap.put(new Info("ggml_backend_event_t").valueTypes("ggml_backend_event"));
      infoMap.put(new Info("ggml_backend_dev_t").valueTypes("ggml_backend_device"));
      infoMap.put(new Info("ggml_backend_sched_t").valueTypes("ggml_backend_sched"));
      infoMap.put(new Info("ggml_threadpool_t").valueTypes("ggml_threadpool"));
      infoMap.put(new Info("ggml_backend_buffer_t").valueTypes("ggml_backend_buffer"));
      infoMap.put(new Info("ggml_backend_buffer_type_t").valueTypes("ggml_backend_buffer_type"));
      infoMap.put(new Info("ggml_backend_reg_t").valueTypes("ggml_backend_reg"));
      infoMap.put(new Info("ggml_gallocr_t").valueTypes("ggml_gallocr"));

      // TODO:
      infoMap.put(new Info("ggml_guid_t").skip());
      infoMap.put(new Info("ggml_from_float_t").skip());
      infoMap.put(new Info("ggml_to_float_t").skip());
      infoMap.put(new Info("ggml_vec_dot_t").skip());
      infoMap.put(new Info("ggml_abort_callback").skip());
      infoMap.put(new Info("ggml_backend_graph_copy").skip());
      infoMap.put(new Info("ggml_backend_dev_type").skip());

      // TODO: Windows
      infoMap.put(new Info("ggml_graph_export").skip());
      infoMap.put(new Info("ggml_graph_import").skip());
   }
}
