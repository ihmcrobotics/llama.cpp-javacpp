package us.ihmc.javacpp;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Pointer;
import us.ihmc.llamacpp.ggml_log_callback;
import us.ihmc.llamacpp.library.LlamaCPPNativeLibrary;
import us.ihmc.llamacpp.llama_batch;
import us.ihmc.llamacpp.llama_chat_message;
import us.ihmc.llamacpp.llama_context;
import us.ihmc.llamacpp.llama_context_params;
import us.ihmc.llamacpp.llama_model;
import us.ihmc.llamacpp.llama_model_params;
import us.ihmc.llamacpp.llama_sampler;
import us.ihmc.llamacpp.llama_vocab;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static us.ihmc.llamacpp.global.llamacpp.*;
import static us.ihmc.llamacpp.global.llamacpp.ggml_log_level.GGML_LOG_LEVEL_ERROR;

// https://github.com/ggml-org/llama.cpp/blob/master/examples/simple-chat/simple-chat.cpp
public class SimpleChat {
   static {
      LlamaCPPNativeLibrary.load();
   }

   public static final Path MODELS_DIRECTORY = Paths.get(System.getProperty("user.home")).resolve(".ihmc/llama-models");
   public static final Path MODEL_TO_USE = MODELS_DIRECTORY.resolve("Llama-3.2-1B-Instruct-Q8_0.gguf");

   private final llama_context ctx;
   private final llama_vocab vocab;
   private final llama_sampler smpl;

   llama_chat_message messages = new llama_chat_message(100);
   int n_messages = 0;

   public SimpleChat() {
      // only print errors
      ggml_log_callback callback = new ggml_log_callback() {
         @Override
         public void call(ggml_log_level level, BytePointer text, Pointer user_data) {
            if (level.value >= GGML_LOG_LEVEL_ERROR.value) {
               System.err.printf("%s", text.getString());
            }
         }
      };
      llama_log_set(callback, null);

      ggml_backend_load_all();

      llama_model_params model_params = llama_model_default_params();
      model_params.n_gpu_layers(99);

      llama_model model = llama_model_load_from_file(MODEL_TO_USE.toString(), model_params);
      vocab = llama_model_get_vocab(model);

      llama_context_params ctx_params = llama_context_default_params();
      ctx_params.n_ctx(2048);
      ctx_params.n_batch(2048);

      ctx = llama_init_from_model(model, ctx_params);

      // initialize the sampler
      smpl = llama_sampler_chain_init(llama_sampler_chain_default_params());
      llama_sampler_chain_add(smpl, llama_sampler_init_min_p(0.05f, 1));
      llama_sampler_chain_add(smpl, llama_sampler_init_temp(0.8f));
      llama_sampler_chain_add(smpl, llama_sampler_init_dist(LLAMA_DEFAULT_SEED));

      BytePointer formatted = new BytePointer(llama_n_ctx(ctx));
      int prev_len = 0;
      while (true) {
         System.out.print("\033[32m> \033[0m");
         // get user input
         Scanner scanner = new Scanner(System.in);
         String user = scanner.nextLine();

         if (user.isEmpty()) {
            break;
         }

         String tmpl = llama_model_chat_template(model, (String) null);

         // add the user input to the message list and format it
         push_back_message("user", user);
         int new_len = llama_chat_apply_template(tmpl, messages, n_messages, true, formatted, (int) formatted.capacity());
         if (new_len > formatted.capacity()) {
            formatted = new BytePointer(new_len);
            new_len = llama_chat_apply_template(tmpl, messages, n_messages, true, formatted, (int) formatted.capacity());
         }
         if (new_len < 0) {
            System.err.println("failed to apply the chat template");
            System.exit(1);
         }

         String prompt = formatted.getString().substring(prev_len, new_len);

         // generate a response
         System.out.print("\033[33m");
         String response = generate(prompt);
         System.out.print("\n\033[0m");

         // add the response to the messages
         push_back_message("assistant", response);
         prev_len = llama_chat_apply_template(tmpl, messages, n_messages, false, (BytePointer) null, 0);
         if (prev_len < 0) {
            System.err.println("failed to apply the chat template");
            System.exit(1);
         }
      }

      // free resources
      messages.close();
      llama_sampler_free(smpl);
      llama_free(ctx);
      llama_model_free(model);
   }

   private String generate(String prompt) {
      String response = "";

      boolean is_first = llama_get_kv_cache_used_cells(ctx) == 0;

      int n_prompt_tokens = -llama_tokenize(vocab, prompt, prompt.length(), (IntPointer) null, 0, is_first, true);
      IntPointer prompt_tokens = new IntPointer(n_prompt_tokens);
      if (llama_tokenize(vocab, prompt, prompt.length(), prompt_tokens, n_prompt_tokens, is_first, true) < 0) {
         System.err.println("failed to tokenize the prompt");
      }

      // prepare a batch for the prompt
      llama_batch batch = llama_batch_get_one(prompt_tokens, n_prompt_tokens);

      int new_token_id;
      while (true) {
         // check if we have enough space in the context to evaluate this batch
         int n_ctx = llama_n_ctx(ctx);
         int n_ctx_used = llama_get_kv_cache_used_cells(ctx);
         if (n_ctx_used + batch.n_tokens() > n_ctx) {
            System.out.print("\033[0m\n");
            System.err.println("context size exceeded");
            System.exit(0);
         }

         if (llama_decode(ctx, batch) != 0) {
            System.err.println("failed to decode");
         }

         // sample the next token
         new_token_id = llama_sampler_sample(smpl, ctx, -1);

         // is it an end of generation?
         if (llama_vocab_is_eog(vocab, new_token_id)) {
            break;
         }

         // convert the token to a string, print it and add it to the response
         byte[] buf = new byte[256];
         int n = llama_token_to_piece(vocab, new_token_id, buf, buf.length, 0, true);
         if (n < 0) {
            System.err.println("failed to convert token to piece");
         }
         String piece = new String(buf, 0, n);
         System.out.printf("%s", piece);
         System.out.flush();
         response += piece;

         // prepare the next batch with the sampled token
         batch.token().put(0, new_token_id);
         batch.n_tokens(1);
      }

      return response;
   }

   private void push_back_message(String role, String content) {
      if (n_messages >= messages.capacity())
      {
         llama_chat_message messages_new = new llama_chat_message(n_messages * 2L);
         Pointer.memcpy(messages_new, messages, n_messages);
         messages.close();
         messages = messages_new;
      }

      llama_chat_message message = messages.getPointer(n_messages++);
      message.role(new BytePointer(role));
      message.content(new BytePointer(content));
   }

   public static void main(String[] args) {
      new SimpleChat();
   }
}