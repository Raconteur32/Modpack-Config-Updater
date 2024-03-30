package fr.paulbrancieq.modpackconfigupdater.options.type;

import com.google.gson.JsonParseException;
import fr.paulbrancieq.modpackconfigupdater.Backup;
import fr.paulbrancieq.modpackconfigupdater.exceptions.OptionException;
import fr.paulbrancieq.modpackconfigupdater.options.Option;
import fr.paulbrancieq.modpackconfigupdater.path.OptionPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.google.gson.Gson;

public class JsonMapOptionFile extends MapOption {
  public JsonMapOptionFile(OptionPath optionPath, Backup backup, Map<Object, Option<?>> value, CollectionOption<?> parent) {
    super(optionPath, backup, value, parent);
  }

  @SuppressWarnings({"unused","Duplicates"})
  @Override
  public void save() throws OptionException.FileException.CantWriteFile {
    Gson gson = new Gson();
    try {
      File file = new File(optionPath.getFilePath());
      Boolean check = file.getParentFile().mkdirs();
      java.io.FileWriter writer = new java.io.FileWriter(file);
      gson.toJson(value, writer);
      writer.close();
    } catch (java.io.IOException e) {
      throw new OptionException.FileException.CantWriteFile(optionPath.getFilePath(), e);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static JsonMapOptionFile fromFile(OptionPath optionPath, Backup backup) throws OptionException.FileException.CantReadFile {
    if (!optionPath.getInFileOptionPathParts().isEmpty()) {
      throw new IllegalArgumentException("optionPath should only have the file path");
    }
    try {
      File file = new File(optionPath.getFilePath());
      Gson gson = new Gson();
      BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
      Map value = gson.fromJson(reader, Map.class);
      return new JsonMapOptionFile(optionPath, backup, serializableMapToOptionMap(optionPath, backup, (Map<Object, Object>) value), null);
    } catch (FileNotFoundException | JsonParseException e) {
      throw new OptionException.FileException.CantReadFile(optionPath.getFilePath(), e);
    }
  }

  @Override
  public JsonMapOptionFile deepCopy(CollectionOption<?> parent) {
    return new JsonMapOptionFile(optionPath, backup, value, parent);
  }
}
