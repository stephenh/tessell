package org.tessell.examples.client.app;

import static org.tessell.examples.client.views.AppViews.newCheckboxExampleView;
import static org.tessell.model.dsl.TakesValues.textOf;
import static org.tessell.model.properties.NewProperty.booleanProperty;

import org.tessell.examples.client.views.IsCheckboxExampleView;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.presenter.BasicPresenter;

/** An example of checkbox binding. */
public class CheckboxExamplePresenter extends BasicPresenter<IsCheckboxExampleView> {

  private final BooleanProperty checked = booleanProperty("checked");

  public CheckboxExamplePresenter() {
    super(newCheckboxExampleView());
  }

  @Override
  public void onBind() {
    super.onBind();

    // two-way binding between checked and view.box()
    binder.bind(checked).to(view.box());

    // one-way binding between our derived value and view.label()'s text
    binder.bind(() -> checked.isTrue() ? "Checked!" : "Not checked").to(textOf(view.label()));
  }

}
