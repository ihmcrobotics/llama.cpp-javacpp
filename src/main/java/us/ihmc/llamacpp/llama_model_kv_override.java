// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_model_kv_override extends Pointer {
        static { Loader.load(); }
        /** Default native constructor. */
        public llama_model_kv_override() { super((Pointer)null); allocate(); }
        /** Native array allocator. Access with {@link Pointer#position(long)}. */
        public llama_model_kv_override(long size) { super((Pointer)null); allocateArray(size); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_model_kv_override(Pointer p) { super(p); }
        private native void allocate();
        private native void allocateArray(long size);
        @Override public llama_model_kv_override position(long position) {
            return (llama_model_kv_override)super.position(position);
        }
        @Override public llama_model_kv_override getPointer(long i) {
            return new llama_model_kv_override((Pointer)this).offsetAddress(i);
        }
    
        public native llama_model_kv_override_type tag(); public native llama_model_kv_override tag(llama_model_kv_override_type setter);

        public native @Cast("char") byte key(int i); public native llama_model_kv_override key(int i, byte setter);
        @MemberGetter public native @Cast("char*") BytePointer key();
            public native @Cast("int64_t") long val_i64(); public native llama_model_kv_override val_i64(long setter);
            public native double val_f64(); public native llama_model_kv_override val_f64(double setter);
            public native @Cast("bool") boolean val_bool(); public native llama_model_kv_override val_bool(boolean setter);
            public native @Cast("char") byte val_str(int i); public native llama_model_kv_override val_str(int i, byte setter);
            @MemberGetter public native @Cast("char*") BytePointer val_str();
    }
