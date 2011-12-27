package org.tessell.widgets.form.lines;

import static org.tessell.widgets.Widgets.newTextList;

import org.tessell.model.dsl.Binder;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.util.WidgetUtils;
import org.tessell.widgets.IsTextBox;
import org.tessell.widgets.IsTextList;
import org.tessell.widgets.form.FormPresenter;

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
