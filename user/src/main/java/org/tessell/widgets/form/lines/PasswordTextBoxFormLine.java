package org.tessell.widgets.form.lines;

import static org.tessell.widgets.Widgets.newPasswordTextBox;

import org.tessell.model.properties.Property;
import org.tessell.widgets.IsPasswordTextBox;

/** Adds a {@link IsPasswordTextBox} to a form. */
public class PasswordTextBoxFormLine extends TextBoxBaseFormLine {

  public PasswordTextBoxFormLine(Property<String> property) {
    super(property, newPasswordTextBox());
  }

}
