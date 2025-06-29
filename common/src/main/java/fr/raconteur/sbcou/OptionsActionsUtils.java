package fr.raconteur.sbcou;

import com.google.gson.stream.JsonReader;
import fr.raconteur.sbcou.exceptions.OptionsActionsUtilsException;
import fr.raconteur.sbcou.exceptions.SbcouException;
import fr.raconteur.sbcou.fileshandlers.CustomizedObjectTypeAdapter;
import fr.raconteur.sbcou.flatobject.FlatObject;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.raconteur.sbcou.platform.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionsActionsUtils {
  public static ConfigContext createMainContextObject() throws SbcouException {
    return new ConfigContext(Services.PLATFORM.getMinecraftDirectory().toString());
  }

  public static FlatObject generateMainContextFlatObject() throws SbcouException {
    return createMainContextObject().generateContextFlatObject();
  }

  public static void initDevDirectory() throws SbcouException {
    File devDir = new File(Constants.DEV_DIR);
    if (!devDir.exists()) {
      boolean ignored = devDir.mkdirs();
    }
  }

  private static void verifyEditorMemory() throws OptionsActionsUtilsException {
    File editorMemoryJson = new File(Constants.DEV_EDITOR_MEMORY_JSON);
    if (!editorMemoryJson.exists() || editorMemoryJson.length() == 0) {
      try {
        Files.write(editorMemoryJson.toPath(), "{}".getBytes());
      } catch (IOException e) {
        throw new OptionsActionsUtilsException("Failed to create editor_memory.json", e);
      }
    } else {
      Map<String, Object> editorMemory;
      try {
        editorMemory = new Gson().fromJson(Files.readString(editorMemoryJson.toPath()),
                new TypeToken<Map<String, Object>>() {
                }.getType());
      } catch (IOException e) {
        throw new OptionsActionsUtilsException("Failed to read editor_memory.json", e);
      }
      if (editorMemory == null) {
        throw new OptionsActionsUtilsException("Failed to parse editor_memory.json");
      }
    }
  }

  public static Map<String, String> getEditorMemory() throws OptionsActionsUtilsException {
    try {
      verifyEditorMemory();
      return new Gson().fromJson(Files.readString(Paths.get(Constants.DEV_EDITOR_MEMORY_JSON)),
              new TypeToken<Map<String, String>>() {
              }.getType());
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to read editor_memory.json", e);
    }
  }

  public static void saveEditorMemory(Map<String, String> editorMemory) throws IOException {
    String json = new GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(editorMemory);
    Files.writeString(Paths.get(Constants.DEV_EDITOR_MEMORY_JSON), json);
  }

  public static void resetEditorMemory() throws IOException {
    Files.write(Paths.get(Constants.DEV_EDITOR_MEMORY_JSON), "{}".getBytes());
  }

  public static void verifyFullContextDirectory() throws SbcouException {
    File devFullContextDir = new File(Constants.DEV_FULL_CONTEXT_DIR);
    if (!devFullContextDir.exists()) {
      boolean ignored = devFullContextDir.mkdirs();
    }
  }

  public static void generateFullContextFromVersions(ConfigContext devFullContext) throws SbcouException {
    verifyFullContextDirectory();
    List<Version> versions = Version.listVersions();

    for (Version version : versions) {
      devFullContext.update(version);
    }
  }

  public static ConfigContext createDevFullContext() throws SbcouException {
    verifyFullContextDirectory();
    return new ConfigContext(Constants.DEV_FULL_CONTEXT_DIR);
  }

  public static String getSbcouConfigDir() {
    return Services.PLATFORM.getMinecraftDirectory() + "/config/sbcou";
  }

  public static boolean isDevInstance() {
    File devDir = new File(Constants.DEV_DIR);
    return devDir.exists();
  }

  private static void verifyIgnoredOptionPathsFile() throws OptionsActionsUtilsException {
    File ignoredOptionPathsFile = new File(Constants.IGNORED_OPTION_PATHS_FILE);
    if (!ignoredOptionPathsFile.exists() || ignoredOptionPathsFile.length() == 0) {
      try {
        Files.write(ignoredOptionPathsFile.toPath(), "[]".getBytes(), StandardOpenOption.CREATE);
      } catch (IOException e) {
        throw new OptionsActionsUtilsException("Failed to create or verify ignored_option_paths.json", e);
      }
    }
  }

  public static List<String> getIgnoredOptionPaths() throws OptionsActionsUtilsException {
    verifyIgnoredOptionPathsFile();
    try {
      List<String> rawResults = new Gson().fromJson(Files.readString(Paths.get(Constants.IGNORED_OPTION_PATHS_FILE)),
              new TypeToken<List<String>>() {}.getType());
      List<String> results = new ArrayList<>();
      for (String item : rawResults) {
        results.add((new File(item)).getCanonicalPath());
      }
      return results;
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to read ignored_option_paths.json", e);
    }
  }

  public static void saveIgnoredOptionPaths(List<String> ignoredOptionPaths) throws OptionsActionsUtilsException {
    verifyIgnoredOptionPathsFile();
    try {
      String json = new GsonBuilder()
              .setPrettyPrinting()
              .create()
              .toJson(ignoredOptionPaths);
      Files.writeString(Paths.get(Constants.IGNORED_OPTION_PATHS_FILE), json);
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to write ignored_option_paths.json", e);
    }
  }

  private static void verifyIgnoredChangeFile() throws OptionsActionsUtilsException {
    File ignoredChangeFile = new File(Constants.IGNORED_CHANGE_FILE);
    if (!ignoredChangeFile.exists() || ignoredChangeFile.length() == 0) {
      try {
        Files.write(ignoredChangeFile.toPath(), "{}".getBytes(), StandardOpenOption.CREATE);
      } catch (IOException e) {
        throw new OptionsActionsUtilsException("Failed to create or verify ignored_change.json", e);
      }
    }
  }

  public static Map<String, Object> getIgnoredChange() throws OptionsActionsUtilsException {
    verifyIgnoredChangeFile();
    try (BufferedReader reader = new BufferedReader(new FileReader(Constants.IGNORED_CHANGE_FILE))) {
      //return new Gson().fromJson(Files.readString(Paths.get(Constants.IGNORED_CHANGE_FILE)),
      //        new TypeToken<Map<String, Object>>() {}.getType());
      GsonBuilder gsonBuilder = new GsonBuilder();
      Gson gson = gsonBuilder.create();
      JsonReader jsonReader = gson.newJsonReader(reader);
      Object result = (new CustomizedObjectTypeAdapter()).read(jsonReader);
      if (!(result instanceof Map)) {
        throw new OptionsActionsUtilsException("Failed to parse ignored_change.json, invalid JSON map");
      }
      return (Map<String, Object>) result;
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to read ignored_change.json", e);
    }
  }

  public static void saveIgnoredChange(Map<String, Object> ignoredChange) throws OptionsActionsUtilsException {
    verifyIgnoredChangeFile();
    try {
      String json = new GsonBuilder()
              .setPrettyPrinting()
              .create()
              .toJson(ignoredChange);
      Files.writeString(Paths.get(Constants.IGNORED_CHANGE_FILE), json);
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to write ignored_change.json", e);
    }
  }

  private static void verifyFileExtensionHandlingFile() throws OptionsActionsUtilsException {
    File fileExtensionHandlingFile = new File(Constants.FILE_EXTENSION_HANDLING_FILE);
    if (!fileExtensionHandlingFile.exists() || fileExtensionHandlingFile.length() == 0) {
      try {
        Files.write(fileExtensionHandlingFile.toPath(), "{}".getBytes(), StandardOpenOption.CREATE);
      } catch (IOException e) {
        throw new OptionsActionsUtilsException("Failed to create or verify file_extension_handling.json", e);
      }
    }
  }

  public static Map<String, String> getFileExtensionHandling() throws OptionsActionsUtilsException {
    verifyFileExtensionHandlingFile();
    try {
      return new Gson().fromJson(Files.readString(Paths.get(Constants.FILE_EXTENSION_HANDLING_FILE)),
              new TypeToken<Map<String, String>>() {}.getType());
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to read file_extension_handling.json", e);
    }
  }

  public static void saveFileExtensionHandling(Map<String, String> fileExtensionHandling) throws OptionsActionsUtilsException {
    verifyFileExtensionHandlingFile();
    try {
      String json = new GsonBuilder()
              .setPrettyPrinting()
              .create()
              .toJson(fileExtensionHandling);
      Files.writeString(Paths.get(Constants.FILE_EXTENSION_HANDLING_FILE), json);
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to write file_extension_handling.json", e);
    }
  }

  private static void verifySpecificFileHandlingFile() throws OptionsActionsUtilsException {
    File specificFileHandlingFile = new File(Constants.SPECIFIC_FILE_HANDLING_FILE);
    if (!specificFileHandlingFile.exists() || specificFileHandlingFile.length() == 0) {
      try {
        Files.write(specificFileHandlingFile.toPath(), "{}".getBytes(), StandardOpenOption.CREATE);
      } catch (IOException e) {
        throw new OptionsActionsUtilsException("Failed to create or verify specific_file_handling.json", e);
      }
    }
  }

  public static Map<String, String> getSpecificFileHandling() throws OptionsActionsUtilsException {
    verifySpecificFileHandlingFile();
    try {
      return new Gson().fromJson(Files.readString(Paths.get(Constants.SPECIFIC_FILE_HANDLING_FILE)),
              new TypeToken<Map<String, String>>() {}.getType());
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to read specific_file_handling.json", e);
    }
  }

  public static void saveSpecificFileHandling(Map<String, String> specificFileHandling) throws OptionsActionsUtilsException {
    verifySpecificFileHandlingFile();
    try {
      String json = new GsonBuilder()
              .setPrettyPrinting()
              .create()
              .toJson(specificFileHandling);
      Files.writeString(Paths.get(Constants.SPECIFIC_FILE_HANDLING_FILE), json);
    } catch (IOException e) {
      throw new OptionsActionsUtilsException("Failed to write specific_file_handling.json", e);
    }
  }
}
