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

  private final Binder b;
  private final UiCommand command;

  public UiCommandBinder(final Binder b, final UiCommand command) {
    this.b = b;
    this.command = command;
  }

  /** Binds clicks from {@code clickable} to our command, and our errors to {@code errors}. */
  public void to(final HasClickHandlers clickable, final IsTextList errors) {
    to(clickable);
    errorsTo(errors);
  }

  /** Binds "enter" from key down handlers to our command. */
  public void toEnterKey(final HasAllKeyHandlers... allKeys) {
    OnEnterKeyHandler h = new OnEnterKeyHandler(new Runnable() {
      public void run() {
        command.execute();
      }
    });
    for (HasAllKeyHandlers allKey : allKeys) {
      b.add(allKey.addKeyDownHandler(h));
    }
  }

  /** Has our command execute only if {@code onlyIf} is true. */
  public UiCommandBinder onlyIf(final Property<Boolean> onlyIf) {
    command.addOnlyIf(onlyIf);
    return this;
  }

  /** Binds clicks from {@code clickable} to our command. */
  public void to(final HasClickHandlers clickable) {
    b.add(clickable.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        command.execute();
        event.preventDefault();
      }
    }));
  }

  /** Binds errors for our command to {@code errors}. */
  public void errorsTo(IsTextList errors) {
    final TextListOnError i = new TextListOnError(errors);
    b.add(command.addRuleTriggeredHandler(i));
    b.add(command.addRuleUntriggeredHandler(i));
  }
}
