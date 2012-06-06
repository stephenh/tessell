package org.tessell.model.dsl;

import org.tessell.util.Inflector;

public class ListBoxHumanizerAdaptor<P> implements ListBoxAdaptor<P, P> {

  @Override
  public String toDisplay(P option) {
    return option == null ? "" : Inflector.humanize(option.toString());
  }

  @Override
  public P toValue(P option) {
    return option;
  }

}
