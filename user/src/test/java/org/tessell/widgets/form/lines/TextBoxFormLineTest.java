package org.tessell.widgets.form.lines;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.testing.TessellMatchers.hasErrors;

import org.junit.Test;
import org.tessell.model.dsl.Binder;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.StubTextBox;
import org.tessell.widgets.StubTextList;
import org.tessell.widgets.form.AbstractFormPresenterTest;
import org.tessell.widgets.form.EmployeeModel;
import org.tessell.widgets.form.FormPresenter;
import org.tessell.widgets.form.actions.FormAction;
import org.tessell.widgets.form.lines.TextBoxBaseFormLine;
import org.tessell.widgets.form.lines.TextBoxFormLine;

public class TextBoxFormLineTest extends AbstractFormPresenterTest {

  final EmployeeModel employee = new EmployeeModel();

  @Test
  public void htmlOfOneTextBox() {
    p.add(new TextBoxFormLine(employee.firstName));
    assertHtml("<div class=\"form\">",//
      "<div class=\"lines\"><ol>",
      "<li>",
      "<div class=\"label\"><label for=\"p-firstName\">First Name</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-1\"></div><div class=\"errors\"><div id=\"mpv-hb-2\"></div></div></div>",
      "</li>",
      "</ol></div>",
      "</div>");
  }

  @Test
  public void htmlOfTwoTextBoxes() {
    p.add(new TextBoxFormLine(employee.firstName));
    p.add(new TextBoxFormLine(employee.lastName));
    assertHtml("<div class=\"form\">",//
      "<div class=\"lines\"><ol>",
      "<li>",
      "<div class=\"label\"><label for=\"p-firstName\">First Name</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-3\"></div><div class=\"errors\"><div id=\"mpv-hb-4\"></div></div></div>",
      "</li>",
      "<li>",
      "<div class=\"label\"><label for=\"p-lastName\">Last Name</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-5\"></div><div class=\"errors\"><div id=\"mpv-hb-6\"></div></div></div>",
      "</li>",
      "</ol></div>",
      "</div>");
  }

  @Test
  public void customLabel() {
    TextBoxBaseFormLine a = new TextBoxFormLine(employee.firstName).label("foo");
    HTMLPanelBuilder hb = new HTMLPanelBuilder();
    a.renderLabel(hb);
    assertHtml(hb, "<label for=\"null\">foo</label>");
  }

  @Test
  public void widgetsAreBoundForOneTextBox() {
    employee.firstName.set("bob");
    p.add(new TextBoxFormLine(employee.firstName));

    assertThat(tb("mpv-hb-1").getText(), is("bob"));
  }

  @Test
  public void widgetsAreBoundTwoWayForOneTextBox() {
    employee.firstName.set("bob");
    p.add(new TextBoxFormLine(employee.firstName));

    tb("mpv-hb-1").type("fred");
    assertThat(employee.firstName.get(), is("fred"));
  }

  @Test
  public void widgetsAreChangedOnBlur() {
    employee.firstName.req();
    p.add(new TextBoxFormLine(employee.firstName));

    tb("mpv-hb-1").blur();
    assertThat(errors("mpv-hb-2"), hasErrors("First Name is required"));
  }

  @Test
  public void enterKeySubmitsTheForm() {
    DummyAction action = new DummyAction();
    p.setDefaultAction(action);
    p.add(new TextBoxFormLine(employee.firstName));
    tb("mpv-hb-1").keyDown(KEY_ENTER);
    tb("mpv-hb-1").keyUp(KEY_ENTER);
    assertThat(action.triggered, is(true));
  }

  @Test
  public void otherKeysDoNotSubmitTheForm() {
    DummyAction action = new DummyAction();
    p.setDefaultAction(action);
    p.add(new TextBoxFormLine(employee.firstName));
    tb("mpv-hb-1").keyUp(KEY_LEFT);
    assertThat(action.triggered, is(false));
  }

  private StubTextBox tb(String id) {
    return (StubTextBox) html().getReplaced(id);
  }

  private StubTextList errors(String id) {
    return (StubTextList) html().getReplaced(id);
  }

  private static final class DummyAction implements FormAction {
    private boolean triggered;

    @Override
    public void bind(FormPresenter p, Binder binder) {
    }

    @Override
    public void renderAction(HTMLPanelBuilder hb) {
    }

    @Override
    public void trigger() {
      triggered = true;
    }
  }

}
