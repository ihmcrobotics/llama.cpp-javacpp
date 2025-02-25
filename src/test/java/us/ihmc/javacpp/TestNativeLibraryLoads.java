package us.ihmc.javacpp;

import org.bytedeco.javacpp.IntPointer;
import org.junit.jupiter.api.Test;
import us.ihmc.llamacpp.global.llamacpp;
import us.ihmc.llamacpp.library.LlamaCPPNativeLibrary;
import us.ihmc.llamacpp.llama_batch;
import us.ihmc.llamacpp.llama_context;

public class TestNativeLibraryLoads
{
   @Test
   public void testLlamaCPPLoads()
   {
      LlamaCPPNativeLibrary.load();

      llama_context context = new llama_context();

      llama_batch b = new llama_batch();
      IntPointer token = new IntPointer(1);
      token.put(34343443);
      b.token(token);
      System.out.println(b.token().get());

      b.close();
   }
}
