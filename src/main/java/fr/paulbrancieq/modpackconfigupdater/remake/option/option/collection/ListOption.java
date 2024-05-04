package fr.paulbrancieq.modpackconfigupdater.remake.option.option.collection;

import fr.paulbrancieq.modpackconfigupdater.remake.option.CollectionOptionChildOperationException;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainer;
import fr.paulbrancieq.modpackconfigupdater.remake.option.containers.OptionContainerBasicImpl;
import fr.paulbrancieq.modpackconfigupdater.remake.option.option.Option;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.OptionPathPart;
import fr.paulbrancieq.modpackconfigupdater.remake.path.pathpart.SimpleOptionPathPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOption extends CollectionOption<List<OptionContainer>> {
  public ListOption(@NotNull @Unmodifiable List<OptionContainer> value, @NotNull OptionContainer container) {
    super(new ArrayList<>(), container);
    for (OptionContainer optionContainer : value) {
      add(optionContainer.getOption());
    }
  }

  public static ListOption fromSerializable(@NotNull @Unmodifiable List<Object> serializable,
                                            @NotNull OptionContainer container) {
    ListOption listOption = new ListOption(new ArrayList<>(), container);
    for (Object o : serializable) {
      listOption.add(o);
    }
    return listOption;
  }

  @SuppressWarnings("unused")
  private void add(@NotNull Option<?> option) {
    value.add(new OptionContainerBasicImpl(option, this, String.valueOf(value.size())));
  }

  @SuppressWarnings("unused")
  private void add(@Nullable Object newOptionValue) {
    value.add(new OptionContainerBasicImpl(newOptionValue, this, String.valueOf(value.size())));
  }

  @Override
  public @NotNull @Unmodifiable List<OptionContainer> getRawValue() {
    return Collections.unmodifiableList(value);
  }

  @Override
  public boolean equals(@NotNull Option<?> otherOpt) {
    if (!(otherOpt instanceof ListOption)) {
      return false;
    }
    List<OptionContainer> otherValue = ((ListOption) otherOpt).getRawValue();
    if (value.size() != otherValue.size()) {
      return false;
    }
    for (int i = 0; i < value.size(); i++) {
      if (!value.get(i).getOption().equals(otherValue.get(i).getOption())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Option<List<OptionContainer>> deepCopy(@NotNull OptionContainer newContainer) {
    return new ListOption(getRawValue(), newContainer);
  }

  @Override
  public List<OptionContainer> getChildContainerListFromPathPart(@NotNull OptionPathPart pathPart) {
    return value.stream().filter(optionContainer -> pathPart.match(optionContainer.getOption())).toList();
  }

  @Override
  public @NotNull OptionContainer getChildContainerFromUniquePathPart(@NotNull SimpleOptionPathPart pathPart)
          throws CollectionOptionChildOperationException.ChildDoesNotExist {
    return value.stream().filter(optionContainer -> pathPart.match(optionContainer.getOption())).findFirst()
            .orElseThrow(CollectionOptionChildOperationException.ChildDoesNotExist::new);
  }

  @Override
  public void defaultOption(@NotNull Option<?> refOption)
          throws CollectionOptionChildOperationException.NotADefaultableCollectionOption {
    throw new CollectionOptionChildOperationException.NotADefaultableCollectionOption();
  }

  @Override
  public void defaultChildOption(@NotNull Option<?> refOption)
          throws CollectionOptionChildOperationException.NotADefaultableCollectionOption {
    throw new CollectionOptionChildOperationException.NotADefaultableCollectionOption();
  }

  @Override
  public void removeChildOption(@NotNull OptionPathPart pathPart) {
    List<OptionContainer> optionContainer = value.stream().filter(oc -> pathPart.match(oc.getOption())).toList();
    for (OptionContainer oc : optionContainer) {
      value.remove(oc);
    }
  }
}
