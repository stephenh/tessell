package org.tessell.model.dsl;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;

import java.util.Arrays;

import org.tessell.model.commands.UiCommand;
import org.tessell.model.properties.*;
import org.tessell.model.validation.rules.Rule;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.HasValue;

/**
 * Provides a fluent interface for binding properties to widgets.
 * 
 * Very heavily influenced by gwt-pectin.
 */
public class Binder {

  private Binder() {
  }

  /** @return a fluent {@link PropertyBinder} against {@code property}. */
  public static <P> PropertyBinder<P> bind(Property<P> property) {
    return new PropertyBinder<P>(property);
  }

  /** @return a fluent {@link ListPropertyBinder} against {@code property}. */
  public static <P> ListPropertyBinder<P> bind(ListProperty<P> property) {
    return new ListPropertyBinder<P>(property);
  }

  /** @return a fluent {@link RuleBinder} against {@code rule}. */
  public static RuleBinder bind(Rule rule) {
    return new RuleBinder(rule);
  }

  /** @return a fluent {@link StringPropertyBinder} against {@code property}. */
  public static <P> StringPropertyBinder bind(StringProperty property) {
    return new StringPropertyBinder(property);
  }

  /** @return a fluent {@link BooleanPropertyBinder} against {@code property}. */
  public static <P> BooleanPropertyBinder bind(BooleanProperty property) {
    return new BooleanPropertyBinder(property);
  }

  /** @return a fluent {@link EnumPropertyBinder} against {@code property}. */
  public static <E extends Enum<E>> EnumPropertyBinder<E> bind(EnumProperty<E> property) {
    return new EnumPropertyBinder<E>(property);
  }

  /** @return a fluent {@link UiCommandBinder} against {@code command}. */
  public static UiCommandBinder bind(UiCommand command) {
    return new UiCommandBinder(command);
  }

  /** @return a fluent {@link WhenBinder} against {@code property}. */
  public static <P> WhenBinder<P> when(Property<P> property) {
    return new WhenBinder<P>(property);
  }

  /** @return a fluent {@link EventBinder} against {@code clickable}. */
  public static EventBinder onClick(HasClickHandlers clickable) {
    return new ClickBinder(clickable);
  }

  /** @return a fluent {@link EventBinder} against {@code clickable}. */
  public static EventBinder onDoubleClick(HasDoubleClickHandlers clickable) {
    return new DoubleClickBinder(clickable);
  }

  /** @return a fluent {@link EventBinder} against {@code blurable}. */
  public static EventBinder onBlur(HasBlurHandlers blurable) {
    return new BlurBinder(blurable);
  }

  /** @return a fluent {@link EventBinder} against {@code changable}. */
  @SuppressWarnings("unchecked")
  public static EventBinder onChange(HasValueChangeHandlers<?> changable) {
    return new ChangeBinder((HasValueChangeHandlers<Object>) changable);
  }

  /** @return a fluent {@link EventBinder} against {@code keyDownable}. */
  public static EventBinder onKeyDown(HasKeyDownHandlers keyDownable) {
    return new KeyDownBinder(keyDownable, null);
  }

  /** @return a fluent {@link EventBinder} against {@code keyDownable}, when {@code char} is pressed. */
  public static EventBinder onKeyDown(HasKeyDownHandlers keyDownable, Integer... filter) {
    return new KeyDownBinder(keyDownable, Arrays.asList(filter));
  }

  /** Enhances each {@code source} to fire change events on key up and blur. */
  public static <P, S extends HasKeyUpHandlers & HasBlurHandlers & HasValue<P>> void enhance(S... sources) {
    fireChangeOnBlur(sources);
    fireChangeOnKeyUp(sources);
  }

  /** Forces a {@link ValueChangeEvent} (and hence touching) of each {@code source} on blur. */
  public static <P, S extends HasBlurHandlers & HasValue<P>> HandlerRegistrations fireChangeOnBlur(S... sources) {
    HandlerRegistrations hrs = new HandlerRegistrations();
    for (final S source : sources) {
      hrs.add(source.addBlurHandler(new BlurHandler() {
        public void onBlur(final BlurEvent event) {
          ValueChangeEvent.fire(source, source.getValue());
        }
      }));
    }
    return hrs;
  }

  /** Forces a {@link ValueChangeEvent} (and hence touching) of each {@code source} on key up. */
  public static <P, S extends HasKeyUpHandlers & HasValue<P>> HandlerRegistrations fireChangeOnKeyUp(S... sources) {
    HandlerRegistrations hrs = new HandlerRegistrations();
    for (final S source : sources) {
      hrs.add(source.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(final KeyUpEvent event) {
          if (event.getNativeKeyCode() == KEY_TAB) {
            return; // ignore tabbing into the field
          }
          ValueChangeEvent.fire(source, source.getValue());
        }
      }));
    }
    return hrs;
  }

  /*
   * Properties might need two kinds of values--current and changing. E.g. property.changing()
   * is the same value, except that it includes results from key up events. Then derivative
   * properties can be made off of either the current or changing property.
   * 
   * Think of the login email box case:
   *
   * 1. User starts typing "foo@" -- do not do validation.
   * 1a. The 'remaining' box is counting down from 100, 99, 98... chars left
   * 2. User finishes typing "foo@bar.com!" -- stays in the box -- (auto-fire changed after X seconds? fancy, probably not)
   * 3. User tabs out of the email box, blur/change fires, do validation
   * 4. User fixes email by removing "!", redo validation as soon as key up is fired
   *
   * Or is 'remaining' just a view artifact, that is driven directly by key up,
   * textBox.getValue and not by the backing model property?
   * 
   * Maybe that makes more sense--the model is only updated when the user
   * is no longer in input mode. While in input mode, the view can display
   * various view-specific content, like length of input remaining. But
   * there is no reason for the in-progress state to leak back into the model.
   * 
   * ...but what about in error-mode correction? After a field/model have
   * been touched, and determined invalid, don't we want to let the user
   * know they've remedied the error ASAP, e.g. after key up? After a pause?
   */

  /** Forces a {@link ValueChangeEvent} of each {@code source} on key up, after it's been touched. */
  public static <P, S extends HasKeyUpHandlers & HasBlurHandlers & HasValue<P>> HandlerRegistrations fireChangeOnBlurThenKeyUp(S... sources) {
    HandlerRegistrations hr = new HandlerRegistrations();
    for (final S source : sources) {
      BlurThenKeyUp<P> h = new BlurThenKeyUp<P>(source);
      hr.add(source.addKeyUpHandler(h));
      hr.add(source.addBlurHandler(h));
    }
    return hr;
  }

  /** Fires {@link ValueChangeEvent} on blur (always) and key up (after a blur has been fired). */
  private static class BlurThenKeyUp<P> implements KeyUpHandler, BlurHandler {
    private final HasValue<P> source;
    private boolean hasBlurred = false;

    /*
     * Instead of only firing key-up-change if post-hasBlurred, it should be
     * only firing key-up-change is the property is currenty invalid. Then
     * a currently valid property would not eagerly invalidated as the user is
     * changing it from 1 valid value to another valid value. It's only when
     * properties are invalid that we want to fix things as soon as possible.
     * 
     * For validation anyway...for characters left, that seems better suited to
     * a separate "property.changing" derived property.
     */

    private BlurThenKeyUp(HasValue<P> source) {
      this.source = source;
    }

    @Override
    public void onBlur(BlurEvent event) {
      hasBlurred = true;
      ValueChangeEvent.fire(source, source.getValue());
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
      if (!hasBlurred) {
        return;
      }
      if (event.getNativeKeyCode() == KEY_TAB) {
        return; // ignore tabbing into the field
      }
      ValueChangeEvent.fire(source, source.getValue());
    }
  }

}
