package org.tessell.model.dsl;

@FunctionalInterface
public interface ListBoxLambdaAdaptor<P> extends ListBoxAdaptor<P, P> {

  @Override
  default P toValue(P option) {
    return option;
  }
}
