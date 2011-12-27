package org.tessell.widgets;

import com.google.gwt.i18n.client.HasDirection;

public interface IsTextBox extends IsTextBoxBase, HasDirection {

  int getMaxLength();

  int getVisibleLength();

  void setMaxLength(int length);

  void setVisibleLength(int length);

}
