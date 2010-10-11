package org.gwtmpv.widgets;

public interface IsHTMLPanel extends IsComplexPanel {

  void add(IsWidget widget, IsElement elem);

  void addAndReplaceElement(IsWidget widget, IsElement elem);

  void addAndReplaceElement(IsWidget widget, String id);

}
