package org.tessell.model.validation;

import java.util.ArrayList;

import org.tessell.model.properties.Property;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.widgets.TextList;

/**
 * For keeping track of custom, controlled-based errors that later need to be cleared
 * on the next AJAX request to the server.
 *
 * When a message is added, a {@link RuleTriggeredEvent} event is fired against
 * the property, and so any {@link TextList} components listening for errors will
 * pick it up and show the error.
 * 
 * When {@link #clear()} is called, a {@link RuleUntriggeredEvent} event is fired
 * to remove the message while the server is re-consulted about the error state.
 */
public class ServerErrors {

  private final ArrayList<Message> messages = new ArrayList<Message>();

  public void add(final Property<?> property, final String message) {
    final Message m = new Message(property, message);
    m.fire();
    messages.add(m);
  }

  // If there some sort of of "ServerCall" object, this clear could be called automatically 

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
