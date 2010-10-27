package org.gwtmpv.model.properties;

import java.util.ArrayList;

import org.gwtmpv.model.validation.events.RuleTriggeredEvent;
import org.gwtmpv.model.validation.events.RuleTriggeredHandler;
import org.gwtmpv.model.validation.events.RuleUntriggeredEvent;
import org.gwtmpv.model.validation.events.RuleUntriggeredHandler;
import org.gwtmpv.model.validation.rules.Custom;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.SetValue;

/** Groups a set of {@link Property}s together. This is pretty hacky. It might go away. */
public class PropertyGroup extends AbstractProperty<Boolean, PropertyGroup> {

  // All of the rules in this group
  private final ArrayList<Property<?>> properties = new ArrayList<Property<?>>();
  private final ArrayList<Object> invalid = new ArrayList<Object>();

  public PropertyGroup(final String name, final String message) {
    super(new SetValue<Boolean>(name));
    new Custom(this, message, new DerivedValue<Boolean>() {
      public Boolean get() {
        return invalid.size() == 0;
      }
    });
  }

  /** Adds properties to the group to validate as a group. */
  public void add(final Property<?>... properties) {
    for (final Property<?> property : properties) {
      this.properties.add(property);
      property.addRuleTriggeredHandler(triggered);
      property.addRuleUntriggeredHandler(untriggered);
    }
  }

  @Override
  public void set(final Boolean value) {
    throw new IllegalArgumentException("PropertyGroups cannot be set, call validate");
  }

  public ArrayList<Property<?>> getProperties() {
    return properties;
  }

  @Override
  public void setTouched(final boolean touched) {
    for (final Property<?> other : properties) {
      other.setTouched(touched);
    }
    super.setTouched(touched);
  }

  @Override
  protected PropertyGroup getThis() {
    return this;
  }

  private final RuleTriggeredHandler triggered = new RuleTriggeredHandler() {
    public void onTrigger(final RuleTriggeredEvent event) {
      invalid.add(event.getKey());
      reassess();
    }
  };

  private final RuleUntriggeredHandler untriggered = new RuleUntriggeredHandler() {
    public void onUntrigger(final RuleUntriggeredEvent event) {
      invalid.remove(event.getKey());
      reassess();
    }
  };

}
