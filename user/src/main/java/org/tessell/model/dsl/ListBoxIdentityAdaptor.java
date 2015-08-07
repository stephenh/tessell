package org.tessell.model.dsl;

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
    return option == null ? nullText : option.toString();
  }

  @Override
  public P toValue(P option) {
    return option;
  }

}
