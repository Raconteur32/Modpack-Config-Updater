package fr.paulbrancieq.modpackconfigupdater;

import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OptionsPathTest {
  public String separator = FileSystems.getDefault().getSeparator();

  @Test
  public void basicOptionPath() {
    OptionPath optionPath = new OptionPath("test/test.json:map.map2.list.1");
    assertEquals("test" + separator + "test.json", optionPath.getFilePath());
    assertEquals(4, optionPath.getInFileOptionPathParts().size());
    List<String> expected = List.of("map", "map2", "list", "1");
    for (int i = 0; i < optionPath.getInFileOptionPathParts().size(); i++) {
      assertEquals(expected.get(i), optionPath.getInFileOptionPathParts().get(i).getPartName());
      assertFalse(optionPath.getInFileOptionPathParts().get(i).haveFilters());
    }
  }

  @Test
  public void optionPathWithOneFilter() {
    OptionPath optionPath = new OptionPath("test/test.json:map[42]");
    assertEquals("test" + separator + "test.json", optionPath.getFilePath());
    assertEquals(1, optionPath.getInFileOptionPathParts().size());
    assertEquals("map", optionPath.getInFileOptionPathParts().get(0).getPartName());
    assertTrue(optionPath.getInFileOptionPathParts().get(0).haveFilters());
    assertEquals(1, optionPath.getInFileOptionPathParts().get(0).getFilters().size());
  }
}
