package us.ihmc.javacpp;

import us.ihmc.llamacpp.library.LlamaCPPNativeLibrary;
import us.ihmc.llamacpp.llama_context;
import us.ihmc.llamacpp.llama_context_params;
import us.ihmc.llamacpp.llama_model;
import us.ihmc.llamacpp.llama_model_params;
import us.ihmc.llamacpp.llama_sampler;
import us.ihmc.llamacpp.llama_vocab;

import static us.ihmc.llamacpp.global.llamacpp.*;

// https://github.com/ggml-org/llama.cpp/blob/master/examples/simple-chat/simple-chat.cpp
public class SimpleChat
{
   public static void main(String[] args)
   {
      LlamaCPPNativeLibrary.load();

      ggml_backend_load_all();

      llama_model_params model_params = llama_model_default_params();
      model_params.n_gpu_layers(99);

      llama_model model = llama_model_load_from_file("/home/d/.ihmc/llama-models/Llama-3.2-1B-Instruct-Q8_0.gguf", model_params);
      llama_vocab vocab = llama_model_get_vocab(model);

      llama_context_params ctx_params = llama_context_default_params();
      ctx_params.n_ctx(2048);
      ctx_params.n_batch(2048);

      llama_context ctx = llama_init_from_model(model, ctx_params);

      // initialize the sampler
      llama_sampler smpl = llama_sampler_chain_init(llama_sampler_chain_default_params());
      llama_sampler_chain_add(smpl, llama_sampler_init_min_p(0.05f, 1));
      llama_sampler_chain_add(smpl, llama_sampler_init_temp(0.8f));
      llama_sampler_chain_add(smpl, llama_sampler_init_dist(LLAMA_DEFAULT_SEED));
   }
}
