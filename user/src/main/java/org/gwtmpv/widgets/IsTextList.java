package org.gwtmpv.widgets;

public interface IsTextList extends IsWidget {

  boolean hasErrors();

  void add(String text);

  void remove(String text);

  void clear();

  boolean isEnabled();

  void setEnabled(boolean enabled);

}
