package org.tessell.model.dsl;

/**
 * For adapting options to properties in a list box.
 *
 * @param P the target property type
 * @param O the listed option type
 */
public interface ListBoxAdaptor<P, O> {

  /** @return the string to display in the list box */
  String toDisplay(O option);

  /** @return the value for the given option to set into the property */
  P toValue(O option);

}
