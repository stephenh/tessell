package org.tessell.bootstrap;

import static org.tessell.bootstrap.views.AppViews.newMoneyLineView;

import org.tessell.bootstrap.views.IsMoneyLineView;

import com.google.gwt.user.client.ui.HasEnabled;

/** Provides boilerplate HTML/behavior for text boxes representing money in bizstrap forms. */
public class MoneyLine extends AbstractTextLine implements HasEnabled {

  private final IsMoneyLineView view = newMoneyLineView();

  public MoneyLine() {
    setTextBox(view.textBox(), view);
    setInputPrepend(true);
  }

}