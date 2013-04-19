package org.tessell.bootstrap;

import static org.tessell.widgets.Widgets.newTextBox;

import org.tessell.gwt.user.client.ui.IsTextBox;

/** Provides boilerplate HTML/behavior for text boxes in bizstrap forms. */
public class TextLine extends AbstractTextLine {

  protected final IsTextBox box = newTextBox();

  public TextLine() {
    setTextBox(box, box);
  }

}