package fr.paulbrancieq.modpackconfigupdater.optionreftest;

import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleOptionPathPartTest {
  @Test
  public void testFromMultiPartStringBasic() {
    List<SimpleOptionPathPart> uniqueOptionPathParts = SimpleOptionPathPart.fromMultiPartString("ab.cd.ef.gh.ij");
    assertEquals(uniqueOptionPathParts.size(), 5);
    assertEquals(uniqueOptionPathParts.get(0).getStringPathPart(), "ab");
    assertEquals(uniqueOptionPathParts.get(1).getStringPathPart(), "cd");
    assertEquals(uniqueOptionPathParts.get(2).getStringPathPart(), "ef");
    assertEquals(uniqueOptionPathParts.get(3).getStringPathPart(), "gh");
    assertEquals(uniqueOptionPathParts.get(4).getStringPathPart(), "ij");
  }

  @Test
  public void testFromMultiPartStringEscaped() {
    List<SimpleOptionPathPart> uniqueOptionPathParts = SimpleOptionPathPart.fromMultiPartString("ab\\\\.cd\\.ef.gh\\\\\\.ij");
    assertEquals(uniqueOptionPathParts.size(), 3);
    assertEquals(uniqueOptionPathParts.get(0).getStringPathPart(), "ab\\\\"); // Pair number of '\', so it's an escaped '\' and the split happens
    assertEquals(uniqueOptionPathParts.get(1).getStringPathPart(), "cd\\.ef"); // Single '\', so it's not an escaped '\', and the dot is escaped
    assertEquals(uniqueOptionPathParts.get(2).getStringPathPart(), "gh\\\\\\.ij"); // Odd number of '\', so it's  not an escaped '\', and the dot is escaped
  }
}
