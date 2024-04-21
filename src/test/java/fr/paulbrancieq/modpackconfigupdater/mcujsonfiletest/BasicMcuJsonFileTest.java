package fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest;

import com.google.gson.JsonParseException;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.basictestfiles.McuFileData;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.basictestfiles.McuFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicMcuJsonFileTest {
  @Test
  public void testFieldRetrieve() {
    McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/basictestfiles/basic.json");
    McuFileData mcuData = mcuFile.getData();
    assertEquals("testId", mcuData.getId());
    assertEquals("testName", mcuData.getName());
  }

  @Test
  public void testMissingVersionField() {
    assertThrows(JsonParseException.class, () -> {
      McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/basictestfiles/missingVersionField.json");
    });
  }

  @Test
  public void testMissingRequiredField() {
    assertThrows(JsonParseException.class, () -> {
      McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/basictestfiles/missingRequiredField.json");
    });
  }

  @Test
  public void testMissingOptionalField() {
    McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/basictestfiles/missingOptionalField.json");
    McuFileData mcuData = mcuFile.getData();
    assertEquals("test", mcuData.getId());
    assertNull(mcuData.getName());
  }
}
