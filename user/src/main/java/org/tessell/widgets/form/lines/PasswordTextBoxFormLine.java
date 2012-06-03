package org.tessell.widgets.form.lines;

import static org.tessell.widgets.Widgets.newPasswordTextBox;

import org.tessell.gwt.user.client.ui.IsPasswordTextBox;
import org.tessell.model.properties.Property;

/** Adds a {@link IsPasswordTextBox} to a form. */
public class PasswordTextBoxFormLine extends TextBoxBaseFormLine {

  public PasswordTextBoxFormLine(Property<String> property) {
    super(property, newPasswordTextBox());
  }

}
