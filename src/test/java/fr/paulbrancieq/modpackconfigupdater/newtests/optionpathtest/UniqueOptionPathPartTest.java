package fr.paulbrancieq.modpackconfigupdater.newtests.optionpathtest;

import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UniqueOptionPathPartTest {
  @Test
  public void testFromMultiPartStringBasic() {
    List<UniqueOptionPathPart> uniqueOptionPathParts = UniqueOptionPathPart.fromMultiPartString("ab.cd.ef.gh.ij");
    assert uniqueOptionPathParts.size() == 5;
    assert uniqueOptionPathParts.get(0).getStringPathPart().equals("ab");
    assert uniqueOptionPathParts.get(1).getStringPathPart().equals("cd");
    assert uniqueOptionPathParts.get(2).getStringPathPart().equals("ef");
  }

  @Test
  public void testFromMultiPartStringEscaped() {
    List<UniqueOptionPathPart> uniqueOptionPathParts = UniqueOptionPathPart.fromMultiPartString("ab\\\\.cd\\.ef.gh\\\\\\.ij");
    assert uniqueOptionPathParts.size() == 3;
    assert uniqueOptionPathParts.get(0).getStringPathPart().equals("ab\\\\"); // Pair number of '\', so it's an escaped '\' and the split happens
    assert uniqueOptionPathParts.get(1).getStringPathPart().equals("cd\\.ef"); // Single '\', so it's not an escaped '\', and the dot is escaped
    assert uniqueOptionPathParts.get(2).getStringPathPart().equals("gh\\\\\\.ij"); // Odd number of '\', so it's  not an escaped '\', and the dot is escaped
  }
}
