package org.tessell.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.stringProperty;
import static org.tessell.testing.TessellMatchers.hasErrors;
import static org.tessell.testing.TessellMatchers.hasStyle;
import static org.tessell.testing.TessellMatchers.hidden;
import static org.tessell.testing.TessellMatchers.shown;

import org.junit.Test;
import org.tessell.model.properties.StringProperty;

public class BasicLineTest extends AbstractPresenterTest {

  @Test
  public void lineInvalid() {
    final StringProperty name = stringProperty("name").req();
    final BasicLine line = new BasicLine();
    line.addToValid(name);
    name.touch();
    assertThat(line.controlGroup(), hasStyle(b.error()));
    assertThat(line.getErrorList(), hasErrors("Name is required"));
  }

  @Test
  public void optionalShownForOptionalFields() {
    final StringProperty name = stringProperty("name");
    final BasicLine line = new BasicLine();
    line.addToValid(name);
    assertThat(line.getView().optional(), is(shown()));
  }

  @Test
  public void optionalNotShownForRequiredFields() {
    final StringProperty name = stringProperty("name").req();
    final BasicLine line = new BasicLine();
    line.addToValid(name);
    assertThat(line.getView().optional(), is(hidden()));
  }

  @Test
  public void optionalHiddenByDefault() {
    final BasicLine line = new BasicLine();
    assertThat(line.getView().optional(), is(hidden()));
  }

  @Test
  public void optionalNotShownForRequiredFieldsIfTold() {
    // given we tell it to not show
    final BasicLine line = new BasicLine();
    line.alwaysHideOptionalLabel();
    // when we later add a required field
    final StringProperty name = stringProperty("name");
    line.addToValid(name);
    // then it should still be hidden
    assertThat(line.getView().optional(), is(hidden()));
  }
}
