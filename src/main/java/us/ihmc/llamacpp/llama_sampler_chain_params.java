// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_sampler_chain_params extends Pointer {
        static { Loader.load(); }
        /** Default native constructor. */
        public llama_sampler_chain_params() { super((Pointer)null); allocate(); }
        /** Native array allocator. Access with {@link Pointer#position(long)}. */
        public llama_sampler_chain_params(long size) { super((Pointer)null); allocateArray(size); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_sampler_chain_params(Pointer p) { super(p); }
        private native void allocate();
        private native void allocateArray(long size);
        @Override public llama_sampler_chain_params position(long position) {
            return (llama_sampler_chain_params)super.position(position);
        }
        @Override public llama_sampler_chain_params getPointer(long i) {
            return new llama_sampler_chain_params((Pointer)this).offsetAddress(i);
        }
    
        public native @Cast("bool") boolean no_perf(); public native llama_sampler_chain_params no_perf(boolean setter); // whether to measure performance timings
    }
