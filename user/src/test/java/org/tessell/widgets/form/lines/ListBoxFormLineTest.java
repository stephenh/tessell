package org.tessell.widgets.form.lines;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.tessell.model.properties.NewProperty.enumProperty;
import static org.tessell.widgets.form.lines.NewFormLine.newListBoxFormLine;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubListBox;
import org.tessell.model.dsl.ListBoxAdaptor;
import org.tessell.model.dsl.ListBoxHumanizerAdaptor;
import org.tessell.model.properties.EnumProperty;
import org.tessell.widgets.StubTextList;
import org.tessell.widgets.form.AbstractFormPresenterTest;
import org.tessell.widgets.form.EmployeeModel;
import org.tessell.widgets.form.EmployerDto;

public class ListBoxFormLineTest extends AbstractFormPresenterTest {

  final EmployeeModel employee = new EmployeeModel();
  final ArrayList<EmployerDto> employers = new ArrayList<EmployerDto>();
  {
    employers.add(new EmployerDto(1, "Employer One"));
    employers.add(new EmployerDto(2, "Employer Two"));
  }

  @Test
  public void htmlOfOneListBox() {
    p.add(newListBoxFormLine(employee.employerId, employers, new EmployerDtoAdaptor()));
    assertHtml("<div class=\"form\">",//
      "<div class=\"lines\"><ol>",
      "<li>",
      "<div class=\"label\"><label for=\"p-employerId\">Employer Id</label></div>",
      "<div class=\"value\"><div id=\"mpv-hb-1\"></div><div class=\"errors\"><div id=\"mpv-hb-2\"></div></div></div>",
      "</li>",
      "</ol></div>",
      "</div>");
  }

  @Test
  public void listBoxIsBound() {
    p.add(newListBoxFormLine(employee.employerId, employers, new EmployerDtoAdaptor()));
    assertThat(listBox("p-employerId").getItemCount(), is(2));
    assertThat(listBox("p-employerId").getItems(), contains("Employer One", "Employer Two"));
  }

  @Test
  public void listBoxErrorsAreBound() {
    // give the user a null option
    employee.employerId.req();
    employers.add(0, null);
    p.add(newListBoxFormLine(employee.employerId, employers, new EmployerDtoAdaptor()));
    assertThat(employee.employerId.isTouched(), is(false));
    // now select it (which will touch the property)
    listBox("p-employerId").select("");
    assertThat(employee.employerId.isTouched(), is(true));
    assertThat(errorList("p-employerId-errors").getList(), contains("Employer Id is required"));
  }

  @Test
  public void listBoxDoesNotTouchIfNullIsAvailable() {
    employee.employerId.req();
    employers.add(0, null);
    p.add(newListBoxFormLine(employee.employerId, employers, new EmployerDtoAdaptor()));
    assertThat(employee.employerId.get(), is(nullValue()));
    assertThat(employee.employerId.isTouched(), is(false));
    assertThat(errorList("p-employerId-errors").getList().size(), is(0));
  }

  @Test
  public void listBoxIsBoundCurrentlyAssignsFirstvalue() {
    assertThat(employee.employerId.get(), is(nullValue()));
    p.add(newListBoxFormLine(employee.employerId, employers, new EmployerDtoAdaptor()));
    assertThat(employee.employerId.get(), is(1));
    assertThat(employee.employerId.isTouched(), is(false));
  }

  @Test
  public void listBoxToAnEnum() {
    EnumProperty<Foo> foo = enumProperty("foo");
    p.add(newListBoxFormLine(foo, Arrays.asList(Foo.values()), new ListBoxHumanizerAdaptor<Foo>()));
    assertThat(listBox("p-foo").getItemCount(), is(2));
    assertThat(listBox("p-foo").getItems(), contains("Value One", "Value Two"));
  }

  private StubListBox listBox(String id) {
    return (StubListBox) html().findById(id);
  }

  private StubTextList errorList(String id) {
    return (StubTextList) html().findById(id);
  }

  private static final class EmployerDtoAdaptor implements ListBoxAdaptor<Integer, EmployerDto> {
    public String toDisplay(EmployerDto option) {
      return option == null ? "" : option.name;
    }

    public Integer toValue(EmployerDto option) {
      return option == null ? null : option.id;
    }
  }

  private static enum Foo {
    VALUE_ONE, VALUE_TWO
  }

}
