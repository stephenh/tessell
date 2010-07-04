package org.gwtmpv.tests.model.validation.rules;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import joist.util.Join;

import org.gwtmpv.model.properties.HasRuleTriggers;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredEvent.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent.RuleUntriggeredHandler;

public abstract class AbstractRuleTest {

  protected final Map<Object, String> messages = new LinkedHashMap<Object, String>();

  public void listenTo(final HasRuleTriggers... hasTriggersList) {
    for (final HasRuleTriggers hasTriggers : hasTriggersList) {
      hasTriggers.addRuleTriggeredHandler(new RuleTriggeredHandler() {
        public void onTrigger(final RuleTriggeredEvent event) {
          if (event.getMessage() != null) {
            messages.put(event.getKey(), event.getMessage());
          }
        }
      });
      hasTriggers.addRuleUntriggeredHandler(new RuleUntriggeredHandler() {
        public void onUntrigger(final RuleUntriggeredEvent event) {
          messages.remove(event.getKey());
        }
      });
    }
  }

  protected void assertMessages(final String... messages) {
    assertEquals(Join.lines(messages), Join.lines(this.messages.values()));
  }

}