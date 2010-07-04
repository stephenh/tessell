package org.gwtmpv.widgets;

import com.google.gwt.user.client.ui.HasWidgets;

public interface IsPanel extends IsWidget, HasWidgets {

  void add(IsWidget isWidget);

  boolean remove(IsWidget isWidget);

}
