// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class ggml_custom1_op_f32_t extends FunctionPointer {
        static { Loader.load(); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public    ggml_custom1_op_f32_t(Pointer p) { super(p); }
        protected ggml_custom1_op_f32_t() { allocate(); }
        private native void allocate();
        public native void call(ggml_tensor arg0, @Const ggml_tensor arg1);
    }
