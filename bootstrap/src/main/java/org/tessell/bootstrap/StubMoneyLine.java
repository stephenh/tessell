package org.tessell.bootstrap;

import org.tessell.gwt.user.client.ui.StubTextBox;
import org.tessell.widgets.CompositeIsWidget;
import org.tessell.widgets.StubTextList;

/**
 * Provides some test friendly methods to {@link MoneyLine}, which is already unit testable given it extends
 * {@link CompositeIsWidget}.
 */
public class StubMoneyLine extends MoneyLine {

  @Override
  public StubTextList getErrorList() {
    return (StubTextList) super.getErrorList();
  }

  public void type(final String text) {
    ((StubTextBox) super.getTextBox()).type(text);
  }
}
