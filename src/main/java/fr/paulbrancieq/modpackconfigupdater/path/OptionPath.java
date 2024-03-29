package fr.paulbrancieq.modpackconfigupdater.path;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * To aim at a specific element in a list, the index of the element is written in square brackets ('[]').
 */
public class OptionPath {
  protected String filePathString;
  protected String inFileOptionPathString;
  protected List<InFileOptionPathPart> inFileOptionPathParts = new ArrayList<>();

  /**
   * Construct an OptionPath object with a string representing the path to the option.
   */
  public OptionPath(String path) {
    sliceTwoParts(path);
    correctFilePath();
    verifyFilePath();
    splitOptionPath();
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
   * Option path part. Represent a part of inFileOptionPathString (between two dots).
   * Contain a partName, and optionally an index if the part is an element of a list.
   * Example: "partName" or "partName[0]".
   */
  public static class InFileOptionPathPart {
    protected String baseString;
    protected String partName;
    protected String filterPart;
    protected List<String> filters = new ArrayList<>();
    protected boolean haveIndex = false;

    /**
     * Construct an OptionPathPart object with a string representing a part of the option path.
     */
    public InFileOptionPathPart(String part) {
      baseString = part;
      verifyBaseString();
      slicePart(part);
    }

    /**
     * Verify base string. Example: "partName" or "partName[0]". Should match
     * the regex "^(?<namepart>([^\[\]]|(?<=\\)[\[\]])*)(?<indexpart>\[[1-9]+])?$".
     */
    private void verifyBaseString() {
      if (!Pattern.matches("^(?<namepart>(?:[^\\[\\]]|(?<=\\\\)[\\[\\]])*)(?<filterpart>\\[.+])?$", baseString)) { // TODO handle multiple indexes for sub lists
          throw new IllegalArgumentException("The part must be a valid part name with an optional index.");
      }
    }

    /**
     * Slice the inputted string into 2 parts: the part name and the index. Ignore the '[' and ']'
     * if they're escaped by a backslash. Should use the regex "^(?<namepart>([^\[\]]|(?<=\\)[\[\]])*)(?<indexpart>\[[1-9]+])?$".
     *
     * @param part The part of the option path.
     */
    private void slicePart(String part) {
      Pattern pattern = Pattern.compile("^(?<namepart>(?:[^\\[\\]]|(?<=\\\\)[\\[\\]])*)(?<filterpart>\\[.+])?$"); // TODO handle multiple indexes for sub lists
      Matcher matcher = pattern.matcher(part);
      if (matcher.find()) {
        partName = matcher.group("namepart");
        filterPart = matcher.group("filterpart");
      }
      // unescape partName (remove all non-escaped backslashes)
      partName = partName.replaceAll("\\\\([^\\\\])", "$1");
      // slice filter part
      if (filterPart != null) {
        // filter part example: "[test][42][lol][toto]". Split it
        String[] filters = filterPart.substring(1, filterPart.length() - 1).split("(?<!\\\\)\\[");
        for (String filter : filters) {
          // unescape filter (remove all non-escaped backslashes)
          filter = filter.replaceAll("\\\\([^\\\\])", "$1");
          this.filters.add(filter);
        }
      }
    }

    /**
     * Get the part name.
     *
     * @return The part name.
     */
    public String getPartName() {
      return partName;
    }

    /**
     * Get the filters.
     *
     * @return The filters.
     */
    public List<String> getFilters() {
      return filters;
    }
  }
}
