package org.tessell.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.tessell.model.properties.NewProperty.stringProperty;
import static org.tessell.testing.TessellMatchers.hasStyle;

import org.junit.Before;
import org.junit.Test;
import org.tessell.model.properties.StringProperty;

public class TextLineTest extends AbstractPresenterTest {

  private final StubTextLine t = new StubTextLine();
  private final StringProperty s = stringProperty("s", "foo").req();

  @Before
  public void setUp() {
    t.bind(s);
  }

  @Test
  public void setsTextBoxValue() {
    assertThat(t.getTextBox().getText(), is("foo"));
  }

  @Test
  public void setsLabel() {
    assertThat(t.getLabel().getText(), is("S"));
  }

  @Test
  public void updatesProperty() {
    t.getTextBox().type("bar");
    assertThat(s.get(), is("bar"));
  }

  @Test
  public void showsValidationErrors() {
    s.set(null);
    assertThat(t.getErrorList().getList(), contains("S is required"));
  }

  @Test
  public void addsErrorStyle() {
    s.set(null);
    assertThat(t.controlGroup(), hasStyle(b.error()));
  }
}
