package fr.paulbrancieq.modpackconfigupdater.mcufiles.version.actual_version;

import fr.paulbrancieq.modpackconfigupdater.mcufiles.McuJsonFile;

public class ActualVersionJsonDataFile extends McuJsonFile<ActualVersionJsonData> {
  public ActualVersionJsonDataFile(String filePath) {
    super(filePath, ActualVersionJsonData.class);
  }
}
