package org.tessell.model.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;
import org.tessell.model.validation.events.RuleUntriggeredEvent;
import org.tessell.model.validation.events.RuleUntriggeredHandler;
import org.tessell.model.validation.rules.Custom;
import org.tessell.model.values.SetValue;

import com.google.gwt.event.shared.HandlerRegistration;

/** Groups a set of {@link Property}s together. */
public class PropertyGroup extends AbstractProperty<Boolean, PropertyGroup> {

  // All of the properties in this group
  private final ArrayList<PropertyWithHandlers> properties = new ArrayList<PropertyWithHandlers>();
  // Any outstanding errors from properties in this group
  private final ArrayList<PropertyError> invalid = new ArrayList<PropertyError>();
  private Snapshot snapshot;

  public PropertyGroup(final String name, final String message) {
    super(new SetValue<Boolean>(name, true));
    // add a rule that fires whenever we're false (and touched)
    addRule(new Custom(message, this));
    // We always want to consider ourselves "touched", so that our
    // error message fires right away, and the UI/parent property
    // group can see it. However, we don't want to touch our children
    // right away, we only do that when the client calls setTouched.
    super.setTouched(true);
  }

  @Override
  public void reassess() {
    ((SetValue<Boolean>) getValueObject()).set(invalid.size() == 0);
    super.reassess();
  }

  /** Adds properties to the group to validate as a group. */
  public void add(final Property<?>... properties) {
    for (final Property<?> property : properties) {
      this.properties.add(new PropertyWithHandlers(property));
      for (Map.Entry<Object, String> e : property.getErrors().entrySet()) {
        invalid.add(new PropertyError(property, e.getKey()));
      }
    }
    reassess();
  }

  /** Removes {@code property} from the group. */
  public void remove(Property<?> property) {
    for (Iterator<PropertyWithHandlers> i = properties.iterator(); i.hasNext();) {
      PropertyWithHandlers pwd = i.next();
      if (pwd.property == property) {
        pwd.removeHandlers();
        i.remove();
      }
    }
    for (Iterator<PropertyError> i = invalid.iterator(); i.hasNext();) {
      if (i.next().property == property) {
        i.remove();
      }
    }
    reassess();
  }

  public ArrayList<Property<?>> getProperties() {
    ArrayList<Property<?>> properties = new ArrayList<Property<?>>();
    for (PropertyWithHandlers pwh : this.properties) {
      properties.add(pwh.property);
    }
    return properties;
  }

  @Override
  public void setTouched(final boolean touched) {
    for (final PropertyWithHandlers other : properties) {
      other.property.setTouched(touched);
    }
    // Per comment in the constructor, we don't actually want
    // to toggle our touched state, it should always be true.
    // So don't call super, but copy/paste some of it's logic
    // here to be at least somewhat consistent.
    // super.setTouched(touched);
    for (final Downstream other : downstream) {
      if (other.touch) {
        other.property.setTouched(touched);
      }
    }
    reassess();
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

  public void reassessAll() {
    for (Property<?> p : getProperties()) {
      p.reassess();
    }
  }

  public void setTouchedOnAll(boolean touched) {
    for (Property<?> p : getProperties()) {
      p.setTouched(touched);
    }
  }

  @Override
  protected PropertyGroup getThis() {
    return this;
  }

  /** Remembers the state of all of the properties in our group so that we can roll back when needed (e.g. on cancel). */
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

  /** Holds a property + its handler registrations (in case we have to remove it). */
  private class PropertyWithHandlers {
    private final Property<?> property;
    private final HandlerRegistration triggered;
    private final HandlerRegistration untriggered;

    private PropertyWithHandlers(final Property<?> property) {
      this.property = property;
      triggered = property.addRuleTriggeredHandler(new RuleTriggeredHandler() {
        public void onTrigger(final RuleTriggeredEvent event) {
          invalid.add(new PropertyError(property, event.getKey()));
          reassess();
        }
      });
      untriggered = property.addRuleUntriggeredHandler(new RuleUntriggeredHandler() {
        public void onUntrigger(final RuleUntriggeredEvent event) {
          for (Iterator<PropertyError> i = invalid.iterator(); i.hasNext();) {
            if (i.next().key.equals(event.getKey())) {
              i.remove();
            }
          }
          reassess();
        }
      });
    }

    private void removeHandlers() {
      triggered.removeHandler();
      untriggered.removeHandler();
    }

    @Override
    public String toString() {
      return property.toString();
    }
  }

  /** Holds a rule triggered key + the property that caused it (in case we have to remove it). */
  private class PropertyError {
    private final Property<?> property;
    private final Object key;

    private PropertyError(Property<?> property, Object key) {
      this.property = property;
      this.key = key;
    }
  }

}
