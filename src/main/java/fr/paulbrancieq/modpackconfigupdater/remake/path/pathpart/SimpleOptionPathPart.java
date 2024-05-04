package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart;

import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleOptionPathPart extends OptionPathPart {
  private final String stringPathPart;

  public SimpleOptionPathPart(String stringPathPart) {
    this.stringPathPart = stringPathPart;
  }

  @Override
  public boolean match(Object objectToMatch) {
    if (!(objectToMatch instanceof String)) {
      return stringPathPart.equals(objectToMatch.toString());
    }
    return stringPathPart.equals(objectToMatch);
  }

  @Override
  public boolean match(Option<?> option) {
    return match(option.getContainer().getPathPart());
  }

  public String getStringPathPart() {
    return stringPathPart;
  }

  public static List<SimpleOptionPathPart> fromMultiPartString(String multiPartString) {
    // Split the string by '.' but not by '\.' . Match only the dots that are not preceded by a pair number of '\'
    // (pair number of '\' means that they are escaped '\' characters, and the dot is not escaped)
    List<SimpleOptionPathPart> uniqueOptionPathParts = new ArrayList<>();
    Pattern pattern = Pattern.compile("(?<!\\\\)(?:\\\\\\\\)*(\\.)");
    Matcher matcher = pattern.matcher(multiPartString);
    int start;
    int end;
    int previousEnd = 0;
    while (matcher.find()) {
      end = matcher.end(1);
      start = matcher.start(1) - (end - previousEnd - 1);
      uniqueOptionPathParts.add(new SimpleOptionPathPart(multiPartString.substring(start, end - 1)));
      previousEnd = end;
    }
    uniqueOptionPathParts.add(new SimpleOptionPathPart(multiPartString.substring(previousEnd)));
    return uniqueOptionPathParts;
  }
}
