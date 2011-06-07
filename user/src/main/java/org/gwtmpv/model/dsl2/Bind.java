package org.gwtmpv.model.dsl2;

import org.gwtmpv.model.dsl.HandlerRegistrations;
import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.util.ObjectUtils;
import org.gwtmpv.widgets.HasCss;
import org.gwtmpv.widgets.IsWidget;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class Bind {

  public static <P> HandlerRegistrations bind(Property<P> p, BindTarget<P> target) {
    return target.bindTo(p);
  }

  // when(property, is(foo), show(blah));

  public static <P> HandlerRegistrations when(final Property<P> p, final WhenBoolCondition<P> condition, final WhenBoolAction<P> action) {
    return new HandlerRegistrations(p.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      @Override
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        action.update(condition.evaluate(p));
      }
    }));
  }

  public interface WhenBoolCondition<P> {
    boolean evaluate(Property<P> p);
  }

  public interface WhenBoolAction<P> {
    void update(boolean value);
  }

  public interface BindTarget<P> {
    HandlerRegistrations bindTo(Property<P> source);
  }

  public static <P> WhenBoolCondition<P> is(final P value) {
    return new WhenBoolCondition<P>() {
      @Override
      public boolean evaluate(Property<P> p) {
        return ObjectUtils.eq(p.get(), value);
      }
    };
  }

  public static <P> WhenBoolAction<P> show(final IsWidget w) {
    return new WhenBoolAction<P>() {
      public void update(boolean value) {
        if (value) {
          w.getStyle().clearDisplay();
        } else {
          w.getStyle().setDisplay(Display.NONE);
        }
      }
    };
  }

  public static <P> WhenBoolAction<P> addStyle(final HasCss hc, final String styleName) {
    return new WhenBoolAction<P>() {
      public void update(boolean value) {
        if (value) {
          hc.addStyleName(styleName);
        } else {
          hc.removeStyleName(styleName);
        }
      }
    };
  }

  public static <P> BindTarget<P> to(final HasValue<P> source) {
    return new BindTarget<P>() {
      public HandlerRegistrations bindTo(final Property<P> p) {
        PropertyChangedHandler<P> h = new PropertyChangedHandler<P>() {
          public void onPropertyChanged(final PropertyChangedEvent<P> event) {
            source.setValue(event.getProperty().get(), true);
          }
        };
        // set initial value
        h.onPropertyChanged(new PropertyChangedEvent<P>(p));
        // after we've set the initial value (which fired ValueChangeEvent and
        // would have messed up our 'touched' state), listen for others changes
        HandlerRegistration a = null;
        if (!p.isReadOnly()) {
          a = source.addValueChangeHandler(new ValueChangeHandler<P>() {
            public void onValueChange(ValueChangeEvent<P> event) {
              p.set("".equals(source.getValue()) ? null : source.getValue());
            }
          });
        }
        HandlerRegistration b = p.addPropertyChangedHandler(h);
        return new HandlerRegistrations(a, b);
      }
    };
  }

}
