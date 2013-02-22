package org.tessell.widgets;

import java.util.Iterator;

import org.tessell.gwt.user.client.ui.IsWidget;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * An extension of {@link CompositeIsWidget} to use if you want
 * to have child widgets in an {@code ui.xml} file.
 *
 * Specifically, something like:
 * 
 * <my:Foo>
 *   <gwt:FlowPanel />
 * </my:Foo>
 */
public abstract class CompositeHasIsWidgets extends CompositeIsWidget implements HasWidgets {

  protected abstract void addIsWidget(IsWidget w);

  @Override
  public void add(Widget w) {
    // by virtue of using Tessell, we assume this is an IsWidget
    addIsWidget((IsWidget) w);
  }

  // for Tessell-generated GwtXxxView classes, CompositeIsWidget is a
  // common way to in Tessell to build Widget-less/MVP components.
  // But that means they won't match the add(Widget) method of
  // HasWidgets, and we can't add add(IsWidget) (see class javadoc),
  // so just add this here.
  public void add(CompositeIsWidget w) {
    addIsWidget(w);
  }

  // for Tessell-generated StubXxxView classes, because we can't have
  // an add(IsWidget) (see class javadoc)
  public void add(IsStubWidget w) {
    addIsWidget((IsWidget) w);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("Not expected to be called by UiBinder");
  }

  @Override
  public Iterator<Widget> iterator() {
    throw new UnsupportedOperationException("Not expected to be called by UiBinder");
  }

  @Override
  public boolean remove(Widget w) {
    throw new UnsupportedOperationException("Not expected to be called by UiBinder");
  }

}
