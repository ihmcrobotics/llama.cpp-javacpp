// Targeted by JavaCPP version 1.5.11: DO NOT EDIT THIS FILE

package us.ihmc.llamacpp;

import java.nio.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

import static us.ihmc.llamacpp.global.llamacpp.*;


// Graph allocator
/*
  Example usage:
    ggml_gallocr_t galloc = ggml_gallocr_new(ggml_backend_cpu_buffer_type());

    // optional: create a worst-case graph and reserve the buffers to avoid reallocations
    ggml_gallocr_reserve(galloc, build_graph(max_batch));

    // allocate the graph
    struct ggml_cgraph * graph = build_graph(batch);
    ggml_gallocr_alloc_graph(galloc, graph);

    printf("compute buffer size: %zu bytes\n", ggml_gallocr_get_buffer_size(galloc, 0));

    // evaluate the graph
    ggml_backend_graph_compute(backend, graph);
*/

// special tensor flags for use with the graph allocator:
//   ggml_set_input(): all input tensors are allocated at the beginning of the graph in non-overlapping addresses
//   ggml_set_output(): output tensors are never freed and never overwritten

@Opaque @Properties(inherit = us.ihmc.llamacpp.LlamaCPPConfig.class)
public class ggml_gallocr extends Pointer {
    /** Empty constructor. Calls {@code super((Pointer)null)}. */
    public ggml_gallocr() { super((Pointer)null); }
    /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
    public ggml_gallocr(Pointer p) { super(p); }
}
