package fr.raconteur.sbcou;

import fr.raconteur.sbcou.config.ExcludedFilePathsList;
import fr.raconteur.sbcou.config.SbcouConfigFile;
import fr.raconteur.sbcou.config.UnparsablesFilesPathList;
import fr.raconteur.sbcou.exceptions.ConfigContextException;
import fr.raconteur.sbcou.exceptions.SbcouException;
import fr.raconteur.sbcou.fileshandlers.*;
import fr.raconteur.sbcou.flatobject.FlatObject;
import fr.raconteur.sbcou.platform.Services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigContext {
  private static final Map<String, Class<? extends AbstractConfigFileHandler>> DEFAULT_HANDLERS = Map.of(
          ".json", JsonConfigFileHandler.class,
          ".json5", JsonConfigFileHandler.class,
          ".properties", PropertiesConfigFileHandler.class,
          ".toml", TomlConfigFileHandler.class
  );

  private static final Map<String, Class<? extends AbstractConfigFileHandler>> BUILTIN_SPECIFIC_HANDLERS = Map.of(
          "options.txt", PropertiesConfigFileHandler.class
  );

  private final String basePath;
  private final Map<String, Class<? extends AbstractConfigFileHandler>> extensionHandlers;
  private final Map<String, Class<? extends AbstractConfigFileHandler>> specificFileHandlers;
  private final Map<String, AbstractConfigFileHandler> fileHandlers;
  private final Set<String> excludedPaths;

  public ConfigContext(String basePath) throws SbcouException {
    this.basePath = basePath.replace(File.separator, "/");
    this.extensionHandlers = new HashMap<>();
    this.specificFileHandlers = new HashMap<>();
    this.fileHandlers = new HashMap<>();
    this.excludedPaths = initializeExcludedPaths();
    loadHandlings();
    initializeFileHandlers();
  }

  private Set<String> initializeExcludedPaths() throws SbcouConfigFile.SbcouConfigFileException {
    List<String> excludedSet = ExcludedFilePathsList.getExcludedFilePaths();

    return excludedSet.stream()
            .map(path -> {
              try {
                return Paths.get(basePath, path).toFile().getCanonicalPath().replace(File.separator, "/");
              } catch (IOException e) {
                Services.PLATFORM.logError("Error canonicalizing excluded path: " + path, e);
                return null;
              }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
  }

  private void initializeFileHandlers() throws SbcouException {
    try (var paths = Files.walk(Paths.get(basePath))) {
      paths.filter(Files::isRegularFile)
              .forEach(this::addFileHandler);
    } catch (IOException e) {
      throw new ConfigContextException("Error reading files", e);
    }
  }

  private boolean hasAcceptedExtension(Path path) {
    String relativePath = Paths.get(basePath).relativize(path).toString().replace(File.separator, "/");

    // Check specific file mappings first
    if (specificFileHandlers.containsKey(relativePath)) {
      return true;
    }

    // Then check custom extensions
    String extension = this.getFileExtension(path.toString());
    return extensionHandlers.keySet().stream()
            .anyMatch(extension::equalsIgnoreCase);
  }

  private void addFileHandler(Path path) {
    try {
      String canonicalPath = path.toFile().getCanonicalPath().replace(File.separator, "/");
      if (!isExcludedPath(canonicalPath)) {
        String relativePath = Paths.get(basePath).relativize(path).toString().replace(File.separator, "/");
        Class<? extends AbstractConfigFileHandler> handlerClass = null;

        // If the file is an unparsable file
        List<String> unparsableFiles = UnparsablesFilesPathList.getUnparsablesFilePaths();
        if (unparsableFiles.contains(relativePath)) {
          handlerClass = UnparsableFileHandler.class;
        } else if (!hasAcceptedExtension(path)) {
          return;
        }

        // Check specific file handlers first
        if (handlerClass == null) {
          handlerClass = specificFileHandlers.get(relativePath);
        }

        if (handlerClass == null) {
          // Fall back to extension-based handling
          String extension = getFileExtension(relativePath);
          handlerClass = extensionHandlers.get(extension.toLowerCase());
        }

        if (handlerClass != null) {
          AbstractConfigFileHandler fileHandler = handlerClass.getDeclaredConstructor(String.class, String.class)
                  .newInstance(basePath, relativePath);
          fileHandlers.put(relativePath, fileHandler);
        }
      }
    } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
             IllegalAccessException | SbcouConfigFile.SbcouConfigFileException e) {
      Services.PLATFORM.logError("Error during file handler initialization: " + path, e);
    }
  }

  private boolean isExcludedPath(String canonicalPath) {
    return excludedPaths.stream().anyMatch(excludedPath ->
            canonicalPath.startsWith(excludedPath) ||
                    canonicalPath.startsWith(excludedPath.replace(File.separator, "/"))
    );
  }

  public FlatObject generateContextFlatObject() {
    Map<String, Object> contextMap = new HashMap<>();
    for (Map.Entry<String, AbstractConfigFileHandler> entry : fileHandlers.entrySet()) {
      try {
        contextMap.put(entry.getKey(), entry.getValue().read());
      } catch (Exception e) {
        Services.PLATFORM.logError("Error reading file " + entry.getKey(), e);
      }
    }
    return FlatObject.fromMap(contextMap);
  }

  public void writeFromContextMap(FlatObject contextMap) {
    for (Map.Entry<String, Object> entry : contextMap.toMap().entrySet()) {
      String filePath = entry.getKey();
      Object value = entry.getValue();

      AbstractConfigFileHandler fileHandler = fileHandlers.get(filePath);
      if (fileHandler == null) {
        addFileHandler(Paths.get(basePath, filePath));
      }

      fileHandler = fileHandlers.get(filePath);
      if (fileHandler == null) {
        Services.PLATFORM.logWarning("No file handler found for: " + filePath);
        continue;
      }

      try {
        fileHandler.write(value);
      } catch (Exception e) {
        Services.PLATFORM.logError("Error writing to file: " + filePath, e);
      }
    }
  }

  private String getFileExtension(String filePath) {
    int lastDotIndex = filePath.lastIndexOf('.');
    return (lastDotIndex == -1) ? "" : filePath.substring(lastDotIndex);
  }


  private void updateDefault(FlatObject baseMap, FlatObject updateMap) {
    for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
      if (!baseMap.containsKey(entry.getKey())) {
        baseMap.put(entry.getKey(), entry.getValue());
      }
    }
  }

  private void updateOverrides(FlatObject baseMap, List<String> overrideList, FlatObject updateMap) {
    for (String key : overrideList) {
      if (updateMap.containsKey(key)) {
        baseMap.put(key, updateMap.get(key));
      }
    }
  }

  private void updateDeletesAndWrite(List<String> deleteList) {
    FlatObject baseMap = FlatObject.distinctFlatObject(this.generateContextFlatObject());

    for (String key : deleteList) {
      baseMap.remove(key);
    }

    this.writeFromContextMap(baseMap);
  }

  public void update(Version updateVersion) throws SbcouException {
    FlatObject baseMap = this.generateContextFlatObject();
    FlatObject updateMap = updateVersion.getContext().generateContextFlatObject();

    updateDefault(baseMap, updateMap);
    updateOverrides(baseMap, updateVersion.getOverrides(), updateMap);
    // Update deletes needs updated maps in the FlatObject so we need to write the context the method will regenerate
    // the base map from the files
    this.writeFromContextMap(baseMap);
    updateDeletesAndWrite(updateVersion.getDeletes());
  }

  private void loadHandlings() throws SbcouException {
    // Load extension handlers
    extensionHandlers.putAll(DEFAULT_HANDLERS);
    Map<String, String> customExtensions = OptionsActionsUtils.getFileExtensionHandling();
    for (Map.Entry<String, String> entry : customExtensions.entrySet()) {
      String extension = entry.getKey();
      String handlerExtension = entry.getValue();
      Class<? extends AbstractConfigFileHandler> handlerClass = DEFAULT_HANDLERS.get(handlerExtension);
      if (handlerClass != null) {
        extensionHandlers.put(extension, handlerClass);
      }
    }

    // Load specific file handlers
    specificFileHandlers.putAll(BUILTIN_SPECIFIC_HANDLERS);
    Map<String, String> specificMappings = OptionsActionsUtils.getSpecificFileHandling();
    for (Map.Entry<String, String> entry : specificMappings.entrySet()) {
      String filename = entry.getKey();
      String handlerExtension = entry.getValue();
      Class<? extends AbstractConfigFileHandler> handlerClass = DEFAULT_HANDLERS.get(handlerExtension);
      if (handlerClass != null) {
        specificFileHandlers.put(filename, handlerClass);
      }
    }
  }
}
