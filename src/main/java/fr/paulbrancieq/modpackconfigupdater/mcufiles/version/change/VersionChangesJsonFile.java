package fr.paulbrancieq.modpackconfigupdater.mcufiles.version.change;

import fr.paulbrancieq.modpackconfigupdater.mcufiles.McuJsonFile;

public class VersionChangesJsonFile extends McuJsonFile<VersionChangesJsonData> {
  public VersionChangesJsonFile(String filePath) {
    super(filePath, VersionChangesJsonData.class);
  }
}
