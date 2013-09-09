package org.tessell.gwt.user.client.ui;

import com.google.gwt.i18n.client.HasDirection;

public interface IsTextBox extends IsTextBoxBase, HasDirection {

  int getVisibleLength();

  void setVisibleLength(int length);

}
