package fr.paulbrancieq.modpackconfigupdater.remake.path.filters.validators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import fr.paulbrancieq.modpackconfigupdater.remake.mcufile.JsonRequired;
import org.jetbrains.annotations.Nullable;

@JsonAdapter(FilterOptionPathPartValidatorJsonDeserializer.class)
public abstract class FilterOptionPathPartValidator {
  @Expose
  @JsonRequired
  protected FilterValidatorType type;

  public abstract boolean match(@Nullable Object otherStringPathPart);
}
