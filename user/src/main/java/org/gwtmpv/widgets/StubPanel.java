package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;

public abstract class StubPanel extends StubWidget implements IsPanel {

  @Override
  public void add(final Widget w) {
    throw new IllegalArgumentException("This is a stub.");
  }

  @Override
  public Iterator<Widget> iterator() {
    throw new IllegalArgumentException("This is a stub.");
  }

  @Override
  public boolean remove(final Widget w) {
    throw new IllegalArgumentException("This is a stub.");
  }

}