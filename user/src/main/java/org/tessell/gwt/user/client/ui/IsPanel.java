package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.IsWidget;

public interface IsPanel extends IsWidget, HasIsWidgets {

  void add(IsWidget isWidget);

  boolean remove(IsWidget isWidget);

}
