package org.tessell.bootstrap;

import static org.tessell.widgets.Widgets.newPasswordTextBox;

import org.tessell.gwt.user.client.ui.IsPasswordTextBox;

/** Provides boilerplate HTML/behavior for password boxes in bizstrap forms. */
public class PasswordTextLine extends AbstractTextLine {

  protected final IsPasswordTextBox box = newPasswordTextBox();

  public PasswordTextLine() {
    setTextBox(box, box);
  }

}