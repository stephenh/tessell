package org.tessell.bootstrap;

import org.tessell.gwt.user.client.ui.StubPasswordTextBox;
import org.tessell.widgets.CompositeIsWidget;
import org.tessell.widgets.StubTextList;

/**
 * Provides some test friendly methods to {@link TextLine}, which is already unit testable given it extends
 * {@link CompositeIsWidget}.
 */
public class StubPasswordTextLine extends PasswordTextLine {

  @Override
  public StubTextList getErrorList() {
    return (StubTextList) super.getErrorList();
  }

  public void type(final String text) {
    ((StubPasswordTextBox) box).type(text);
  }

}
