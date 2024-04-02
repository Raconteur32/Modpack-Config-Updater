package fr.paulbrancieq.modpackconfigupdater;

import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.options.context.OptionFileLoader;
import fr.paulbrancieq.modpackconfigupdater.options.type.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OptionFileLoaderTest {
  Path currentPath = Paths.get("").toAbsolutePath();
  @Test
  public void testLoad() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "test1.json", null);
    assertTrue(optional.isPresent());
    JsonMapOptionFile optionFile = (JsonMapOptionFile) optional.get();
    assertEquals("test", optionFile.getValue().get("test").getValue());
  }

  @Test
  public void testLoad2() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "test2.json", null);
    assertTrue(optional.isPresent());
    JsonMapOptionFile optionFile = (JsonMapOptionFile) optional.get();
    // get object
    Option<?> objectO = optionFile.getValue().get("object");
    // verify it's a JsonMapOptionFile
    assertInstanceOf(MapOption.class, objectO);
    // cast it to JsonMapOptionFile
    MapOption object = (MapOption) objectO;
    // verify the value of the object
    assertEquals("string", object.getValue().get("string").getValue());
    assertEquals(1, ((LongOption)object.getValue().get("number")).getValue());
    assertEquals(true, object.getValue().get("boolean").getValue());
    assertNull(object.getValue().get("null").getValue());
    // array
    ListOption array = (ListOption) object.getValue().get("array");
    assertInstanceOf(ListOption.class, array);
    assertEquals("string", array.getValue().get(0).getValue());
    assertEquals(1, ((LongOption)array.getValue().get(1)).getValue());
  }
}
