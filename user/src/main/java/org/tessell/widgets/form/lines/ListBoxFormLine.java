package org.tessell.widgets.form.lines;

import static org.tessell.widgets.Widgets.newListBox;
import static org.tessell.widgets.Widgets.newTextList;

import java.util.ArrayList;

import org.tessell.model.dsl.Binder;
import org.tessell.model.dsl.ListBoxAdaptor;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.util.WidgetUtils;
import org.tessell.widgets.IsListBox;
import org.tessell.widgets.IsTextList;
import org.tessell.widgets.form.FormPresenter;

/**
 * Adds a {@link IsListBox} to a form.
 * 
 * @param T the type of the property to set when selected
 * @param O the type of the options shown in the list
 */
public class ListBoxFormLine<T, O> extends AbstractFormLine<T> {

  protected final ArrayList<O> possibleValues;
  protected final ListBoxAdaptor<T, O> adaptor;
  protected final IsTextList errorList = newTextList();
  protected final IsListBox listBox = newListBox();

  public ListBoxFormLine(Property<T> property, ArrayList<O> possibleValues, ListBoxAdaptor<T, O> adaptor) {
    super(property);
    this.possibleValues = possibleValues;
    this.adaptor = adaptor;
  }

  @Override
  public void bind(FormPresenter p, PropertyGroup all, Binder binder) {
    super.bind(p, all, binder);
    listBox.ensureDebugId(id);
    errorList.ensureDebugId(id + "-errors");
    binder.bind(property).to(listBox, possibleValues, adaptor);
  }

  @Override
  public void renderLabel(HTMLPanelBuilder hb) {
    hb.add("<label for=\"" + id + "\">");
    hb.add(label);
    hb.add("</label>");
  }

  @Override
  public void renderValue(HTMLPanelBuilder hb) {
    hb.add(listBox);
  }

  @Override
  public void renderErrors(HTMLPanelBuilder hb) {
    hb.add(errorList);
  }

  @Override
  public void focus() {
    WidgetUtils.focus(listBox);
  }

}
