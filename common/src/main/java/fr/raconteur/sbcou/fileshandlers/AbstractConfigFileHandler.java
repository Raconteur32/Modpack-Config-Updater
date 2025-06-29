package fr.raconteur.sbcou.fileshandlers;

import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;

public abstract class AbstractConfigFileHandler {
  protected File configFile;

  public AbstractConfigFileHandler(String basePath, String relativePath) {
    String filePath = Paths.get(basePath, relativePath).toString();
    this.configFile = new File(filePath);
  }

  public abstract Object read() throws Exception;

  public abstract void write(Object value) throws IOException;
}
