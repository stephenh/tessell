package org.gwtmpv.model.validation;

import java.util.ArrayList;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;

public class ServerErrors {

  private final ArrayList<Message> messages = new ArrayList<Message>();

  public void add(final Property<?> property, final String message) {
    final Message m = new Message(property, message);
    m.fire();
    messages.add(m);
  }

  public void clear() {
    for (final Message m : messages) {
      m.unfire();
    }
  }

  private static class Message {
    private final Property<?> property;
    private final String message;

    private Message(final Property<?> property, final String message) {
      this.property = property;
      this.message = message;
    }

    private void fire() {
      property.fireEvent(new RuleTriggeredEvent(this, message, new Boolean[] { false }));
    }

    private void unfire() {
      // Ack. Not good because the rule will still think it is fired. Untouched is better.
      property.fireEvent(new RuleUntriggeredEvent(this, message));
    }
  }

}
