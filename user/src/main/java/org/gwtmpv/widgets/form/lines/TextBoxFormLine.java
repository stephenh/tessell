package org.gwtmpv.widgets.form.lines;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;
import org.gwtmpv.widgets.Widgets;

public class TextBoxFormLine implements FormLine {

  private final Property<String> property;
  private final IsTextBox textBox;
  private final IsTextList errorList;

  public TextBoxFormLine(Widgets widgets, Binder binder, Property<String> property) {
    this.property = property;
    textBox = widgets.newTextBox();
    errorList = widgets.newTextList();
    binder.bind(property).to(textBox);
  }

  @Override
  public void render(HTMLPanelBuilder hb) {
    hb.add("<dl>");
    hb.add("<dt>" + getName() + "</dt>");
    hb.add("<dd>");
    hb.add(textBox);
    hb.add("</dd>");
    hb.add(errorList);
    hb.add("</dl>");
  }

  protected String getName() {
    return property.getName();
  }

}
