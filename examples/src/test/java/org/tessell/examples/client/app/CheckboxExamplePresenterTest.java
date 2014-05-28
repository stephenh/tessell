package org.tessell.examples.client.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.tessell.examples.client.views.StubCheckboxExampleView;

public class CheckboxExamplePresenterTest extends AbstractPresenterTest {

  private final CheckboxExamplePresenter p = bind(new CheckboxExamplePresenter());
  private final StubCheckboxExampleView v = (StubCheckboxExampleView) p.getView();

  @Test
  public void defaultsToNotChecked() {
    assertThat(v.label().getText(), is("Not checked"));
  }

  @Test
  public void checkingSetsTheLabel() {
    v.box().check();
    assertThat(v.label().getText(), is("Checked!"));
  }
  
  @Test
  public void uncheckingUnsetsTheLabel() {
    v.box().check();
    v.box().uncheck();
    assertThat(v.label().getText(), is("Not checked"));
  }

}
