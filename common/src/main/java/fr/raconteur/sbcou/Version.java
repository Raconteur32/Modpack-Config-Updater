package fr.raconteur.sbcou;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.raconteur.sbcou.exceptions.SbcouException;
import fr.raconteur.sbcou.platform.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Version {
  private final String id;
  private static List<Version> versions;
  private ConfigContext context;

  private Version(String id) throws VersionException {
    this.id = id;
    verifyVersionDirectory();
    addToVersionsJson();
  }

  public static Version createVersion(String id) throws VersionException {
    Services.PLATFORM.logInfo("Initialize new version " + id);
    return new Version(id);
  }

  public String getId() {
    return id;
  }

  public List<String> getOverrides() throws VersionException {
    String versionDir = Constants.VERSIONS_DIR + "/" + id;
    File overrideFile = new File(versionDir, "override.json");
    String jsonContent;
    try {
      jsonContent = Files.readString(Paths.get(overrideFile.getPath()));
    } catch (IOException e) {
      throw new VersionException("Failed to read override.json", e);
    }
    return new Gson().fromJson(jsonContent, new TypeToken<List<String>>() {
    }.getType());
  }

  public List<String> getDeletes() throws VersionException {
    String versionDir = Constants.VERSIONS_DIR + "/" + id;
    File deleteFile = new File(versionDir, "delete.json");
    String jsonContent;
    try {
      jsonContent = Files.readString(Paths.get(deleteFile.getPath()));
    } catch (IOException e) {
      throw new VersionException("Failed to read delete.json", e);
    }
    return new Gson().fromJson(jsonContent, new TypeToken<List<String>>() {
    }.getType());
  }

  public ConfigContext getContext() throws SbcouException {
    if (context == null) {
      reloadContext();
    }
    return context;
  }

  public void reloadContext() throws SbcouException {
    String versionDir = Constants.VERSIONS_DIR + "/" + id + "/context";
    context = new ConfigContext(versionDir);
  }

  public void setOverrides(List<String> overrides) throws IOException {
    String versionDir = Constants.VERSIONS_DIR + "/" + id;
    File overrideFile = new File(versionDir, "override.json");
    String json = new GsonBuilder().setPrettyPrinting().create().toJson(overrides);
    Files.writeString(Paths.get(overrideFile.getPath()), json);
  }

  public void setDeletes(List<String> deletes) throws IOException {
    String versionDir = Constants.VERSIONS_DIR + "/" + id;
    File deleteFile = new File(versionDir, "delete.json");
    String json = new GsonBuilder().setPrettyPrinting().create().toJson(deletes);
    Files.writeString(Paths.get(deleteFile.getPath()), json);
  }

  public List<String> getNeededUpdates() throws VersionException {
    List<String> versions = listVersionsString();
    int currentIndex = versions.indexOf(id);
    if (currentIndex == -1 || currentIndex == versions.size() - 1) {
      return List.of();
    }
    return versions.subList(currentIndex + 1, versions.size());
  }

  private void addToVersionsJson() throws VersionException {
    Path path = Paths.get(Constants.VERSIONS_FILE);
    List<String> versions = listVersionsString();
    if (!versions.contains(id)) {
      versions.add(id);
      String prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(versions);
      try {
        Files.write(path, prettyJson.getBytes());
      } catch (IOException e) {
        throw new VersionException("Failed to write to versions.json", e);
      }
    }
  }

  private void verifyVersionDirectory() throws VersionException {
    File versionDir = new File(Constants.VERSIONS_DIR, id);
    if (!versionDir.exists()) {
      boolean ignored = versionDir.mkdirs();
    }
    File contextDir = new File(versionDir, "context");
    if (!contextDir.exists()) {
      boolean ignored = contextDir.mkdirs();
    }
    File overrideFile = new File(versionDir, "override.json");
    if (!overrideFile.exists() || overrideFile.length() == 0) {
      try {
        Files.write(Paths.get(overrideFile.getPath()), "[]".getBytes());
      } catch (IOException e) {
        throw new VersionException("Failed to create override.json", e);
      }
    }
    File deleteFile = new File(versionDir, "delete.json");
    if (!deleteFile.exists() || deleteFile.length() == 0) {
      try {
        Files.write(Paths.get(deleteFile.getPath()), "[]".getBytes());
      } catch (IOException e) {
        throw new VersionException("Failed to create delete.json", e);
      }
    }
  }

  public static void verifyVersionsSpecsFiles() throws VersionException {
    Path path = Paths.get(Constants.VERSIONS_FILE);
    Path current_path = Paths.get(Constants.CURRENT_VERSION_FILE);
    try {
      if (!Files.exists(path) || Files.size(path) == 0) {
        List<String> versions = List.of("root");
        String prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(versions);
        Files.write(path, prettyJson.getBytes());
        new Version("root");
      }
      if (!Files.exists(current_path) || Files.size(current_path) == 0) {
        Files.write(current_path, "root".getBytes());
      }
    } catch (IOException e) {
      throw new VersionException("Failed to verify root version", e);
    }
  }

  public static Version getCurrent() throws IOException, VersionException {
    String currentVersion = Files.readString(Paths.get(Constants.CURRENT_VERSION_FILE)).trim();
    List<String> versions = listVersionsString();
    if (!versions.contains(currentVersion)) {
      throw new VersionNotFoundException("Current version " + currentVersion + " is not in versions.json");
    }
    return new Version(currentVersion);
  }

  public static Version get(String id) throws VersionException {
    Optional<Version> optional = versions.stream().filter(version -> version.id.equals(id)).findAny();
    if (optional.isEmpty()) {
      throw new VersionNotFoundException("Version " + id + " not found in versions.json");
    }
    return optional.get();
  }

  public static List<String> listVersionsString() throws VersionException {
    try {
      return new Gson().fromJson(Files.readString(Paths.get(Constants.VERSIONS_FILE)), new TypeToken<List<String>>() {
      }.getType());
    } catch (IOException e) {
      throw new VersionException("Failed to read versions.json", e);
    }
  }

  public static List<Version> listVersions() throws VersionException {
    if (versions == null || versions.isEmpty()) {
      reloadListVersions();
    }
    return versions;
  }

  public static void reloadListVersions() throws VersionException {
    versions = new ArrayList<>();
    for (String id : listVersionsString()) {
      try {
        versions.add(new Version(id));
      } catch (VersionException e) {
        throw new VersionException("Failed to create Version object for id: " + id, e);
      }
    }
  }

  public static class VersionException extends SbcouException {
    public VersionException(String message) {
      super(message);
    }

    public VersionException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  public static class VersionNotFoundException extends VersionException {
    public VersionNotFoundException(String message) {
      super(message);
    }
  }

  public static void setCurrent(String version) throws IOException {
    Files.writeString(Paths.get(Constants.CURRENT_VERSION_FILE), version);

  }
}
