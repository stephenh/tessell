package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

public interface IsTextBoxBase extends IsFocusWidget, HasChangeHandlers, HasText, HasName, HasValue<String> {

  String getSelectedText();

  int getSelectionLength();

  boolean isReadOnly();

  void setCursorPos(int pos);

  void setReadOnly(boolean readOnly);

  void setSelectionRange(int pos, int length);

  void setTextAlignment(TextAlignConstant align);
}
