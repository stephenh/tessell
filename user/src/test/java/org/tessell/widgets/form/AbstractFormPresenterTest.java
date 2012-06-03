package org.tessell.widgets.form;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import joist.util.Join;

import org.junit.Before;
import org.junit.BeforeClass;
import org.tessell.gwt.user.client.ui.StubHTMLPanel;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.StubWidget;
import org.tessell.widgets.StubWidgetsProvider;
import org.tessell.widgets.form.FormPresenter;

public abstract class AbstractFormPresenterTest {

  protected final FormPresenter p = new FormPresenter("p");

  @BeforeClass
  public static void useStubs() {
    StubWidgetsProvider.install();
  }

  @Before
  public void resetHTMLPanelBuilderId() {
    HTMLPanelBuilder.resetId();
  }

  @Before
  public void bind() {
    p.bind();
  }

  protected StubHTMLPanel html() {
    ((StubWidget) p.getView()).fireAttached();
    return (StubHTMLPanel) p.getView().getIsWidget(0);
  }

  protected void assertHtml(String... html) {
    assertThat(html().getHtml(), is(Join.join(html, "")));
  }

  protected void assertHtml(HTMLPanelBuilder hb, String... html) {
    StubHTMLPanel p = (StubHTMLPanel) hb.toHTMLPanel();
    assertThat(p.getHtml(), is(Join.join(html, "")));
  }

}
