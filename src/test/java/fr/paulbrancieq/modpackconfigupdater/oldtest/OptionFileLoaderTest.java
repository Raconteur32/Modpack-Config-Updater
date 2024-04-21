package fr.paulbrancieq.modpackconfigupdater.oldtest;

import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.options.context.OptionFileLoader;
import fr.paulbrancieq.modpackconfigupdater.options.type.*;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OptionFileLoaderTest {
  Path currentPath = Paths.get("").toAbsolutePath();

  @Test
  public void testLoad() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "basic.json", null);
    assertTrue(optional.isPresent());
    JsonMapOptionFile optionFile = (JsonMapOptionFile) optional.get();
    assertEquals("test", optionFile.getValue().get("test").getValue());
  }

  @Test
  public void testLoad2() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "missingVersionField.json", null);
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
    assertEquals(1, ((LongOption) object.getValue().get("number")).getValue());
    assertEquals(true, object.getValue().get("boolean").getValue());
    assertNull(object.getValue().get("null").getValue());
    // array
    ListOption array = (ListOption) object.getValue().get("array");
    assertInstanceOf(ListOption.class, array);
    assertEquals("string", array.getValue().get(0).getValue());
    assertEquals(1, ((LongOption) array.getValue().get(1)).getValue());
  }

  @Test
  public void testFindSubOptionsWithOptionPath1() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "test3.json", null);
    assertTrue(optional.isPresent());
    JsonMapOptionFile optionFile = (JsonMapOptionFile) optional.get();
    OptionPath optionPath = optionFile.getOptionPath().getSubPath("linearObject1")
        .getSubPath("oneway").getSubPath("oneway").getSubPath("string");
    try {
      List<Option<?>> subOptions = optionFile.getSubOptions(optionPath);
      assertEquals(1, subOptions.size());
      assertEquals("string", subOptions.get(0).getValue());
    } catch (OptionException.OptionDoesNotHaveChildren e) {
      fail(e);
    }
  }

  @Test
  public void testFindSubOptionsWithOptionPath2() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "test3.json", null);
    assertTrue(optional.isPresent());
    JsonMapOptionFile optionFile = (JsonMapOptionFile) optional.get();
    OptionPath optionPath = optionFile.getOptionPath().getSubPath("mixObjectAndList1")
        .getSubPath(0).getSubPath("string");
    try {
      List<Option<?>> subOptions = optionFile.getSubOptions(optionPath);
      assertEquals(1, subOptions.size());
      assertEquals("string", subOptions.get(0).getValue());
    } catch (OptionException.OptionDoesNotHaveChildren e) {
      fail(e);
    }
  }

  @Test
  public void testFindSubOptionsWithOptionPath3() {
    OptionFileLoader.Json jsonLoader = new OptionFileLoader.Json();
    Optional<Option<?>> optional = jsonLoader.tryLoad(Path.of(currentPath.toString(), "src/test/java/fr/paulbrancieq/modpackconfigupdater/optionFileLoaderTest/").toString(), "test3.json", null);
    assertTrue(optional.isPresent());
    JsonMapOptionFile optionFile = (JsonMapOptionFile) optional.get();
    OptionPath optionPath = optionFile.getOptionPath().getSubPath("filterList1")
        .getSubPath("[\"mcu_index\";1__3]");
    try {
      List<Option<?>> subOptions = optionFile.getSubOptions(optionPath);
      assertEquals(3, subOptions.size());
      assertEquals("string2", subOptions.get(0).getValue());
      assertEquals("string3", subOptions.get(1).getValue());
      assertEquals("string4", subOptions.get(2).getValue());
    } catch (OptionException.OptionDoesNotHaveChildren e) {
      fail(e);
    }
    optionPath = optionFile.getOptionPath().getSubPath("filterList1")
        .getSubPath("[\"mcu_value\";\"string[1-4]\"]");
    try {
      List<Option<?>> subOptions = optionFile.getSubOptions(optionPath);
      assertEquals(4, subOptions.size());
      assertEquals("string1", subOptions.get(0).getValue());
      assertEquals("string2", subOptions.get(1).getValue());
      assertEquals("string3", subOptions.get(2).getValue());
      assertEquals("string4", subOptions.get(3).getValue());
    } catch (OptionException.OptionDoesNotHaveChildren e) {
      fail(e);
    }
  }
}
