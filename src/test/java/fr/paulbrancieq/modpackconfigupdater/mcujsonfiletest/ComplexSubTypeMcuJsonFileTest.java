package fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest;

import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.complexsubtypetestfiles.McuFile;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.complexsubtypetestfiles.McuFileData;
import fr.paulbrancieq.modpackconfigupdater.mcujsonfiletest.complexsubtypetestfiles.ComplexSubType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexSubTypeMcuJsonFileTest {
  @Test
  public void testFieldRetrieve() {
    McuFile mcuFile = new McuFile("src/test/java/fr/paulbrancieq/modpackconfigupdater/mcujsonfiletest/complexsubtypetestfiles/basic.json");
    McuFileData mcuData = mcuFile.getData();
    ComplexSubType object = mcuData.object;
    assertEquals("test", object.id);
    assertEquals("test", object.name);
    assertEquals("testtest", object.complexField);
    assertEquals(1, mcuData.list.size());
    ComplexSubType listElem = mcuData.list.get(0);
    assertEquals("test", listElem.id);
    assertEquals("test", listElem.name);
    assertEquals("testtest", listElem.complexField);
  }
}
