package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.widgets.Widgets.newTextList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.util.Inflector;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;
import org.gwtmpv.widgets.form.FormPresenter;

/** Adds a {@link IsTextBox} to a form. */
public abstract class TextBoxBaseFormLine implements FormLine {

  private String id;
  private final Property<String> property;
  private final IsTextBox textBox;
  private final IsTextList errorList = newTextList();

  protected TextBoxBaseFormLine(Property<String> property, IsTextBox textBox) {
    this.property = property;
    this.textBox = textBox;
  }

  @Override
  public void bind(final FormPresenter p, PropertyGroup all, Binder binder) {
    id = p.getId() + "-" + Inflector.camelize(property.getName());
    textBox.getIsElement().setId(id);
    errorList.getIsElement().setId(id + "-errors");
    p.watchForEnterKey(textBox);
    binder.bind(property).to(textBox, errorList);
    all.add(property);
  }

  @Override
  public void renderLabel(HTMLPanelBuilder hb) {
    hb.add("<label for=\"" + id + "\">");
    hb.add(property.getName());
    hb.add("</label>");
  }

  @Override
  public void renderValue(HTMLPanelBuilder hb) {
    hb.add(textBox);
  }

  @Override
  public void renderErrors(HTMLPanelBuilder hb) {
    hb.add(errorList);
  }

  public IsTextBox getTextBox() {
    return textBox;
  }

  public IsTextList getErrorList() {
    return errorList;
  }

}
