package org.tessell.bootstrap;

import org.tessell.gwt.user.client.ui.StubListBox;
import org.tessell.widgets.CompositeIsWidget;
import org.tessell.widgets.StubTextList;

/**
 * Provides some test friendly methods to {@link SelectLine}, which is already unit testable given it extends
 * {@link CompositeIsWidget}.
 */
public class StubSelectLine extends SelectLine {

  @Override
  public StubTextList getErrorList() {
    return (StubTextList) super.getErrorList();
  }

  public void select(final String text) {
    ((StubListBox) box).select(text);
  }

}
