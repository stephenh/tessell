package org.tessell.model.dsl;

import java.util.Map;

import org.tessell.model.properties.Property;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.widgets.IsTextList;

/** Listens for rule events for a field and adds the messages to a {@link IsTextList}. */
public class TextListOnError implements RuleTriggeredHandler, RuleUntriggeredHandler {

  private final IsTextList list;

  public TextListOnError(final IsTextList list) {
    this.list = list;
  }

  public void addExisting(Property<?> p) {
    for (Map.Entry<Object, String> e : p.getErrors().entrySet()) {
      list.add(e.getValue()); // need to check displayed, but only exists on the event
    }
  }

  public void onTrigger(final RuleTriggeredEvent event) {
    if (!event.getDisplayed()[0] && list.isEnabled()) {
      list.add(event.getMessage());
      event.getDisplayed()[0] = true;
    }
  }

  public void onUntrigger(final RuleUntriggeredEvent event) {
    list.remove(event.getMessage());
  }

}
