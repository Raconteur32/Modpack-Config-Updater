package fr.paulbrancieq.modpackconfigupdater.newtests.optiontest;

import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainerBasicImpl;
import org.junit.jupiter.api.Test;

public class NonCollectionOptionContainersTest {
  @Test
  public void StringOptionFromSerializableTest() {
    OptionContainer container = new OptionContainerBasicImpl("test", null, "pathPart");
    Option<?> option = container.getOption();
    assert option.getRawValue().equals("test");
  }
}
