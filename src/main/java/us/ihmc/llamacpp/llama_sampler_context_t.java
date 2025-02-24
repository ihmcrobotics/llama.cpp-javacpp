// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    //
    // Sampling API
    //
    // Sample usage:
    //
    //    // prepare the sampling chain at the start
    //    auto sparams = llama_sampler_chain_default_params();
    //
    //    llama_sampler * smpl = llama_sampler_chain_init(sparams);
    //
    //    llama_sampler_chain_add(smpl, llama_sampler_init_top_k(50));
    //    llama_sampler_chain_add(smpl, llama_sampler_init_top_p(0.9, 1));
    //    llama_sampler_chain_add(smpl, llama_sampler_init_temp (0.8));
    //
    //    // typically, the chain should end with a sampler such as "greedy", "dist" or "mirostat"
    //    // this sampler will be responsible to select the actual token
    //    llama_sampler_chain_add(smpl, llama_sampler_init_dist(seed));
    //
    //    ...
    //
    //    // decoding loop:
    //    while (...) {
    //        ...
    //
    //        llama_decode(ctx, batch);
    //
    //        // sample from the logits of the last token in the batch
    //        const llama_token id = llama_sampler_sample(smpl, ctx, -1);
    //
    //        // accepting the token updates the internal state of certain samplers (e.g. grammar, repetition, etc.)
    //        llama_sampler_accept(smpl, id);
    //        ...
    //    }
    //
    //    llama_sampler_free(smpl);
    //
    // TODO: In the future, llama_sampler will be utilized to offload the sampling to the backends (e.g. GPU).
    //

    @Namespace @Name("void") @Opaque @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_sampler_context_t extends Pointer {
        /** Empty constructor. Calls {@code super((Pointer)null)}. */
        public llama_sampler_context_t() { super((Pointer)null); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_sampler_context_t(Pointer p) { super(p); }
    }
