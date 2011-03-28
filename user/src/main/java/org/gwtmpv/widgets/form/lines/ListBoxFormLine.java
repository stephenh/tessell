package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.widgets.Widgets.newListBox;
import static org.gwtmpv.widgets.Widgets.newTextList;

import java.util.ArrayList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.dsl.ListBoxAdaptor;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.util.Inflector;
import org.gwtmpv.util.WidgetUtils;
import org.gwtmpv.widgets.IsListBox;
import org.gwtmpv.widgets.IsTextList;
import org.gwtmpv.widgets.form.FormPresenter;

public class ListBoxFormLine<T, O> implements FormLine {

  protected final Property<T> property;
  protected final ArrayList<O> possibleValues;
  protected final ListBoxAdaptor<T, O> adaptor;
  protected final IsTextList errorList = newTextList();
  protected final IsListBox listBox = newListBox();
  private String label;
  private String id;

  public ListBoxFormLine(Property<T> property, ArrayList<O> possibleValues, ListBoxAdaptor<T, O> adaptor) {
    this.property = property;
    this.possibleValues = possibleValues;
    this.adaptor = adaptor;
    label = property.getName();
  }

  @Override
  public void bind(FormPresenter p, PropertyGroup all, Binder binder) {
    id = p.getId() + "-" + Inflector.camelize(property.getName());
    listBox.getIsElement().setId(id);
    errorList.getIsElement().setId(id + "-errors");
    binder.bind(property).to(listBox, possibleValues, adaptor);
    all.add(property);
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

  public void setLabel(String label) {
    this.label = label;
  }

}
