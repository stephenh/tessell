package org.tessell.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.stringProperty;
import static org.tessell.testing.TessellMatchers.hasErrors;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;

public class RadioButtonLineTest extends AbstractPresenterTest {

  final StringProperty name = stringProperty("name");

  @Test
  public void formInvalid() {
    name.req();
    final RadioButtonLine l = new RadioButtonLine("l");
    l.bind(name).addValue("one", "one label");
    name.touch();
    assertThat(l.valid().get(), is(false));
    assertThat(l.getErrorList(), hasErrors("Name is required"));
  }
}
