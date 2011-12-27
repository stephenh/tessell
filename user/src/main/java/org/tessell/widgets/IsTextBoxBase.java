package org.tessell.widgets;

import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;

@SuppressWarnings("deprecation")
public interface IsTextBoxBase extends IsValueBoxBase<String> {

  @Deprecated
  void setTextAlignment(TextAlignConstant align);

}
