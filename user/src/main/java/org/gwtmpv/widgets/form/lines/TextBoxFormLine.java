package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.widgets.Widgets.newTextBox;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.IsTextBox;

/** Adds a {@link IsTextBox} to a form. */
public class TextBoxFormLine extends TextBoxBaseFormLine {

  public TextBoxFormLine(Property<String> property) {
    super(property, newTextBox());
  }

}
