package fr.paulbrancieq.modpackconfigupdater.mcufiles.version.tree;

import fr.paulbrancieq.modpackconfigupdater.mcufiles.McuJsonFile;

public class VersionsTreeJsonDataFile extends McuJsonFile<VersionsTreeJsonData> {
  public VersionsTreeJsonDataFile(String filePath) {
    super(filePath, VersionsTreeJsonData.class);
  }
}
