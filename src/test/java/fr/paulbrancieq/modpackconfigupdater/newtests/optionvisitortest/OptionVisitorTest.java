package fr.paulbrancieq.modpackconfigupdater.newtests.optionvisitortest;

import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainerBasicImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.OptionContextVisitor;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.OptionContextVisitorImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.MapOption;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPath;
import fr.paulbrancieq.modpackconfigupdater.remake.path.SimpleOptionPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionVisitorTest {
  @Test
  public void test3LevelHierarchyNoFilter() {
    Map<String, Object> testMap = getStringObjectMap();
    OptionContextVisitor visitorTest = new OptionContextVisitorImpl();
    OptionPath path = new SimpleOptionPath("a.0.a");
    MapOption mapOption = MapOption.fromSerializable(testMap, new OptionContainerBasicImpl(testMap, null, "path"));
    List<Option<?>> result = mapOption.getFromPath(visitorTest, path);
    assertEquals(1, result.size());
    assertEquals(result.get(0).getRawValue(), "a");
  }

  @NotNull
  private static Map<String, Object> getStringObjectMap() {
    Map<String, Object> testMap = new HashMap<>();
    List<Object> subTestList = new ArrayList<>();
    Map<String, Object> subSubTestMapA = new HashMap<>();
    subSubTestMapA.put("a", "a");
    subSubTestMapA.put("b", "b");
    subTestList.add(subSubTestMapA);
    Map<String, Object> subSubTestMapB = new HashMap<>();
    subSubTestMapB.put("c", "c");
    subSubTestMapB.put("d", "d");
    subTestList.add(subSubTestMapB);
    testMap.put("a", subTestList);
    testMap.put("b", subTestList);
    return testMap;
  }
}
