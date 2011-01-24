package org.gwtmpv.model.dsl;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;

import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.IsTextList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

/** Binds various things to a command. */
public class UiCommandBinder {

  private final Binder binder;
  private final UiCommand command;

  public UiCommandBinder(final Binder binder, final UiCommand command) {
    this.binder = binder;
    this.command = command;
  }

  /** Binds clicks from {@code clickable} to our command, and our errors to {@code errors}. */
  public UiCommandBinder to(final HasClickHandlers clickable, final IsTextList errors) {
    return to(clickable).errorsTo(errors);
  }

  /** Binds "enter" from key down handlers to our command. */
  public UiCommandBinder toEnterKey(final HasKeyDownHandlers... keyDowns) {
    // listen to key down instead of key up so that the user hit "enter",
    // and then our source got focus on the place change, that we don't
    // immediately respond to the the enter key up event
    for (HasKeyDownHandlers keyDown : keyDowns) {
      binder.registerHandler(keyDown.addKeyDownHandler(new KeyDownHandler() {
        public void onKeyDown(KeyDownEvent event) {
          if (event.getNativeKeyCode() == KEY_ENTER) {
            command.execute();
          }
        }
      }));
    }
    return this;
  }

  /** Has our command execute only if {@code onlyIf} is true. */
  public UiCommandBinder onlyIf(final Property<Boolean> onlyIf) {
    command.addOnlyIf(onlyIf);
    return this;
  }

  /** Binds clicks from {@code clickable} to our command. */
  public UiCommandBinder to(final HasClickHandlers clickable) {
    binder.registerHandler(clickable.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        command.execute();
      }
    }));
    return this;
  }

  /** Binds errors for our command to {@code errors}. */
  public UiCommandBinder errorsTo(IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    binder.registerHandler(command.addRuleTriggeredHandler(i));
    binder.registerHandler(command.addRuleUntriggeredHandler(i));
    return this;
  }
}
