package fr.paulbrancieq.modpackconfigupdater.path;

import fr.paulbrancieq.modpackconfigupdater.ModpackConfigurationUpdater;
import fr.paulbrancieq.modpackconfigupdater.exceptions.FilterException;
import fr.paulbrancieq.modpackconfigupdater.path.filter.Filter;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;

/**
 * This class is an object representing a path to an option.
 * Built with a string representing the path to the option.
 * 2 parts: the path to the file containing the option relative to .minecraft, and the path to the option in the file.
 * The parts are separated by a colon (':').
 * Each part of the path is a string.
 * The separator for file paths is the backward or forward slash ('/' or '\').
 * The separator for option paths is the dot ('.').
 * Each dot in the option path represents a level of nesting in the option (object and sub-objects).
 */
public class OptionPath {
  protected String filePathString;
  protected String inFileOptionPathString;
  protected List<InFileOptionPathPart> inFileOptionPathParts = new ArrayList<>();
  protected final String fullPath;

  /**
   * Construct an OptionPath object with a string representing the path to the option.
   */
  public OptionPath(String path) {
    fullPath = path;
    sliceTwoParts(path);
    correctFilePath();
    verifyFilePath();
    splitOptionPath();
  }

  /**
   * Get the full path.
   *
   * @return The full path.
   */
  public String getFullPath() {
    return fullPath;
  }

  /**
   * Verify that the filepath is a valid filepath with \ or / as separators.
   * It should be a relative path.
   */
  private void verifyFilePath() {
    try {
      Path path = Path.of(filePathString);
      if (path.isAbsolute()) {
        throw new IllegalArgumentException("The file path must be a relative path.");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("The file path must be a valid file path.", e);
    }
  }

  /**
   * Correct the filepath to make it use the system's file separator.
   * Replace all '/' with '\' if the system is Windows, and all '\' with '/' if the system is not Windows.
   */
  private void correctFilePath() {
    filePathString = System.getProperty("os.name").toLowerCase().contains("windows") ?
        filePathString.replace('/', '\\') : filePathString.replace('\\', '/');
  }

  /**
   * Slice the inputted string into 2 parts: the file path and the option path. Ignore the ':'
   * if it's escaped by a backslash.
   *
   * @param path The path to the option.
   */
  private void sliceTwoParts(String path) {
    int separatorIndex = -1;
    for (int i = 0; i < path.length(); i++) {
      if (path.charAt(i) == ':' && (i == 0 || path.charAt(i - 1) != '\\')) {
        separatorIndex = i;
        break;
      }
    }
    if (separatorIndex == -1) {
      throw new IllegalArgumentException("The path must contain a colon (':') to separate the file path and the option path.");
    }
    filePathString = path.substring(0, separatorIndex);
    inFileOptionPathString = path.substring(separatorIndex + 1);
  }

  /**
   * Split the option path into parts. Each part is a string representing a part of the option path.
   * The parts are separated by dots ('.').
   * Each dot in the option path represents a level of nesting in the option (object and sub-objects).
   * Escape the dots if they're escaped by a backslash.
   */
  private void splitOptionPath() {
    String[] parts = inFileOptionPathString.split("(?<!\\\\)\\.");
    for (String part : parts) {
      inFileOptionPathParts.add(new InFileOptionPathPart(part));
    }
  }

  /**
   * Get the file path.
   *
   * @return The file path.
   */
  public String getFilePath() {
    return filePathString;
  }

  /**
   * Get the option path.
   *
   * @return The option path.
   */
  public String getInFileOptionPath() {
    return inFileOptionPathString;
  }

  /**
   * Get the option path parts.
   *
   * @return The option path parts.
   */
  public List<InFileOptionPathPart> getInFileOptionPathParts() {
    return inFileOptionPathParts;
  }

  /**
   * Get a sub path from the option path.
   *
   * @param keyOrIndex The key or index of the sub path.
   */
  public OptionPath getSubPath(Object keyOrIndex) {
    // keyOrIndex should be a string or a number or a boolean
    if (!(keyOrIndex instanceof String) && !(keyOrIndex instanceof Number) && !(keyOrIndex instanceof Boolean)) {
      throw new IllegalArgumentException("The key or index must be a string, a number, or a boolean.");
    }
    if (!fullPath.endsWith(":")) {
      return new OptionPath(fullPath + "." + keyOrIndex);
    } else {
      return new OptionPath(fullPath + keyOrIndex);
    }
  }

  /**
   * Option path part. Represent a part of inFileOptionPathString (between two dots).
   * Can be a string as a key in a map, or an index in a list. Can also be a filter.
   */
  public static class InFileOptionPathPart {
    protected final String baseString;
    protected boolean isFilter = false;
    protected Filter filter;

    /**
     * Construct an OptionPathPart object with a string representing a part of the option path.
     */
    public InFileOptionPathPart(String part) {
      baseString = part;
      try {
        if (baseString.matches("^\\[.*]$")) {
          filter = new Filter(part);
          isFilter = true;
        }
      } catch (FilterException e) {
        ModpackConfigurationUpdater.LOGGER.warn("Option path part could be a filter but is not correctly formatted: " + part);
      }
    }

    /**
     * Get the base string.
     *
     * @return The base string.
     */
    public String getBaseString() {
      return baseString;
    }
  }
}
