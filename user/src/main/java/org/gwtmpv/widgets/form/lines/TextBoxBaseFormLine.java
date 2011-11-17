package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.widgets.Widgets.newTextList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.util.WidgetUtils;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;
import org.gwtmpv.widgets.form.FormPresenter;

/** Adds a {@link IsTextBox} to a form. */
public abstract class TextBoxBaseFormLine extends AbstractFormLine<String> {

  protected final IsTextList errorList = newTextList();
  protected final IsTextBox textBox;

  protected TextBoxBaseFormLine(Property<String> property, IsTextBox textBox) {
    super(property);
    this.textBox = textBox;
  }

  @Override
  public void bind(final FormPresenter p, PropertyGroup all, Binder binder) {
    super.bind(p, all, binder);
    textBox.ensureDebugId(id);
    errorList.ensureDebugId(id + "-errors");
    p.watchForEnterKey(textBox);
    binder.bind(property).to(textBox, errorList);
    binder.fireChangeOnBlur(textBox);
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
  }

  @Override
  public void renderErrors(HTMLPanelBuilder hb) {
    hb.add(errorList);
  }

  @Override
  public void focus() {
    WidgetUtils.focus(textBox);
  }

  public IsTextBox getTextBox() {
    return textBox;
  }

  public IsTextList getErrorList() {
    return errorList;
  }

  public TextBoxBaseFormLine label(String label) {
    setLabel(label);
    return this;
  }

}
