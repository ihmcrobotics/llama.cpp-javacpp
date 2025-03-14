// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


// Tensor allocator
@Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class ggml_tallocr extends Pointer {
    static { Loader.load(); }
    /** Default native constructor. */
    public ggml_tallocr() { super((Pointer)null); allocate(); }
    /** Native array allocator. Access with {@link Pointer#position(long)}. */
    public ggml_tallocr(long size) { super((Pointer)null); allocateArray(size); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public ggml_tallocr(Pointer p) { super(p); }
    private native void allocate();
    private native void allocateArray(long size);
    @Override public ggml_tallocr position(long position) {
        return (ggml_tallocr)super.position(position);
    }
    @Override public ggml_tallocr getPointer(long i) {
        return new ggml_tallocr((Pointer)this).offsetAddress(i);
    }

    public native ggml_backend_buffer buffer(); public native ggml_tallocr buffer(ggml_backend_buffer setter);
    public native Pointer base(); public native ggml_tallocr base(Pointer setter);
    public native @Cast("size_t") long alignment(); public native ggml_tallocr alignment(long setter);
    public native @Cast("size_t") long offset(); public native ggml_tallocr offset(long setter);
}
