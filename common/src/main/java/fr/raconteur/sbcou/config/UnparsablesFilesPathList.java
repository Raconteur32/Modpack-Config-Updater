package fr.raconteur.sbcou.config;

import com.google.gson.JsonElement;
import fr.raconteur.sbcou.Constants;

import java.util.List;

public class UnparsablesFilesPathList {
  public static List<String> getUnparsablesFilePaths() throws SbcouConfigFile.SbcouConfigFileException {
    return new java.util.ArrayList<>(new JsonArrayConfigFile<>(Constants.UNPARSABLE_FILES_FILE,
            (data) -> {
              for (JsonElement path : data.asList()) {
                if (!path.isJsonPrimitive() || !path.getAsJsonPrimitive().isString()) {
                  throw new SbcouConfigFile.SbcouConfigFileException(
                          "Invalid path in excluded file paths list: " + path);
                }
              }
              return data;
            }).verify().asList().stream().map(JsonElement::getAsString).toList());
  }
}
