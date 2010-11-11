package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.widgets.Widgets.newPasswordTextBox;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.IsPasswordTextBox;

/** Adds a {@link IsPasswordTextBox} to a form. */
public class PasswordTextBoxFormLine extends TextBoxBaseFormLine {

  public PasswordTextBoxFormLine(Property<String> property) {
    super(property, newPasswordTextBox());
  }

}
