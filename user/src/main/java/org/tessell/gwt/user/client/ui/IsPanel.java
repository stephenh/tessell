package org.tessell.gwt.user.client.ui;

public interface IsPanel extends IsWidget, HasIsWidgets {

  void add(com.google.gwt.user.client.ui.IsWidget isWidget);

  boolean remove(com.google.gwt.user.client.ui.IsWidget isWidget);

}
