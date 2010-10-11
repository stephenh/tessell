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
    p.addTextBox(employee.firstName);
    assertHtml("<div>",//
      "<dl>",
      "<dt>First Name</dt>",
      "<dd><div id=\"mpv-hb-1\"/></dd>",
      "<div id=\"mpv-hb-2\"/>",
      "</dl>",
      "</div>");
  }

  @Test
  public void htmlOfTwoTextBoxes() {
    p.addTextBox(employee.firstName);
    p.addTextBox(employee.lastName);
    assertHtml("<div>",//
      "<dl>",
      "<dt>First Name</dt>",
      "<dd><div id=\"mpv-hb-1\"/></dd>",
      "<div id=\"mpv-hb-2\"/>",
      "</dl>",
      "<dl>",
      "<dt>Last Name</dt>",
      "<dd><div id=\"mpv-hb-3\"/></dd>",
      "<div id=\"mpv-hb-4\"/>",
      "</dl>",
      "</div>");
  }

  @Test
  public void widgetsAreBoundForOneTextBox() {
    employee.firstName.set("bob");
    p.addTextBox(employee.firstName);

    assertThat(tb("mpv-hb-1").getText(), is("bob"));
  }

  @Test
  public void widgetsAreBoundTwoWayForOneTextBox() {
    employee.firstName.set("bob");
    p.addTextBox(employee.firstName);

    tb("mpv-hb-1").type("fred");
    assertThat(employee.firstName.get(), is("fred"));
  }

  private StubTextBox tb(String id) {
    return (StubTextBox) html().getReplaced(id);
  }

}
