// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;

    @Opaque @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class llama_model extends Pointer {
        /** Empty constructor. Calls {@code super((Pointer)null)}. */
        public llama_model() { super((Pointer)null); }
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public llama_model(Pointer p) { super(p); }
    }
