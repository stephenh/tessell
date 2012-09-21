package org.tessell.model.dsl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
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

}
