package org.tessell.widgets;

import com.google.gwt.user.client.ui.SimplePanel;

/** Base class for views generated from {@code ui.xml} files.
 *
 * Extending {@code SimplePanel} means we've inserted another {@code div}
 * into the tree, but it also means we can return ourselves from {@code asWidget}.
 *
 * This means that, later, the client can retrieve the widget from containers
 * (like other panels) and cast us back to the view subclasses.
 *
 * (Previously we returned the uibinder object itself from asWidget, but
 * then the client had no reference back to the view.)
 *
 * (We also extend {@code SimplePanel} instead of {@code Composite} so
 * that technically our uibinder widget can be stolen from us and put
 * into another parent (like {@link RowTable} does).)
 */
public class DelegateIsWidget extends SimplePanel implements IsWidget {

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

}
