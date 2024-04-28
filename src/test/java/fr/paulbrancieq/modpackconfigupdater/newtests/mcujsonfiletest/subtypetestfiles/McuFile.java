package fr.paulbrancieq.modpackconfigupdater.newtests.mcujsonfiletest.subtypetestfiles;

import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.McuJsonFile;

public class McuFile extends McuJsonFile<McuFileData> {
  public McuFile(String filePath) {
    super(filePath, McuFileData.class);
  }
}
