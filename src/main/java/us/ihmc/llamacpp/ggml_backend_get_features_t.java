// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;

    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class ggml_backend_get_features_t extends FunctionPointer {
        static { Loader.load(); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public    ggml_backend_get_features_t(Pointer p) { super(p); }
        protected ggml_backend_get_features_t() { allocate(); }
        private native void allocate();
        public native ggml_backend_feature call(@ByVal ggml_backend_reg reg);
    }
