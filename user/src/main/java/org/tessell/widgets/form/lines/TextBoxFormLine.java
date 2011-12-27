package org.tessell.widgets.form.lines;

import static org.tessell.widgets.Widgets.newTextBox;

import org.tessell.model.properties.Property;
import org.tessell.widgets.IsTextBox;

/** Adds a {@link IsTextBox} to a form. */
public class TextBoxFormLine extends TextBoxBaseFormLine {

  public TextBoxFormLine(Property<String> property) {
    super(property, newTextBox());
  }

}
