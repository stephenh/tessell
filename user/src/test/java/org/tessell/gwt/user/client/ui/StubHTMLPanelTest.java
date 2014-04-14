package org.tessell.gwt.user.client.ui;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.tessell.gwt.dom.client.StubElement;

public class StubHTMLPanelTest {

  public StubHTMLPanel p = new StubHTMLPanel();

  @Test
  public void testInsertDomInsertFalse() {
    StubTextBox b = new StubTextBox();
    StubElement e = new StubElement();
    p.insert(b, e, 0, false);
    assertThat(p.getIsWidget(0), is((IsWidget) b));
    assertThat(p.getAddedTo(e).get(0), is((com.google.gwt.user.client.ui.IsWidget) b));
  }

  @Test
  public void testInsertDomInsertTrue() {
    StubTextBox b = new StubTextBox();
    StubElement e = new StubElement();
    p.insert(b, e, 0, true);
    assertThat(p.getIsWidget(0), is((IsWidget) b));
    assertThat(p.getAddedTo(e).get(0), is((com.google.gwt.user.client.ui.IsWidget) b));
  }

}
