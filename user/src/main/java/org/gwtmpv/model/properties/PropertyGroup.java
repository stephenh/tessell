package org.gwtmpv.model.properties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
  private Snapshot snapshot;

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

  public void capture() {
    Snapshot s = new Snapshot();
    for (Property<?> p : getProperties()) {
      s.save(p);
    }
    snapshot = s;
  }

  public void restore() {
    for (Property<?> p : getProperties()) {
      snapshot.restore(p);
    }
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

  private static class Snapshot {
    private final Map<Property<?>, Object> state = new LinkedHashMap<Property<?>, Object>();

    private <P> void save(Property<P> p) {
      state.put(p, p.get());
    }

    @SuppressWarnings("unchecked")
    private <P> void restore(Property<P> p) {
      p.set((P) state.get(p));
    }
  }

}
