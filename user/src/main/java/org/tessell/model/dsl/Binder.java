package org.tessell.model.dsl;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;

import java.util.Arrays;

import org.tessell.bus.AbstractBound;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.commands.UiCommand;
import org.tessell.model.events.HasMemberChangedHandlers;
import org.tessell.model.properties.*;
import org.tessell.model.validation.rules.Rule;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/**
 * Provides a fluent interface for binding properties to widgets.
 * 
 * Very heavily influenced by gwt-pectin.
 */
public class Binder extends AbstractBound {

  /** @return a fluent {@link PropertyBinder} against {@code property}. */
  public <P> PropertyBinder<P> bind(Property<P> property) {
    return new PropertyBinder<P>(this, property);
  }

  /** @return a fluent {@link ListPropertyBinder} against {@code property}. */
  public <P> ListPropertyBinder<P> bind(ListProperty<P> property) {
    return new ListPropertyBinder<P>(this, property);
  }

  /** @return a fluent {@link RuleBinder} against {@code rule}. */
  public <P> RuleBinder<P> bind(Rule<P> rule) {
    return new RuleBinder<P>(this, rule);
  }

  /** @return a fluent {@link StringPropertyBinder} against {@code property}. */
  public <P> StringPropertyBinder bind(StringProperty property) {
    return new StringPropertyBinder(this, property);
  }

  /** @return a fluent {@link BooleanPropertyBinder} against {@code property}. */
  public <P> BooleanPropertyBinder bind(BooleanProperty property) {
    return new BooleanPropertyBinder(this, property);
  }

  /** @return a fluent {@link EnumPropertyBinder} against {@code property}. */
  public <E extends Enum<E>> EnumPropertyBinder<E> bind(EnumProperty<E> property) {
    return new EnumPropertyBinder<E>(this, property);
  }

  /** @return a fluent {@link UiCommandBinder} against {@code command}. */
  public UiCommandBinder bind(UiCommand command) {
    return new UiCommandBinder(this, command);
  }

  /** @return a fluent {@link WhenBinder} against {@code property}. */
  public <P> WhenBinder<P> when(Property<P> property) {
    return new WhenBinder<P>(this, property);
  }

  /** @return a fluent {@link EventBinder} against {@code clickable}. */
  public EventBinder onClick(IsWidget clickable) {
    return new ClickBinder(this, clickable);
  }

  /** @return a fluent {@link EventBinder} against {@code clickable}. */
  public EventBinder onDoubleClick(HasDoubleClickHandlers clickable) {
    return new DoubleClickBinder(this, clickable);
  }

  /** @return a fluent {@link EventBinder} against {@code blurable}. */
  public EventBinder onBlur(HasBlurHandlers blurable) {
    return new BlurBinder(this, blurable);
  }

  /** @return a fluent {@link EventBinder} against {@code attachable}. */
  public EventBinder onAttach(HasAttachHandlers attachable) {
    return new AttachBinder(this, attachable, true);
  }

  /** @return a fluent {@link EventBinder} against {@code attachable}. */
  public EventBinder onDetach(HasAttachHandlers attachable) {
    return new AttachBinder(this, attachable, false);
  }

  /** @return a fluent {@link EventBinder} against {@code changable}. */
  @SuppressWarnings("unchecked")
  public EventBinder onChange(HasValueChangeHandlers<?> changable) {
    return new ChangeBinder(this, (HasValueChangeHandlers<Object>) changable);
  }

  /** @return a fluent {@link EventBinder} against {@code property}. */
  @SuppressWarnings("unchecked")
  public EventBinder onChange(Property<?> property) {
    return new PropertyChangeBinder(this, (Property<Object>) property);
  }

  /** @return a fluent {@link EventBinder} against {@code hasMembers}. */
  public EventBinder onMemberChange(HasMemberChangedHandlers hasMembers) {
    return new MemberChangeBinder(this, hasMembers);
  }

  /** @return a fluent {@link EventBinder} against {@code keyDownable}. */
  public EventBinder onKeyDown(HasKeyDownHandlers keyDownable) {
    return new KeyDownBinder(this, keyDownable, null);
  }

  /** @return a fluent {@link EventBinder} against {@code keyDownable}, when {@code char} is pressed. */
  public EventBinder onKeyDown(HasKeyDownHandlers keyDownable, Integer... filter) {
    return new KeyDownBinder(this, keyDownable, Arrays.asList(filter));
  }

  /** Enhances each {@code source} to fire change events on key up and blur. */
  public <P, S extends HasKeyUpHandlers & HasBlurHandlers & HasValue<P>> void enhance(S... sources) {
    fireChangeOnBlur(sources);
    fireChangeOnKeyUp(sources);
  }

  /** Forces a {@link ValueChangeEvent} (and hence touching) of each {@code source} on blur. */
  public <P, S extends HasBlurHandlers & HasValue<P>> void fireChangeOnBlur(S... sources) {
    for (final S source : sources) {
      add(source.addBlurHandler(new BlurHandler() {
        public void onBlur(final BlurEvent event) {
          ValueChangeEvent.fire(source, source.getValue());
        }
      }));
    }
  }

  /** Forces a {@link ValueChangeEvent} (and hence touching) of each {@code source} on key up. */
  public <P, S extends HasKeyUpHandlers & HasValue<P>> void fireChangeOnKeyUp(S... sources) {
    for (final S source : sources) {
      add(source.addKeyUpHandler(new KeyUpHandler() {
        public void onKeyUp(final KeyUpEvent event) {
          if (event.getNativeKeyCode() == KEY_TAB) {
            return; // ignore tabbing into the field
          }
          ValueChangeEvent.fire(source, source.getValue());
        }
      }));
    }
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
  public <P, S extends HasKeyUpHandlers & HasBlurHandlers & HasValue<P>> void fireChangeOnBlurThenKeyUp(S... sources) {
    for (final S source : sources) {
      BlurThenKeyUp<P> h = new BlurThenKeyUp<P>(source);
      add(source.addKeyUpHandler(h));
      add(source.addBlurHandler(h));
    }
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

  protected void add(HandlerRegistration registration) {
    super.registerHandler(registration);
  }

  boolean canSetInitialValue(Property<?> property) {
    return !property.isReadOnly() && !property.isTouched() && property.get() == null;
  }
}
