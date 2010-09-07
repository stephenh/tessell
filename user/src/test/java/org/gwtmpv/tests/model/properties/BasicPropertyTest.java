package org.gwtmpv.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.gwtmpv.model.util.Listen;
import org.gwtmpv.widgets.StubHasText;
import org.gwtmpv.widgets.StubHasValue;
import org.junit.Test;

public class BasicPropertyTest {

  @Test
  public void bothWaysFiresIfAlreadyLoaded() {
    final DummyModel m = new DummyModel();
    m.name.set("dummy");
    final StubHasValue<String> v = new StubHasValue<String>();
    Listen.bothWays(v, m.name);
    assertThat(v.getValue(), is("dummy"));
  }

  @Test
  public void changingFiresIfAlreadyLoaded() {
    final DummyModel m = new DummyModel();
    m.name.set("dummy");
    final StubHasText v = new StubHasText();
    Listen.updateOnChanged(m.name.remaining(), v, "text={}");
    assertThat(v.getText(), is("text=45"));
  }

  @Test
  public void changingEventNotesWhetherItIsTheInitialValue() {
    final DummyModel m = new DummyModel();
    m.name.set("dummy");
    final StubHasText v = new StubHasText();
    Listen.updateOnChanged(m.name.remaining(), v, "text={}");
    assertThat(v.getText(), is("text=45"));
  }

}
