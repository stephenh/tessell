package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.IsWidget;
import org.tessell.widgets.StubWidget;

public abstract class StubPanel extends StubWidget implements IsPanel {

  @Override
  protected IsWidget findInChildren(String id) {
    return findInChildren(iteratorIsWidgets(), id);
  }

}
