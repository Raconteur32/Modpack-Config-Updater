package fr.paulbrancieq.modpackconfigupdater.path.filter;

import fr.paulbrancieq.modpackconfigupdater.exceptions.FilterException;
import fr.paulbrancieq.modpackconfigupdater.options.type.CollectionOption;
import fr.paulbrancieq.modpackconfigupdater.options.type.ListOption;
import fr.paulbrancieq.modpackconfigupdater.options.type.MapOption;
import fr.paulbrancieq.modpackconfigupdater.options.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import java.util.function.Function;

/**
 * Filter a Collection Option. Built with a string with this format:
 * ["value_to_filter";"expected_value_expression", "value_to_filter2";2, ...]
 * value_to_filter: the value to filter. It has 2 built-in values: mcu_index (to filter with the index if the Collection
 * is a list) and mcu_key (to filter with the key if the Collection is a map). Any other value will be considered as an
 * expression to describe the expected value.
 */
public class Filter {
  List<SubFilters> filters = new ArrayList<>();
  public Filter(String filterString) throws FilterException {
    if (!filterString.matches("^\\[.*]$")) {
      throw new FilterException.InvalidFilterString("Invalid filter format: " + filterString);
    }
    // remove the brackets
    filterString = filterString.substring(1, filterString.length() - 1);
    Pattern pattern = Pattern.compile(", ?");
    String[] filters = pattern.split(filterString);
    for (String filter : filters) {
      this.filters.add(new SubFilters(filter));
    }
  }

  public List<Option<?>> filter(CollectionOption<?> option) {
    List<Option<?>> filteredOptions = new ArrayList<>();
    if (option.getValue() instanceof Map) {
      MapOption mapOption = (MapOption) option;
      for (Map.Entry<Object, Option<?>> entry : mapOption.getValue().entrySet()) {
        boolean passFilter = filters.stream().allMatch(filter -> filter.filter(entry.getValue(), entry.getKey()));
        if (passFilter) {
          filteredOptions.add(entry.getValue());
        }
      }
    } else if (option.getValue() instanceof List) {
      ListOption listOption = (ListOption) option;
      for (int i = 0; i < listOption.getValue().size(); i++) {
        int finalI = i;
        boolean passFilter = filters.stream().allMatch(filter -> filter.filter(listOption.getValue().get(finalI), finalI));
        if (passFilter) {
          filteredOptions.add(listOption.getValue().get(i));
        }
      }
    }
    return filteredOptions;
  }

  public static class SubFilters {
    private final String valueToFilter;
    ValueValidator filterFunction;
    public SubFilters(String filterString) throws FilterException {
      Pattern pattern = Pattern.compile("(?<!\\\\);");
      String[] parts = pattern.split(filterString);
      if (parts.length != 2) {
        throw new FilterException.InvalidFilterString("Invalid sub-filter format: " + filterString);
      }
      String[] acceptedValuesToFilter = {"mcu_index", "mcu_key", "mcu_value"};
      this.valueToFilter = parts[0].substring(1, parts[0].length() - 1);
      if (!List.of(acceptedValuesToFilter).contains(valueToFilter)) {
        throw new FilterException.InvalidValueToFilter(valueToFilter, acceptedValuesToFilter);
      }
      String expectedValueExpression = parts[1];
      this.filterFunction = generateValueValidator(expectedValueExpression);
    }

    private ValueValidator generateValueValidator(String expectedValueExpression) {
      return new ValueValidator(expectedValueExpression);
    }

    private boolean filter(Option<?> option, Object indexOrKey) {
      if (valueToFilter.equals("mcu_index") || valueToFilter.equals("mcu_key")) {
        return filterFunction.filter(indexOrKey);
      }
      if (valueToFilter.equals("mcu_value")) {
        Object value = option.getValue();
        return this.filterFunction.filter(value);
      }
      throw new RuntimeException("Invalid value to filter: " + valueToFilter);
    }

    public static class ValueValidator {
      Function<Object, Boolean> filterFunction;

      public boolean filter(Object value) {
        return filterFunction.apply(value);
      }
      public ValueValidator(String expectedValueExpression) {
        // if matches a string
        if (expectedValueExpression.matches("^\".*\"$")) {
          String expectedRegex = expectedValueExpression.substring(1, expectedValueExpression.length() - 1);
          filterFunction = (value) -> {
            if (!(value instanceof String)) {
              return false;
            }
            return Pattern.matches(expectedRegex, (String) value);
          };
        }
        // if matches a integer
        else if (expectedValueExpression.matches("^-?\\d+$")) {
          int expectedValue = Integer.parseInt(expectedValueExpression);
          filterFunction = (value) -> {
            if (!(value instanceof Integer)) {
              return false;
            }
            return ((Number) value).intValue() == expectedValue;
          };
        }
        // if matches a double / float
        else if (expectedValueExpression.matches("^-?\\d+\\.\\d+$")) {
          double expectedValue = Double.parseDouble(expectedValueExpression);
          filterFunction = (value) -> {
            if (!(value instanceof Double)) {
              return false;
            }
            return ((Number) value).doubleValue() == expectedValue;
          };
        }
        // if matches a boolean
        else if (expectedValueExpression.matches("^(true|false)$")) {
          boolean expectedValue = Boolean.parseBoolean(expectedValueExpression);
          filterFunction = (value) -> {
            if (!(value instanceof Boolean)) {
              return false;
            }
            return value.equals(expectedValue);
          };
        }
        // if matches a range
        else if (expectedValueExpression.matches("^(-?\\d+(?:\\.\\d+)?)__(-?\\d+(?:\\.\\d+)?)$")) {
          String[] range = expectedValueExpression.split("__");
          double min = Double.parseDouble(range[0]);
          double max = Double.parseDouble(range[1]);
          filterFunction = (value) -> {
            if (!(value instanceof Number)) {
              return false;
            }
            double doubleValue = ((Number) value).doubleValue();
            return doubleValue >= min && doubleValue <= max;
          };
        }
      }
    }
  }
}
