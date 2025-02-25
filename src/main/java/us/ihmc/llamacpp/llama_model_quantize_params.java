// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    // model quantization parameters
    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_model_quantize_params extends Pointer {
        static { Loader.load(); }
        /** Default native constructor. */
        public llama_model_quantize_params() { super((Pointer)null); allocate(); }
        /** Native array allocator. Access with {@link Pointer#position(long)}. */
        public llama_model_quantize_params(long size) { super((Pointer)null); allocateArray(size); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_model_quantize_params(Pointer p) { super(p); }
        private native void allocate();
        private native void allocateArray(long size);
        @Override public llama_model_quantize_params position(long position) {
            return (llama_model_quantize_params)super.position(position);
        }
        @Override public llama_model_quantize_params getPointer(long i) {
            return new llama_model_quantize_params((Pointer)this).offsetAddress(i);
        }
    
        public native int nthread(); public native llama_model_quantize_params nthread(int setter);                     // number of threads to use for quantizing, if <=0 will use std::thread::hardware_concurrency()
        public native @Cast("llama_ftype") int ftype(); public native llama_model_quantize_params ftype(int setter);              // quantize to this llama_ftype   // output tensor type // token embeddings tensor type
        public native @Cast("bool") boolean allow_requantize(); public native llama_model_quantize_params allow_requantize(boolean setter);               // allow quantizing non-f32/f16 tensors
        public native @Cast("bool") boolean quantize_output_tensor(); public native llama_model_quantize_params quantize_output_tensor(boolean setter);         // quantize output.weight
        public native @Cast("bool") boolean only_copy(); public native llama_model_quantize_params only_copy(boolean setter);                      // only copy tensors - ftype, allow_requantize and quantize_output_tensor are ignored
        public native @Cast("bool") boolean pure(); public native llama_model_quantize_params pure(boolean setter);                           // quantize all tensors to the default type
        public native @Cast("bool") boolean keep_split(); public native llama_model_quantize_params keep_split(boolean setter);                     // quantize to the same number of shards
        public native Pointer imatrix(); public native llama_model_quantize_params imatrix(Pointer setter);                      // pointer to importance matrix data
        public native Pointer kv_overrides(); public native llama_model_quantize_params kv_overrides(Pointer setter);                 // pointer to vector containing overrides
    }
