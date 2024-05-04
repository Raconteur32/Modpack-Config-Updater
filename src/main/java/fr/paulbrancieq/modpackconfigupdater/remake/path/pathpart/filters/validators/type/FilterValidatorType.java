package fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.validators.type;

import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.validators.FilterOptionPathPartValidator;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.filters.validators.RangeFilterOptionPathPartValidator;

@JsonAdapter(FilterValidatorDeserializer.class)
public enum FilterValidatorType {
  RANGE("range", RangeFilterOptionPathPartValidator.class); // TODO: Add more filter types
  // Note for myself for when adding more filters: You have already done all that is needed to verify the filter fields.
  // Take example on the RangeFilterOptionPathPartValidator class to create a new filter type. You may add a @JsonAdapter
  // on the new filter class if you need a more complex deserialization process.
  private final String type;
  private final Class<? extends FilterOptionPathPartValidator> filterValidatorOptionPathPartClass;

  FilterValidatorType(String type, Class<? extends FilterOptionPathPartValidator> filterOptionPathPartClass) {
    this.type = type;
    this.filterValidatorOptionPathPartClass = filterOptionPathPartClass;
  }

  public String getTypeString() {
    return type;
  }

  public Class<? extends FilterOptionPathPartValidator> getFilterValidatorOptionPathPartClass() {
    return filterValidatorOptionPathPartClass;
  }

  public static FilterValidatorType fromString(String type) {
    for (FilterValidatorType filterValidatorType : FilterValidatorType.values()) {
      if (filterValidatorType.getTypeString().equals(type)) {
        return filterValidatorType;
      }
    }
    throw new IllegalArgumentException("Unknown filter validator type: " + type);
  }
}
