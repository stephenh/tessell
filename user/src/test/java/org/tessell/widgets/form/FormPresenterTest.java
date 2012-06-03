package org.tessell.widgets.form;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.stringProperty;
import joist.util.Join;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubButton;
import org.tessell.gwt.user.client.ui.StubTextBox;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.Valid;
import org.tessell.tests.model.commands.DummyUiCommand;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.actions.ButtonFormAction;
import org.tessell.widgets.form.lines.StaticFormLine;
import org.tessell.widgets.form.lines.TextBoxFormLine;

public class FormPresenterTest extends AbstractFormPresenterTest {

  final EmployeeModel employee = new EmployeeModel();

  @Test
  public void htmlOfOneTextBox() {
    p.add(new TextBoxFormLine(employee.firstName));

    assertThat(html().getHtml(), is(Join.join(new String[] {//
      "<div class=\"form\">",//
        "<div class=\"lines\"><ol>",
        "<li>",
        "<div class=\"label\"><label for=\"p-firstName\">First Name</label></div>",//
        "<div class=\"value\"><div id=\"mpv-hb-1\"></div><div class=\"errors\"><div id=\"mpv-hb-2\"></div></div></div>",//
        "</li>",
        "</ol></div>",
        "</div>" },
      "")));
  }

  @Test
  public void allValid() {
    final StringProperty name = stringProperty("name").max(10);
    p.add(new TextBoxFormLine(name));
    name.set("0123456789a");
    assertThat(p.allValid().wasValid(), is(Valid.NO));

    name.set("1");
    assertThat(p.allValid().wasValid(), is(Valid.YES));
  }

  @Test
  public void focusFirstLine() {
    TextBoxFormLine l = new TextBoxFormLine(employee.firstName);
    p.add(l);
    p.focusFirstLine();
    assertThat(((StubTextBox) l.getTextBox()).isFocused(), is(true));
  }

  @Test
  public void customLine() {
    p.add(new TextBoxFormLine(employee.firstName));
    p.add(new StaticFormLine() {
      @Override
      public void render(FormPresenter p, FormLayout l, HTMLPanelBuilder hb) {
        hb.add("some custom html");
      }
    });

    assertThat(html().getHtml(), is(Join.join(new String[] {//
      "<div class=\"form\">",//
        "<div class=\"lines\"><ol>",
        "<li>",
        "<div class=\"label\"><label for=\"p-firstName\">First Name</label></div>",//
        "<div class=\"value\"><div id=\"mpv-hb-3\"></div><div class=\"errors\"><div id=\"mpv-hb-4\"></div></div></div>",//
        "</li>some custom html",
        "</ol></div>",
        "</div>" },
      "")));
  }

  @Test
  public void buttonTouchesProperties() {
    final DummyUiCommand command = new DummyUiCommand();
    final StringProperty name = stringProperty("name").max(10);
    p.add(new TextBoxFormLine(name));
    ButtonFormAction action = new ButtonFormAction(command, "foo");
    p.add(action);
    // given the name is not touched
    assertThat(name.isTouched(), is(false));
    // when submit is clicked
    ((StubButton) action.getButton()).click();
    // then the property is now touched
    assertThat(name.isTouched(), is(true));
    // and the command did not execute
    assertThat(command.getExecutions(), is(0));
  }

  @Test
  public void cancelButtonDoesNotTouchProperties() {
    final DummyUiCommand command = new DummyUiCommand();
    final StringProperty name = stringProperty("name").max(10);
    p.add(new TextBoxFormLine(name));
    ButtonFormAction action = new ButtonFormAction(command, "cancel", false);
    p.add(action);
    // given the name is not touched
    assertThat(name.isTouched(), is(false));
    // when cancel is clicked
    ((StubButton) action.getButton()).click();
    // then the property is still not touched
    assertThat(name.isTouched(), is(false));
    // and the command executed
    assertThat(command.getExecutions(), is(1));
  }
}
