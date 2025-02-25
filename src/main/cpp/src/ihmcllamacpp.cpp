#include "ihmcllamacpp.h"

void ihmc_ggml_backend_load_all()
{
    ggml_backend_load_all();
}

int main(int argc, char ** argv)
{
    ggml_backend_load_all();
}
