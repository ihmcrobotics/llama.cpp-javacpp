package us.ihmc.llamacpp.library;

import us.ihmc.tools.nativelibraries.NativeLibraryDescription;
import us.ihmc.tools.nativelibraries.NativeLibraryLoader;
import us.ihmc.tools.nativelibraries.NativeLibraryWithDependencies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO
public class LlamaCPPNativeLibrary implements NativeLibraryDescription {
    @Override
    public String getPackage(OperatingSystem operatingSystem, Architecture arch)
    {
        return "";
    }

    @Override
    public NativeLibraryWithDependencies getLibraryWithDependencies(OperatingSystem operatingSystem, Architecture arch)
    {
        return null;
    }
}