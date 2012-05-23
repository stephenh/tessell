package org.tessell.model.dsl;

import org.tessell.model.commands.UiCommand;
import org.tessell.model.properties.Property;
import org.tessell.util.OnEnterKeyHandler;
import org.tessell.widgets.IsTextList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;

/** Binds various things to a command. */
public class UiCommandBinder {

  private final UiCommand command;

  public UiCommandBinder(final UiCommand command) {
    this.command = command;
  }

  /** Binds clicks from {@code clickable} to our command, and our errors to {@code errors}. */
  public HandlerRegistrations to(final HasClickHandlers clickable, final IsTextList errors) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(to(clickable));
    hr.add(errorsTo(errors));
    return hr;
  }

  /** Binds "enter" from key down handlers to our command. */
  public HandlerRegistrations toEnterKey(final HasAllKeyHandlers... allKeys) {
    HandlerRegistrations hr = new HandlerRegistrations();
    for (HasAllKeyHandlers allKey : allKeys) {
      OnEnterKeyHandler h = new OnEnterKeyHandler(new Runnable() {
        public void run() {
          command.execute();
        }
      });
      hr.add(allKey.addKeyDownHandler(h));
      hr.add(allKey.addKeyDownHandler(h));
    }
    return hr;
  }

  /** Has our command execute only if {@code onlyIf} is true. */
  public UiCommandBinder onlyIf(final Property<Boolean> onlyIf) {
    command.addOnlyIf(onlyIf);
    return this;
  }

  /** Binds clicks from {@code clickable} to our command. */
  public HandlerRegistrations to(final HasClickHandlers clickable) {
    return new HandlerRegistrations(clickable.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        command.execute();
        event.preventDefault();
      }
    }));
  }

  /** Binds errors for our command to {@code errors}. */
  public HandlerRegistrations errorsTo(IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(command.addRuleTriggeredHandler(i));
    hr.add(command.addRuleUntriggeredHandler(i));
    return hr;
  }
}
