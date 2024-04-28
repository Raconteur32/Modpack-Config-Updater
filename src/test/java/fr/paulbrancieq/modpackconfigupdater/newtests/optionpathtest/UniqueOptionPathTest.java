package fr.paulbrancieq.modpackconfigupdater.newtests.optionpathtest;

import fr.paulbrancieq.modpackconfigupdater.remake.path.InFileUniqueOptionPath;
import org.junit.jupiter.api.Test;

public class UniqueOptionPathTest {
  @Test
  public void testFromMultiPartStringBasic() {
    InFileUniqueOptionPath uniqueOptionPath = new InFileUniqueOptionPath("ab.cd.ef.gh.ij");
    assert uniqueOptionPath.getOptionPathParts().size() == 5;
    assert uniqueOptionPath.getOptionPathParts().get(0).getStringPathPart().equals("ab");
    assert uniqueOptionPath.getOptionPathParts().get(1).getStringPathPart().equals("cd");
    assert uniqueOptionPath.getOptionPathParts().get(2).getStringPathPart().equals("ef");
    assert uniqueOptionPath.getOptionPathParts().get(3).getStringPathPart().equals("gh");
    assert uniqueOptionPath.getOptionPathParts().get(4).getStringPathPart().equals("ij");
    assert uniqueOptionPath.getPathString().equals("ab.cd.ef.gh.ij");
  }

  @Test
  public void testBasicMatch() {
    InFileUniqueOptionPath uniqueOptionPath1 = new InFileUniqueOptionPath("ab.cd.ef.gh.ij");
    InFileUniqueOptionPath uniqueOptionPath2 = new InFileUniqueOptionPath("ab.cd.ef.gh.ij");
    assert uniqueOptionPath1.match(uniqueOptionPath2);
    InFileUniqueOptionPath uniqueOptionPath3 = new InFileUniqueOptionPath("ab.cd.ef.gh.aj");
    assert !uniqueOptionPath1.match(uniqueOptionPath3);
  }
}
