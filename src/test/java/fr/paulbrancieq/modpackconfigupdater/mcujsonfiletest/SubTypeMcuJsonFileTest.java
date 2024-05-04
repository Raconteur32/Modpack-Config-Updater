package fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest;

import com.google.gson.JsonParseException;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.subtypetestfiles.McuFileData;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.subtypetestfiles.McuFile;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.subtypetestfiles.SubType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubTypeMcuJsonFileTest {
  @Test
  public void testFieldRetrieve() {
    McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/subtypetestfiles/basic.json");
    McuFileData mcuData = mcuFile.getData();
    SubType object = mcuData.object;
    assertEquals("test", object.id);
    assertEquals("test", object.name);
    assertEquals(1, mcuData.list.size());
    SubType listElem = mcuData.list.get(0);
    assertEquals("test", listElem.id);
  }

  @Test
  public void testMissingRequiredField() {
    assertThrows(JsonParseException.class, () -> {
      McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/subtypetestfiles/missingRequiredField.json");
    });
  }
}
