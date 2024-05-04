package fr.paulbrancieq.modpackconfigupdater.optionreftest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.AnnotatedTypeAdapterFactory;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionRef;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OptionRefTest {
  @Test
  public void optionRefTest() {
    try {
      JsonReader reader = new JsonReader(new FileReader("src/test/java/fr/paulbrancieq/modpackconfigupdater" +
              "/optionreftest/optionref/objectoptionref.json"));
      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
              .registerTypeAdapterFactory(new AnnotatedTypeAdapterFactory()).create();
      OptionRef optionRef = gson.fromJson(reader, OptionRef.class);
      assertEquals("src"+ File.separator + "HelloWorld.json", optionRef.getFilePath().toString());
    } catch (FileNotFoundException e) {
      fail(e);
    }
  }
}
