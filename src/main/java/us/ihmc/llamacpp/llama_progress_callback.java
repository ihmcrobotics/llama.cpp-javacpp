// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


    @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_progress_callback extends FunctionPointer {
        static { Loader.load(); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public    llama_progress_callback(Pointer p) { super(p); }
        protected llama_progress_callback() { allocate(); }
        private native void allocate();
        public native @Cast("bool") boolean call(float progress, Pointer user_data);
    }
