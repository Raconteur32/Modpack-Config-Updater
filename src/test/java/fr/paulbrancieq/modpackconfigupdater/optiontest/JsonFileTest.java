package fr.paulbrancieq.modpackconfigupdater.optiontest;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.filetypes.JsonFileContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.OptionContextVisitorImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.MapOption;
import fr.paulbrancieq.modpackconfigupdater.remake.path.SimpleOptionPath;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.file.Path;

import static com.ibm.icu.impl.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonFileTest {
  @Test
  public void basicJsonFileTest() {
    try {
      JsonFileContainer jsonFileContainer =
              JsonFileContainer.fromPath(Path.of("src/test/java/fr/paulbrancieq" + "/modpackconfigupdater/optiontest"),
                      Path.of("basicjsonfiletest.json"));
      MapOption mapOption = (MapOption) jsonFileContainer.getOption();
      assertEquals(BigInteger.valueOf(1),
              mapOption.getFromPath(new OptionContextVisitorImpl(), new SimpleOptionPath("integer")).get(0)
                      .getRawValue());
    } catch (FileNotFoundException e) {
      fail(e);
    }
  }
}
