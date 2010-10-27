package org.gwtmpv.model.properties;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.validation.rules.Static;
import org.gwtmpv.model.values.DelegatedValue;

/**
 * Converts a source property to/from another type, using a {@link PropertyFormatter}. 
 *
 * @param <SP> the source property type
 * @param <DP> the destination property type
 */
public class FormattedProperty<DP, SP> extends BasicProperty<DP> implements DelegatedValue.Delegate<DP> {

  private final Property<SP> source;
  private final PropertyFormatter<SP, DP> formatter;
  private final Static isValid;

  public FormattedProperty(final Property<SP> source, final PropertyFormatter<SP, DP> formatter) {
    this(source, formatter, null);
  }

  public FormattedProperty(final Property<SP> source, final PropertyFormatter<SP, DP> formatter, final String message) {
    super(new DelegatedValue<DP>());
    this.source = source;
    this.formatter = formatter;
    // note, we currently fire the error against our source property, so that people listening for errors
    // to it will see them. it might make more sense to fire against us first, then our source property.
    isValid = new Static(source, (message == null) ? source.getName() + " is invalid" : message);
    ((DelegatedValue<DP>) getValue()).setDelegate(this);
    source.addPropertyChangedHandler(new PropertyChangedHandler<SP>() {
      public void onPropertyChanged(PropertyChangedEvent<SP> event) {
        isValid.set(true); // any real (non-formatted) value being set makes our old attempt worthless
      }
    });
  }

  @Override
  public DP valueGet() {
    SP value = source.get();
    return (value == null) ? null : formatter.format(value);
  }

  @Override
  public void valueSet(DP value) {
    // null and "" are special
    if (value == null || "".equals(value)) {
      isValid.set(true);
      source.set(null);
      return;
    }
    final SP parsed;
    try {
      parsed = formatter.parse(value);
    } catch (Exception e) {
      isValid.set(false);
      // we failed to parse the value, but still treat this as touching the source property
      source.setTouched(true);
      return;
    }
    isValid.set(true);
    source.set(parsed);
  }

  public String valueName() {
    return "formatted " + source.getName();
  }

}
