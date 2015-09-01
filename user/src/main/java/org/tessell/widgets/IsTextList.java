package org.tessell.widgets;

import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.util.ListDiff.ListLike;

public interface IsTextList extends IsWidget, ListLike<String> {

  boolean hasErrors();

  void add(String text);

  void remove(String text);

  void clear();

  boolean isEnabled();

  void setEnabled(boolean enabled);

  String getChildTag();

  void setChildTag(String childTag);

  String getChildStyleName();

  void setChildStyleName(String childStyleName);

}
