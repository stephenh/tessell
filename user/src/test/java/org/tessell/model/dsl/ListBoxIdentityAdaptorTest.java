package org.tessell.model.dsl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class ListBoxIdentityAdaptorTest {

  @Test
  public void testToDisplay() {
    ListBoxIdentityAdaptor<String> a = new ListBoxIdentityAdaptor<String>();
    assertThat(a.toDisplay("a"), is("a"));
  }

  @Test
  public void testToDisplayOfAnEnumUsesHumanizedOutput() {
    ListBoxIdentityAdaptor<Color> a = new ListBoxIdentityAdaptor<Color>();
    assertThat(a.toDisplay(Color.GREEN), is("Green"));
  }

  private static enum Color {
    GREEN, BLUE
  };

}
