package us.ihmc.llamacpp;

import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(value = {
      @Platform(
            include = {"llama.h"},
            linkpath = "lib",
            link = {"llama"},
            preload = "jnillamacpp"
      ),
      @Platform(
            value = "linux",
            includepath = {"include", "/usr/local/cuda/include"}
      ),
},
      target = "us.ihmc.llama",
      global = "us.ihmc.llama.global.llama"
)
public class LlamaCPPConfig implements InfoMapper {
   @Override
   public void map(InfoMap infoMap) {

   }
}
