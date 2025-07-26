package fr.raconteur.sbcou.fileshandlers.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class StrictlyComparableArrayList extends ArrayList<Object> {
  @Override
  public boolean equals(Object o) {
    // The original ArrayList implementation only verifies the identity of the list.
    // Here, we want to verify the content, so we use equals() on each element.
    // We also ensure that the types are among those identified for use in JSON,
    // and that their equals methods compare their content in depth.
    if (!(o instanceof StrictlyComparableArrayList other)) {
      return false;
    }
    if (this.size() != other.size()) {
      return false;
    }
    for (int i = 0; i < this.size(); i++) {
      Object thisElement = this.get(i);
      Object otherElement = other.get(i);
      if (!Utils.isValidType(thisElement) || !Utils.isValidType(otherElement)) {
        throw new RuntimeException("Invalid type in StrictlyComparableArrayList");
      }
      if (thisElement == null ^ otherElement == null) {
        return false;
      } else if (thisElement == null && otherElement == null) {
        return true;
      } else if (!thisElement.getClass().isArray() && !otherElement.getClass().isArray() &&
              !Objects.equals(thisElement, otherElement)) {
        return false;
      } else if (thisElement.getClass().isArray() && otherElement.getClass().isArray() &&
              !Arrays.deepEquals((Object[]) thisElement, (Object[]) otherElement)) {
        return false;
      } else if (thisElement.getClass().isArray() || otherElement.getClass().isArray()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<?> c) {
    for (Object o : c) {
      if (!add(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean add(Object o) {
    return super.add(Utils.toValidType(o));
  }
}
