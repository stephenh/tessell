package org.gwtmpv.widgets.form.actions;

import static org.gwtmpv.widgets.Widgets.newButton;

import org.gwtmpv.model.commands.DispatchUiCommand;
import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.util.Inflector;
import org.gwtmpv.widgets.IsButton;
import org.gwtmpv.widgets.form.FormPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ButtonFormAction implements FormAction {

  private final IsButton button = newButton();
  private final UiCommand command;
  private final String text;

  public ButtonFormAction(final UiCommand command, String text) {
    this.command = command;
    this.text = text;
    button.setText(text);
    button.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        command.execute();
      }
    });
  }

  @Override
  public void bind(FormPresenter p, Binder binder) {
    String id = p.getId() + "-" + Inflector.camelize(text);
    button.ensureDebugId(id);
    binder.when(command.enabled()).is(true).show(button);
    if (command instanceof DispatchUiCommand) {
      binder.when(((DispatchUiCommand<?, ?>) command).active()).is(false).enable(button);
    }
  }

  @Override
  public void renderAction(HTMLPanelBuilder hb) {
    hb.add(button);
  }

  @Override
  public void trigger() {
    command.execute();
  }

  public IsButton getButton() {
    return button;
  }

}
