package org.gwtmpv.widgets.form;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import joist.util.Join;

import org.junit.Test;

public class FormPresenterTest extends AbstractFormPresenterTest {

  final EmployeeModel employee = new EmployeeModel();

  @Test
  public void htmlOfOneTextBox() {
    p.addTextBox(employee.firstName);

    assertThat(html().getHtml(), is(Join.join(new String[] {//
      "<div>",//
        "<dl>",//
        "<dt>First Name</dt>",//
        "<dd><div id=\"mpv-hb-1\"/></dd>",//
        "<div id=\"mpv-hb-2\"/>",//
        "</dl>",
        "</div>" },
      "")));
  }

}
