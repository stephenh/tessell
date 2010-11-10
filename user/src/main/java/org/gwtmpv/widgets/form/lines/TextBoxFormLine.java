package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.widgets.Widgets.newTextBox;
import static org.gwtmpv.widgets.Widgets.newTextList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.util.Inflector;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;

public class TextBoxFormLine implements FormLine {

  private String id;
  private final String label;
  private final Property<String> property;
  private final IsTextBox textBox = newTextBox();
  private final IsTextList errorList = newTextList();

  public TextBoxFormLine(String label, Property<String> property) {
    this.label = label;
    this.property = property;
  }

  public TextBoxFormLine(Property<String> property) {
    label = property.getName();
    this.property = property;
  }

  @Override
  public void bind(String formId, Binder binder) {
    id = formId + "-" + Inflector.camelize(property.getName());
    textBox.getIsElement().setId(id);
    binder.bind(property).to(textBox, errorList);
  }

  @Override
  public void renderLabel(HTMLPanelBuilder hb) {
    hb.add("<label for=\"" + id + "\">");
    hb.add(label);
    hb.add("</label>");
  }

  @Override
  public void renderValue(HTMLPanelBuilder hb) {
    hb.add(textBox);
    hb.add(errorList);
  }

}
