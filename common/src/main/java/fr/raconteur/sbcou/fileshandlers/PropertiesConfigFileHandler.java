package fr.raconteur.sbcou.fileshandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.poiu.apron.PropertyFile;
import de.poiu.apron.entry.BasicEntry;
import de.poiu.apron.entry.Entry;
import de.poiu.apron.entry.PropertyEntry;
import fr.raconteur.sbcou.fileshandlers.types.StrictlyComparableMap;

public class PropertiesConfigFileHandler extends AbstractConfigFileHandler {
  private final String relativePath;
  private static Map<String, PropertiesWriteConfig> propertiesWriteConfigMap = new HashMap<>();

  public PropertiesConfigFileHandler(String basePath, String relativePath) {
    super(basePath, relativePath);
    this.relativePath = relativePath;
  }

  @Override
  public Map<String, Object> read() {
    PropertyFile propertyFile = PropertyFile.from(configFile);
    // All entries
    List<Entry> entries = propertyFile.getAllEntries();
    // List of PropertyEntry
    List<PropertyEntry> propertyEntries = entries.stream()
            .filter(entry -> entry instanceof PropertyEntry)
            .map(entry -> (PropertyEntry) entry)
            .toList();
    // Separator memory
    PropertiesWriteConfig propertiesWriteConfig = new PropertiesWriteConfig();
    for (PropertyEntry propertyEntry : propertyEntries) {
      propertiesWriteConfig.separatorMap.put(propertyEntry.getKey().toString(),
              propertyEntry.getSeparator().toString());
    }
    // Basic entries memory
    List<BasicEntry> bufferList = new ArrayList<>();
    for (int i = 0; i < entries.size(); i++) {
      Entry entry = entries.get(i);
      if (!(entry instanceof BasicEntry basicEntry)) {
        continue;
      }
      if (i == entries.size() - 1) {
        bufferList.add(basicEntry);
        propertiesWriteConfig.basicEntriesMap.put("sbcou:endofile", bufferList);
      } else if (entries.get(i + 1) instanceof BasicEntry) {
        bufferList.add(basicEntry);
      } else if (entries.get(i + 1) instanceof PropertyEntry) {
        bufferList.add(basicEntry);
        propertiesWriteConfig.basicEntriesMap.put(((PropertyEntry) entries.get(i + 1)).getKey().toString(), bufferList);
        bufferList = new ArrayList<>();
      }
    }
    propertiesWriteConfigMap.put(relativePath, propertiesWriteConfig);
    return propertyEntries.stream()
            .collect(StrictlyComparableMap::new, (m, e) -> m.put(e.getKey().toString(), e.getValue().toString()), StrictlyComparableMap::putAll);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void write(Object value) throws IOException {
    PropertyFile propertyFile = new PropertyFile();
    if (!(value instanceof Map)) {
      throw new IllegalArgumentException("Value must be a Map");
    }
    PropertiesWriteConfig propertiesWriteConfig = propertiesWriteConfigMap.get(relativePath);
    propertiesWriteConfig = propertiesWriteConfig == null ? new PropertiesWriteConfig() : propertiesWriteConfig;
    Map<String, String> separatorMap = propertiesWriteConfig.separatorMap;
    Map<String, List<BasicEntry>> basicEntriesMap = propertiesWriteConfig.basicEntriesMap;
    Map<String, Object> map = (Map<String, Object>) value;
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String separator = separatorMap.get(entry.getKey()) != null ? separatorMap.get(entry.getKey()) : "=";
      List<BasicEntry> basicEntries =
              basicEntriesMap.get(entry.getKey()) == null ? new ArrayList<>() : basicEntriesMap.get(entry.getKey());
      if (!basicEntries.isEmpty()) {
        for (BasicEntry basicEntry : basicEntries) {
          propertyFile.appendEntry(basicEntry);
        }
      }
      propertyFile.appendEntry(new PropertyEntry("", entry.getKey(), separator, entry.getValue().toString(),
              "\n"));
      boolean ignored = configFile.createNewFile();
      propertyFile.update(configFile);
    }
  }

  private static class PropertiesWriteConfig {
    Map<String, String> separatorMap = new HashMap<>();
    Map<String, List<BasicEntry>> basicEntriesMap = new HashMap<>();
  }
}
