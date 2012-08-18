package org.tessell.widgets.form.actions;

import static org.tessell.model.dsl.Binder.when;
import static org.tessell.widgets.Widgets.newButton;

import org.tessell.gwt.user.client.ui.IsButton;
import org.tessell.model.commands.DispatchUiCommand;
import org.tessell.model.commands.UiCommand;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.util.Inflector;
import org.tessell.widgets.form.FormPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ButtonFormAction implements FormAction {

  private final IsButton button = newButton();
  private final UiCommand command;
  private final String text;
  private boolean addOnlyIf;

  public ButtonFormAction(final UiCommand command, String text) {
    this(command, text, true);
  }

  public ButtonFormAction(final UiCommand command, String text, boolean addOnlyIf) {
    this.command = command;
    this.text = text;
    this.addOnlyIf = addOnlyIf;
    button.setText(text);
    button.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        command.execute();
      }
    });
  }

  @Override
  public void bind(FormPresenter p) {
    String id = p.getId() + "-" + Inflector.camelize(text);
    button.ensureDebugId(id);
    if (addOnlyIf) {
      command.addOnlyIf(p.allValid());
    }
    when(command.enabled()).is(true).show(button);
    if (command instanceof DispatchUiCommand) {
      when(((DispatchUiCommand<?, ?>) command).active()).is(false).enable(button);
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

  public void setAddOnlyIf(boolean addOnlyIf) {
    this.addOnlyIf = addOnlyIf;
  }

}
