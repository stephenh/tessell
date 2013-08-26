package org.tessell.gwt.user.client.ui;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class StubTextBoxBaseTest {

  private final StubTextBoxBase box = new StubTextBoxBase();

  @Test
  public void testBackspace() {
    box.type("as");
    assertThat(box.getValue(), is("as"));
    box.press(KeyCodes.KEY_BACKSPACE);
    assertThat(box.getValue(), is("a"));
    box.press(KeyCodes.KEY_BACKSPACE);
    assertThat(box.getValue(), is(""));
    box.press(KeyCodes.KEY_BACKSPACE);
    assertThat(box.getValue(), is(""));
  }

  @Test
  public void testDelete() {
    box.type("as");
    assertThat(box.getValue(), is("as"));
    box.press(KeyCodes.KEY_DELETE);
    assertThat(box.getValue(), is("s"));
    box.press(KeyCodes.KEY_DELETE);
    assertThat(box.getValue(), is(""));
    box.press(KeyCodes.KEY_DELETE);
    assertThat(box.getValue(), is(""));
  }

  @Test
  public void testPressKeyCodeFiresChange() {
    box.type("as");
    final boolean[] changed = { false };
    box.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        changed[0] = true;
      }
    });
    box.press(KeyCodes.KEY_BACKSPACE);
    assertThat(changed[0], is(true));
  }

  @Test
  public void testPressKeyCodesFiresOneChange() {
    box.type("as");
    final int[] changes = { 0 };
    box.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        changes[0]++;
      }
    });
    box.press("df");
    assertThat(changes[0], is(1));
  }

  @Test
  public void testPressCharCodeFiresChange() {
    box.type("as");
    final boolean[] changed = { false };
    box.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        changed[0] = true;
      }
    });
    box.press('d');
    assertThat(changed[0], is(true));
  }

  @Test
  public void testKeyCodeMapping() {
    final int[] keyCode = { -1 };
    box.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        keyCode[0] = event.getNativeKeyCode();
      }
    });
    box.press("a");
    assertThat(keyCode[0], is(65));
    box.press("B");
    assertThat(keyCode[0], is(66));
  }
}
