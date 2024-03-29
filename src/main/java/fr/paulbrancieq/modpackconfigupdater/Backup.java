package fr.paulbrancieq.modpackconfigupdater;

import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Backup {
  private final Path backupBasePath;
  private final Path backupSavePath;
  private final List<String> backupedFiles = new ArrayList<>();
  private final List<String> backupedFilesToDelete = new ArrayList<>();
  public Backup(String backupBasePath, String backupsPath, String backupName) {
    this.backupBasePath = Path.of(backupBasePath);
    Path backupsPathDir = Path.of(backupsPath);
    try {
      if (!backupsPathDir.toFile().isDirectory()) {
        throw new IllegalArgumentException("The backups path must be a directory.");
      }
      if (!this.backupBasePath.toFile().isDirectory()) {
        throw new IllegalArgumentException("The backup base path must be a directory.");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't verify paths for backup.", e);
    }
    backupName += "_" + java.time.LocalDateTime.now().toString().replace(':', '-');
    this.backupSavePath = backupsPathDir.resolve(backupName);
    try {
      java.nio.file.Files.createDirectory(this.backupSavePath);
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't create save path for backup.", e);
    }
  }

  public void add(@NotNull OptionPath optionPath) {
    if (this.backupedFiles.contains(optionPath.getFilePath())) {
      return;
    }
    Path filePath = this.backupBasePath.resolve(optionPath.getFilePath());
    Path backupFilePath = this.backupSavePath.resolve(optionPath.getFilePath());
    try {
      if (!filePath.toFile().exists()) {
        backupedFilesToDelete.add(optionPath.getFilePath());
        return;
      }
      if (filePath.toFile().isDirectory()) {
        FileUtils.copyDirectory(filePath.toFile(), backupFilePath.toFile());
      } else if (filePath.toFile().isFile()) {
        java.nio.file.Files.createDirectories(backupFilePath.getParent());
        FileUtils.copyFile(filePath.toFile(), backupFilePath.toFile());
      } else {
        throw new IllegalArgumentException("The file to backup must be a valid file.");
      }
      this.backupedFiles.add(optionPath.getFilePath());
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't copy the file/directory for backup.", e);
    }
  }

  public void apply() {
    try {
      for (String filePath : this.backupedFilesToDelete) {
        Path backupFilePath = this.backupSavePath.resolve(filePath);
        if (backupFilePath.toFile().isDirectory()) {
          FileUtils.deleteDirectory(backupFilePath.toFile());
        } else if (backupFilePath.toFile().isFile()) {
          java.nio.file.Files.delete(backupFilePath);
        }
      }
      FileUtils.copyDirectory(this.backupSavePath.toFile(), this.backupBasePath.toFile());
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't restore backup.", e);
    }
  }
}
