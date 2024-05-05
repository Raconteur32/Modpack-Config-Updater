package fr.paulbrancieq.modpackconfigupdater.optiontest;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.filetypes.JsonFileContainer;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static com.ibm.icu.impl.Assert.fail;

public class JsonFileTest {
  @Test
  public void basicJsonFileTest() {
    try {
      JsonFileContainer jsonFileContainer = JsonFileContainer.fromPath(Path.of("src/test/java/fr/paulbrancieq" +
              "/modpackconfigupdater/optiontest"), Path.of("basicjsonfiletest.json"));
      // TODO: Finish testing of result (just asserts, already tested manually)
    } catch (FileNotFoundException e) {
      fail(e);
    }
  }
}
