package fr.paulbrancieq.modpackconfigupdater.optiontest;

import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainerBasicImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.ListOption;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection.MapOption;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicsOptionContainersTest {
  @Test
  public void stringOptionFromSerializableTest() {
    OptionContainer container = new OptionContainerBasicImpl("test", null, "pathPart");
    Option<?> option = container.getOption();
    assertEquals("test", option.getRawValue());
  }

  @Test
  public void listOptionFromSerializableTest() {
    OptionContainer container = new OptionContainerBasicImpl(List.of("test1", "test2", "test3"), null, "pathPart");
    ListOption option = (ListOption) container.getOption();
    assertEquals(option.getRawValue().get(0).getOption().getRawValue(), "test1");
    assertEquals(option.getRawValue().get(1).getOption().getRawValue(), "test2");
    assertEquals(option.getRawValue().get(2).getOption().getRawValue(), "test3");
  }

  @Test
  public void mapOptionFromSerializableTest() {
    OptionContainer container =
            new OptionContainerBasicImpl(Map.of("KTest1", "VTest1", "KTest2", "VTest2", "KTest3", "VTest3"), null,
                    "pathPart");
    MapOption option = (MapOption) container.getOption();
    Map<String, OptionContainer> value = option.getRawValue();
    assertEquals(value.get("KTest1").getOption().getRawValue(), "VTest1");
    assertEquals(value.get("KTest2").getOption().getRawValue(), "VTest2");
    assertEquals(value.get("KTest3").getOption().getRawValue(), "VTest3");
  }
}
