package org.tessell.model.dsl;

import org.tessell.util.Inflector;

public class ListBoxIdentityAdaptor<P> implements ListBoxAdaptor<P, P> {

  private final String nullText;

  public ListBoxIdentityAdaptor() {
    this("");
  }

  public ListBoxIdentityAdaptor(String nullText) {
    this.nullText = nullText;
  }

  @Override
  public String toDisplay(P option) {
    // This is slightly odd, but it makes binding against enums with just the
    // two-arg .to(listBox, listOfEnums) do the right thing by default.
    if (option instanceof Enum<?> && ((Enum<?>) option).name().equals(option.toString())) {
      return Inflector.humanize(option.toString());
    }
    return option == null ? nullText : option.toString();
  }

  @Override
  public P toValue(P option) {
    return option;
  }

}
