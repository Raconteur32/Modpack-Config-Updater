package fr.raconteur.sbcou.config;

import com.google.gson.JsonElement;
import fr.raconteur.sbcou.Constants;

import java.util.List;

public class ExcludedFilePathsList {
  public static final List<String> HARD_EXCLUDED_PATHS = List.of(
          "config/sbcou",
          "crash-reports",
          "logs",
          "saves",
          "usercache.json",
          "screenshots"
  );

  public static List<String> getExcludedFilePaths() throws SbcouConfigFile.SbcouConfigFileException {
    List<String> list = new java.util.ArrayList<>(new JsonArrayConfigFile<>(Constants.EXCLUDED_FILE_PATHS_FILE,
            (data) -> {
              for (JsonElement path : data.asList()) {
                if (!path.isJsonPrimitive() || !path.getAsJsonPrimitive().isString()) {
                  throw new SbcouConfigFile.SbcouConfigFileException(
                          "Invalid path in excluded file paths list: " + path);
                }
              }
              return data;
            }).verify().asList().stream().map(JsonElement::getAsString).toList());
    list.addAll(HARD_EXCLUDED_PATHS);
    return list;
  }
}
