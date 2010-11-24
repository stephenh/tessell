package org.gwtmpv.widgets.form.lines;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.gwtmpv.widgets.StubTextBox;
import org.gwtmpv.widgets.form.AbstractFormPresenterTest;
import org.gwtmpv.widgets.form.EmployeeModel;
import org.junit.Test;

public class TextBoxFormLineTest extends AbstractFormPresenterTest {

  final EmployeeModel employee = new EmployeeModel();

  @Test
  public void htmlOfOneTextBox() {
    p.add(new TextBoxFormLine(employee.firstName));
    assertHtml("<div class=\"form\"><ol>",//
      "<li>",
      "<div class=\"label\"><label for=\"p-firstName\">First Name</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-1\"></div><div class=\"errors\"><div id=\"mpv-hb-2\"></div></div></div>",
      "</li>",
      "</ol></div>");
  }

  @Test
  public void htmlOfTwoTextBoxes() {
    p.add(new TextBoxFormLine(employee.firstName));
    p.add(new TextBoxFormLine(employee.lastName));
    assertHtml("<div class=\"form\"><ol>",//
      "<li>",
      "<div class=\"label\"><label for=\"p-firstName\">First Name</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-1\"></div><div class=\"errors\"><div id=\"mpv-hb-2\"></div></div></div>",
      "</li>",
      "<li>",
      "<div class=\"label\"><label for=\"p-lastName\">Last Name</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-3\"></div><div class=\"errors\"><div id=\"mpv-hb-4\"></div></div></div>",
      "</li>",
      "</ol></div>");
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

  private StubTextBox tb(String id) {
    return (StubTextBox) html().getReplaced(id);
  }

}
