package fr.raconteur.sbcou.fileshandlers;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;

public class UnparsableFileHandler extends AbstractConfigFileHandler {

  public UnparsableFileHandler(String basePath, String relativePath) {
    super(basePath, relativePath);
  }

  @Override
  public Object read() throws Exception {
    return ArrayUtils.toObject(Files.readAllBytes(configFile.toPath()));
  }

  @Override
  public void write(Object value) throws IOException {
    if (!(value instanceof Byte[] bytes)) {
      throw new IllegalArgumentException("Value must be a byte array");
    }
    Files.write(configFile.toPath(), ArrayUtils.toPrimitive(bytes));
  }
}
