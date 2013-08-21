package org.tessell.gwt.user.client.ui;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.gwt.event.dom.client.KeyCodes;

public class StubTextBoxBaseTest {

  private final StubTextBoxBase box = new StubTextBoxBase();

  @Test
  public void testDelete() {
    box.type("as");
    assertThat(box.getValue(), is("as"));
    box.press(KeyCodes.KEY_DELETE);
    assertThat(box.getValue(), is("a"));
    box.press(KeyCodes.KEY_DELETE);
    assertThat(box.getValue(), is(""));
    box.press(KeyCodes.KEY_DELETE);
    assertThat(box.getValue(), is(""));
  }

}
