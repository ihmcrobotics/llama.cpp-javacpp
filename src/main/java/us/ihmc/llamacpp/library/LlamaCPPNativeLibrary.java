package us.ihmc.llamacpp.library;

import us.ihmc.tools.nativelibraries.NativeLibraryDescription;
import us.ihmc.tools.nativelibraries.NativeLibraryLoader;
import us.ihmc.tools.nativelibraries.NativeLibraryWithDependencies;

public class LlamaCPPNativeLibrary implements NativeLibraryDescription {
    @Override
    public String getPackage(OperatingSystem os, Architecture arch) {
        String archPackage = "";
        if (arch == Architecture.x64) {
            archPackage = switch (os) {
                case LINUX64 -> "linux-x86_64";
                case WIN64, MACOSX64 -> throw new RuntimeException("Unsupported platform");
            };
        } else if (arch == Architecture.arm64) {
            throw new RuntimeException("Unsupported platform");
        }

        return "llamacpp.native." + archPackage;
    }

    @Override
    public NativeLibraryWithDependencies getLibraryWithDependencies(OperatingSystem os, Architecture arch) {
        switch (os) {
            case LINUX64 -> {
                return NativeLibraryWithDependencies.fromFilename("libjnillamacpp.so", "libggml-base.so", "libggml-cpu.so", "libggml-cuda.so", "libggml.so", "libllama.so");
            }
            case WIN64, MACOSX64 -> throw new RuntimeException("Unsupported platform");
        }
        return null;
    }

    private static boolean loaded = false;

    public static boolean load() {
        if (!loaded) {
            LlamaCPPNativeLibrary lib = new LlamaCPPNativeLibrary();
            loaded = NativeLibraryLoader.loadLibrary(lib);
        }
        return loaded;
    }
}