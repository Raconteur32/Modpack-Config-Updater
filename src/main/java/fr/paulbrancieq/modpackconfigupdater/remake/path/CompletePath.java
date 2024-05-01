package fr.paulbrancieq.modpackconfigupdater.remake.path;

import java.nio.file.Path;

public class CompletePath {
  private final InFileOptionPath inFileOptionPath;
  private final Path filePath;

  public CompletePath(InFileOptionPath inFileOptionPath, Path filePath) {
    this.inFileOptionPath = inFileOptionPath;
    this.filePath = filePath;
  }
}
