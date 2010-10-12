package org.gwtmpv.model.dsl;

import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;
import org.gwtmpv.widgets.IsTextList;

/** Listens for rule events for a field and adds the messages to a {@link IsTextList}. */
public class TextListOnError implements RuleTriggeredHandler, RuleUntriggeredHandler {

  private final IsTextList list;

  public TextListOnError(final IsTextList list) {
    this.list = list;
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
