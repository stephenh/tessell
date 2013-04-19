package org.tessell.bootstrap;

import org.tessell.gwt.user.client.ui.StubTextBox;
import org.tessell.widgets.CompositeIsWidget;
import org.tessell.widgets.StubTextList;

/**
 * Provides some test friendly methods to {@link TextLine}, which is already unit testable given it extends
 * {@link CompositeIsWidget}.
 */
public class StubTextLine extends TextLine {

  @Override
  public StubTextList getErrorList() {
    return (StubTextList) super.getErrorList();
  }

  @Override
  public StubTextBox getTextBox() {
    return (StubTextBox) super.getTextBox();
  }

  public void type(final String text) {
    ((StubTextBox) box).type(text);
  }

}
