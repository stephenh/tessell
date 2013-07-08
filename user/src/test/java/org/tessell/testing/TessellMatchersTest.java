package org.tessell.testing;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.tessell.testing.TessellMatchers.*;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubTextBox;
import org.tessell.widgets.StubTextList;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;

public class TessellMatchersTest {

  @Test
  public void hasNoStylesSuccess() {
    StubTextBox t = new StubTextBox();
    assertThat(t, hasNoStyles());
  }

  @Test
  public void hasNoStylesFailure() {
    StubTextBox t = new StubTextBox();
    t.addStyleName("a");
    try {
      assertThat(t, hasNoStyles());
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: style is an empty collection\n     but: style was <[a]>"));
    }
  }

  @Test
  public void hasStyleSuccess() {
    StubTextBox t = new StubTextBox();
    t.addStyleName("a");
    t.addStyleName("b");
    assertThat(t, hasStyle("a"));
  }

  @Test
  public void hasStyleFailure() {
    StubTextBox t = new StubTextBox();
    try {
      assertThat(t, hasStyle("a"));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: style is a collection containing \"a\"\n     but: style was \"\""));
    }
  }

  @Test
  public void hasNoErrorsSuccess() {
    StubTextList l = new StubTextList();
    assertThat(l, hasNoErrors());
  }

  @Test
  public void hasNoErrorsFailure() {
    StubTextList l = new StubTextList();
    l.add("a");
    try {
      assertThat(l, hasNoErrors());
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: errors is an empty collection\n     but: errors had <[a]>"));
    }
  }

  @Test
  public void hasErrorsSuccess() {
    StubTextList l = new StubTextList();
    l.add("a");
    l.add("b");
    assertThat(l, hasErrors("a", "b"));
  }

  @Test
  public void hasErrorsFailure() {
    StubTextList l = new StubTextList();
    l.add("a");
    try {
      assertThat(l, hasErrors("b"));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: errors is an iterable containing [\"b\"]\n     but: errors had item 0: was \"a\""));
    }
  }

  @Test
  public void hasCssValueSuccess() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setColor("blue");
    assertThat(t, hasCssValue("color", "blue"));
  }

  @Test
  public void hasCssValueFailure() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setColor("blue");
    try {
      assertThat(t, hasCssValue("color", "red"));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: color is \"red\"\n     but: color was \"blue\""));
    }
  }

  @Test
  public void shownSuccessWhenUnset() {
    StubTextBox t = new StubTextBox();
    assertThat(t, is(shown()));
  }

  @Test
  public void shownSuccessWhenDisplayBlock() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setDisplay(Display.BLOCK);
    assertThat(t, is(shown()));
  }

  @Test
  public void shownSuccessWhenDisplayInline() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setDisplay(Display.INLINE);
    assertThat(t, is(shown()));
  }

  @Test
  public void shownSuccessWhenDisplayInlineBlock() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setDisplay(Display.INLINE_BLOCK);
    assertThat(t, is(shown()));
  }

  @Test
  public void shownFailureWhenDisplayNone() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setDisplay(Display.NONE);
    try {
      assertThat(t, is(shown()));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: is showing because display is not \"none\"\n     but: display was \"none\""));
    }
  }

  @Test
  public void hiddenSuccess() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setDisplay(Display.NONE);
    assertThat(t, is(hidden()));
  }

  @Test
  public void hiddenFailure() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setDisplay(Display.BLOCK);
    try {
      assertThat(t, is(hidden()));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: is hidden because display is \"none\"\n     but: display was \"block\""));
    }
  }

  @Test
  public void visibleSuccessWhenUnset() {
    StubTextBox t = new StubTextBox();
    assertThat(t, is(visible()));
  }

  @Test
  public void visibleSuccessWhenVisible() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setVisibility(Visibility.VISIBLE);
    assertThat(t, is(visible()));
  }

  @Test
  public void visibleFailure() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setVisibility(Visibility.HIDDEN);
    try {
      assertThat(t, is(visible()));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: is visible because visibility is not \"hidden\"\n     but: visibility was \"hidden\""));
    }
  }

  @Test
  public void invisibleSuccess() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setVisibility(Visibility.HIDDEN);
    assertThat(t, is(invisible()));
  }

  @Test
  public void invisibleFailureWhenUnset() {
    StubTextBox t = new StubTextBox();
    try {
      assertThat(t, is(invisible()));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: is invisible because visibility is \"hidden\"\n     but: visibility was null"));
    }
  }

  @Test
  public void invisibleFailureWhenVisible() {
    StubTextBox t = new StubTextBox();
    t.getStyle().setVisibility(Visibility.VISIBLE);
    try {
      assertThat(t, is(invisible()));
      fail();
    } catch (AssertionError ae) {
      assertThat(ae.getMessage(), is("\nExpected: is invisible because visibility is \"hidden\"\n     but: visibility was \"visible\""));
    }
  }
}
