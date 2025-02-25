// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    // Input data for llama_decode
    // A llama_batch object can contain input about one or many sequences
    // The provided arrays (i.e. token, embd, pos, etc.) must have size of n_tokens
    //
    // - token  : the token ids of the input (used when embd is NULL)
    // - embd   : token embeddings (i.e. float vector of size n_embd) (used when token is NULL)
    // - pos    : the positions of the respective token in the sequence
    //            (if set to NULL, the token position will be tracked automatically by llama_decode)
    // - seq_id : the sequence to which the respective token belongs
    //            (if set to NULL, the sequence ID will be assumed to be 0)
    // - logits : if zero, the logits (and/or the embeddings) for the respective token will not be output
    //            (if set to NULL, only the logits for last token will be returned)
    //
    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_batch extends Pointer {
        static { Loader.load(); }
        /** Default native constructor. */
        public llama_batch() { super((Pointer)null); allocate(); }
        /** Native array allocator. Access with {@link Pointer#position(long)}. */
        public llama_batch(long size) { super((Pointer)null); allocateArray(size); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_batch(Pointer p) { super(p); }
        private native void allocate();
        private native void allocateArray(long size);
        @Override public llama_batch position(long position) {
            return (llama_batch)super.position(position);
        }
        @Override public llama_batch getPointer(long i) {
            return new llama_batch((Pointer)this).offsetAddress(i);
        }
    
        public native int n_tokens(); public native llama_batch n_tokens(int setter);

        public native @Cast("llama_token*") IntPointer token(); public native llama_batch token(IntPointer setter);
        public native FloatPointer embd(); public native llama_batch embd(FloatPointer setter);
        public native @Cast("llama_pos*") IntPointer pos(); public native llama_batch pos(IntPointer setter);
        public native IntPointer n_seq_id(); public native llama_batch n_seq_id(IntPointer setter);
        public native @Cast("llama_seq_id*") IntPointer seq_id(int i); public native llama_batch seq_id(int i, IntPointer setter);
        public native @Cast("llama_seq_id**") PointerPointer seq_id(); public native llama_batch seq_id(PointerPointer setter);
        public native BytePointer logits(); public native llama_batch logits(BytePointer setter); // TODO: rename this to "output"
    }
