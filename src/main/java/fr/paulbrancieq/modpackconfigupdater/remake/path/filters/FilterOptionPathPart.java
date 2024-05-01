package fr.paulbrancieq.modpackconfigupdater.remake.path.filters;

import com.google.gson.annotations.Expose;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.filters.validators.FilterOptionPathPartValidator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FilterOptionPathPart extends OptionPathPart {
  @Expose
  @JsonRequired
  private FilterTarget target;

  @Expose
  @JsonRequired
  private List<FilterOptionPathPartValidator> validators;

  @Override
  protected boolean match(@Nullable Object objectToMatch) {
    for (FilterOptionPathPartValidator validator : validators) {
      if (!validator.match(objectToMatch)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean match(Option<?> option) {
    if (target == FilterTarget.OPTION_VALUE) {
      return match(option.getRawValue());
    } else if (target == FilterTarget.INDEX) {
      return match(option.getContainer().getPathPart());
    }
    return false;
  }
}
