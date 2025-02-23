#!/bin/bash

rm -rf cppbuild/us

find src/main/java/us/ihmc/llamacpp -maxdepth 1 -type f -not \( -name "LlamaCPPConfig.java" \) -delete
