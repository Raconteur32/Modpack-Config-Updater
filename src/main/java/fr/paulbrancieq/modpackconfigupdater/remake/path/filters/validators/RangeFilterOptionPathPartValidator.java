package fr.paulbrancieq.modpackconfigupdater.remake.path.filters.validators;

import com.google.gson.annotations.Expose;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;
import org.jetbrains.annotations.Nullable;

public class RangeFilterOptionPathPartValidator extends FilterOptionPathPartValidator {
  @Expose
  @JsonRequired
  private int min;
  @Expose
  @JsonRequired
  private int max;

  @Override
  public boolean match(@Nullable Object otherStringPathPart) {
    if (otherStringPathPart instanceof Integer) {
      int otherNumber = (int) otherStringPathPart;
      return otherNumber >= min && otherNumber <= max;
    }
    if (otherStringPathPart instanceof Double) {
      double otherNumber = (double) otherStringPathPart;
      return otherNumber >= min && otherNumber <= max;
    }
    if (otherStringPathPart instanceof Float) {
      float otherNumber = (float) otherStringPathPart;
      return otherNumber >= min && otherNumber <= max;
    }
    if (otherStringPathPart instanceof Long) {
      long otherNumber = (long) otherStringPathPart;
      return otherNumber >= min && otherNumber <= max;
    }
    if (otherStringPathPart instanceof String) {
      try {
        double otherNumber = Double.parseDouble((String) otherStringPathPart);
        return otherNumber >= min && otherNumber <= max;
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return false;
  }
}
