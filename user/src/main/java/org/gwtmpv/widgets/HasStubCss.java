package org.gwtmpv.widgets;

import java.util.List;

/**
 * An interface for {@link IsWidget} and {@link IsElement} stubs to implement so tests can see what style names have
 * been set regardless of the target being a widget or element.
 * 
 * Note the existing getStyleName/getClassName returns a space-delimited string; this interface purposefully returns a
 * list for ease of assertions.
 */
public interface HasStubCss extends HasCss {

  /** @return a list of style names */
  List<String> getStyleNames();

}
