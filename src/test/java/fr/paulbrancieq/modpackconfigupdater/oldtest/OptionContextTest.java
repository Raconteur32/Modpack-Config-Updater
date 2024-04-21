package fr.paulbrancieq.modpackconfigupdater.oldtest;

import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.options.context.OptionContext;
import fr.paulbrancieq.modpackconfigupdater.options.type.ListOption;
import fr.paulbrancieq.modpackconfigupdater.options.type.MapOption;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OptionContextTest {
  @Test
  public void testLoadOptionContextJsonMap() {
    OptionContext optionContext = new OptionContext("D:\\Workspace\\minecraft\\mods\\modpack-configuration-updater\\src\\test\\java\\fr\\paulbrancieq\\modpackconfigupdater\\optionContextTest", null, List.of());
    List<Option<?>> optionList = null;
    try {
      optionList = optionContext.getOptionsFromOptionPath(new OptionPath("basic.json:"));
    } catch (Exception e) {
      fail(e);
    }
    assertEquals(1, optionList.size());
    MapOption mapOption = null;
    try {
      mapOption = optionList.get(0).as(MapOption.class);
    } catch (OptionException.OptionCantBeCasted e) {
      fail(e);
    }
  }

  @Test
  public void testLoadOptionContextJsonList() {
    OptionContext optionContext = new OptionContext("D:\\Workspace\\minecraft\\mods\\modpack-configuration-updater\\src\\test\\java\\fr\\paulbrancieq\\modpackconfigupdater\\optionContextTest", null, List.of());
    List<Option<?>> optionList = null;
    // get file option
    try {
      optionList = optionContext.getOptionsFromOptionPath(new OptionPath("missingVersionField.json:"));
    } catch (Exception e) {
      fail(e);
    }
    assertEquals(1, optionList.size());
    ListOption listOption = null;
    try {
      listOption = optionList.get(0).as(ListOption.class);
    } catch (OptionException.OptionCantBeCasted e) {
      fail(e);
    }
    assertEquals(6, listOption.getValue().size());
    // get filtered options
    try {
      optionList = optionContext.getOptionsFromOptionPath(new OptionPath("missingVersionField.json:[\"mcu_value\";\"test[2-4]\",\"mcu_index\";2__4]"));
    } catch (Exception e) {
      fail(e);
    }
    assertEquals(2, optionList.size());
  }
}
