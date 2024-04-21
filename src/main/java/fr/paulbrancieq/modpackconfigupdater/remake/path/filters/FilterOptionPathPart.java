package fr.paulbrancieq.modpackconfigupdater.remake.path.filters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;
import fr.paulbrancieq.modpackconfigupdater.remake.path.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.UniqueOptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.filters.validators.FilterOptionPathPartValidator;

import java.util.List;

@JsonAdapter(FilterOptionPathPartJsonDeserializer.class)
public class FilterOptionPathPart extends OptionPathPart {
  // TODO: Need to match a Option. Need Option to be implemented or at least a skeleton of it first. Then we can
  // implement the match method for each target type (make sub classes for each target type and implement the match)
  @Expose
  @JsonRequired
  private final FilterTarget target;

  @Expose
  @JsonRequired
  private final List<FilterOptionPathPartValidator> validators;

  public FilterOptionPathPart(List<FilterOptionPathPartValidator> validators, FilterTarget target) {
    this.validators = validators;
    this.target = target;
  }

  public boolean match(Object otherStringPathPart) {
    for (FilterOptionPathPartValidator validator : validators) {
      if (!validator.match(otherStringPathPart)) {
        return false;
      }
    }
    return true;
  }

  public boolean match(UniqueOptionPathPart otherOptionPathPart) {
    return match(otherOptionPathPart.getStringPathPart());
  }
}
