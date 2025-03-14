// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_token_data_array extends Pointer {
        static { Loader.load(); }
        /** Default native constructor. */
        public llama_token_data_array() { super((Pointer)null); allocate(); }
        /** Native array allocator. Access with {@link Pointer#position(long)}. */
        public llama_token_data_array(long size) { super((Pointer)null); allocateArray(size); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_token_data_array(Pointer p) { super(p); }
        private native void allocate();
        private native void allocateArray(long size);
        @Override public llama_token_data_array position(long position) {
            return (llama_token_data_array)super.position(position);
        }
        @Override public llama_token_data_array getPointer(long i) {
            return new llama_token_data_array((Pointer)this).offsetAddress(i);
        }
    
        // TODO: consider SoA
        // NOTE: this pointer can be modified by the samplers
        public native llama_token_data data(); public native llama_token_data_array data(llama_token_data setter);
        public native @Cast("size_t") long size(); public native llama_token_data_array size(long setter);
        public native @Cast("int64_t") long selected(); public native llama_token_data_array selected(long setter); // this is the index in the data array (i.e. not the token id)
        public native @Cast("bool") boolean sorted(); public native llama_token_data_array sorted(boolean setter);
    }
