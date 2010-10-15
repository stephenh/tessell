package org.gwtmpv.model.dsl;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.HasCss;

import com.google.gwt.dom.client.Style.Display;

/** Does various things as the boolean property changes from true/false. */
public class BooleanBinder {

  private final Binder binder;
  private final Property<Boolean> property;

  public BooleanBinder(final Binder binder, final Property<Boolean> property) {
    this.binder = binder;
    this.property = property;
  }

  public BooleanBinder show(final HasCss css) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        showIfTrue(css);
      }
    }));
    showIfTrue(css); // set initial
    return this;
  }

  /** @return a binder to set {@code style} on {@link HasCss} */
  public BooleanSetBinder set(String style) {
    return new BooleanSetBinder(binder, property, style);
  }

  /** @return a binder to remove {@code} value} from a list. */
  public <V> BooleanRemoveBinder<V> remove(V value) {
    return new BooleanRemoveBinder<V>(binder, property, value);
  }

  private void showIfTrue(HasCss css) {
    if (Boolean.TRUE.equals(property.get())) {
      css.getStyle().clearDisplay();
    } else {
      css.getStyle().setDisplay(Display.NONE);
    }
  }

}
