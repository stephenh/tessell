package org.gwtmpv.model.dsl;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.util.ObjectUtils;
import org.gwtmpv.widgets.HasCss;

import com.google.gwt.dom.client.Style.Display;

public class When<P> {

  public static <P> When<P> when(Property<P> property, WhenCondition<P> condition) {
    return new When<P>(property, condition);
  }

  public static <P> WhenCondition<P> is(final P value) {
    return new WhenCondition<P>() {
      public boolean evaluate(Property<P> p) {
        return ObjectUtils.eq(value, p.get());
      }
    };
  }

  public static <P> WhenCondition<P> is(final P... values) {
    return new WhenCondition<P>() {
      public boolean evaluate(Property<P> p) {
        for (P value : values) {
          if (ObjectUtils.eq(value, p.get())) {
            return true;
          }
        }
        return false;
      }
    };
  }

  public static <P> WhenCondition<P> or(final WhenCondition<P> condition1, final WhenCondition<P> condition2) {
    return new WhenCondition<P>() {
      public boolean evaluate(Property<P> p) {
        return condition1.evaluate(p) || condition2.evaluate(p);
      }
    };
  }

  public static WhenResult show(final HasCss... csses) {
    return new WhenResult() {
      public void update(boolean result) {
        for (HasCss css : csses) {
          if (result) {
            css.getStyle().clearDisplay();
          } else {
            css.getStyle().setDisplay(Display.NONE);
          }
        }
      }
    };
  }

  public final Property<P> property;
  public final WhenCondition<P> condition;

  public When(Property<P> property, WhenCondition<P> condition) {
    this.property = property;
    this.condition = condition;
  }

}
