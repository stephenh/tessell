package org.tessell.model.dsl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.tessell.testing.TessellMatchers.hasStyle;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubFlowPanel;
import org.tessell.gwt.user.client.ui.StubTextBox;

import com.google.gwt.user.client.TakesValue;

public class TakesValuesTest {

  @Test
  public void debugId() {
    StubTextBox t = new StubTextBox();
    TakesValue<String> id = TakesValues.debugId(t);
    id.setValue("foo");
    assertThat(t.getIsElement().getAttribute("id"), is("foo"));
    assertThat(id.getValue(), is("foo"));
  }

  @Test
  public void styleOf() {
    StubFlowPanel p = new StubFlowPanel();
    TakesValue<String> style = TakesValues.styleOf(p);
    assertThat(style.getValue(), is(nullValue()));

    style.setValue("foo");
    assertThat(p, hasStyle("foo"));
    assertThat(style.getValue(), is("foo"));

    style.setValue("bar");
    assertThat(p, hasStyle("bar"));
    assertThat(p, not(hasStyle("foo")));
    assertThat(style.getValue(), is("bar"));

    style.setValue(null);
    assertThat(p, not(hasStyle("bar")));
    assertThat(style.getValue(), is(nullValue()));

    style.setValue("");
    assertThat(style.getValue(), is(nullValue()));
  }
}
