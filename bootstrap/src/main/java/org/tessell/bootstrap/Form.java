package org.tessell.bootstrap;

import static java.lang.Boolean.FALSE;
import static org.tessell.bootstrap.views.AppViews.newFormView;
import static org.tessell.model.properties.NewProperty.booleanProperty;

import java.util.ArrayList;
import java.util.List;

import org.tessell.bootstrap.views.IsFormView;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.Model;
import org.tessell.model.commands.HasActive;
import org.tessell.model.commands.UiCommand;
import org.tessell.model.dsl.Binder;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.widgets.CompositeHasIsWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;

/** A common abstraction for bizstrap-based forms. */
public class Form extends CompositeHasIsWidgets {

  private final IsFormView view = newFormView();
  private final BooleanProperty loading = booleanProperty("loading", false);
  private final BooleanProperty active = booleanProperty("active", false);
  private final PropertyGroup valid = new PropertyGroup("valid", "Form is invalid");
  private final Binder b = new Binder();
  private final List<AbstractLine> lines = new ArrayList<AbstractLine>();
  private final List<Button> buttons = new ArrayList<Button>();
  private boolean autoFocus = true;

  public Form() {
    setWidget(view);
    b.when(loading).is(true).hide(view.lines(), view.actions());
    setupFocusHandlers();
  }

  public void setTitle(final String title) {
    view.title().setInnerText(title);
  }

  public void setAutoFocus(final boolean autoFocus) {
    this.autoFocus = autoFocus;
  }

  /** Adds a new cancel/save/etc. button. */
  public void add(final Button button) {
    button.setForm(this);
    buttons.add(button);
    if ("left".equals(button.getSide())) {
      view.leftActions().add(button);
    } else {
      view.rightActions().add(button);
    }
  }

  /** Adds a new form line. */
  public void add(final AbstractLine line) {
    line.setForm(this);
    valid.add(line.valid());
    lines.add(line);
    view.lines().add(line);
    if (line instanceof CanTriggerDefaultAction) {
      ((CanTriggerDefaultAction) line).addDefaultActionHandler(new KeyDownHandler() {
        public void onKeyDown(final KeyDownEvent event) {
          if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            // if the user just typed their password, it won't be in our model until
            // on change fires, so we need to defer the default action until the next
            // event loop.
            deferIfInGwt(new ScheduledCommand() {
              public void execute() {
                triggerDefaultAction();
              }
            });
          }
        }
      });
    }
  }

  /** Used by XxxLines for the form to watch a property (for validity, touch-on-action). */
  public void watch(final Property<?> property) {
    valid.add(property);
  }

  /** Used by XxxLines for the form to watch a model (for validity, touch-on-action). */
  public void watch(final Model model) {
    valid.add(model.allValid());
  }

  /** Used by XxxLines for the form to watch command (for on-submit actions). */
  public void watch(final UiCommand command) {
    command.addOnlyIf(valid);
    if (command instanceof HasActive) {
      b.bind(((HasActive) command).active()).to(active);
    }
  }

  public Property<Boolean> valid() {
    return valid;
  }

  public BooleanProperty loading() {
    return loading;
  }

  public Property<Boolean> active() {
    return active;
  }

  @VisibleForTesting
  public IsFormView getView() {
    return view;
  }

  @Override
  protected void addIsWidget(final IsWidget w) {
    throw new UnsupportedOperationException("Not used, as we have explicit add methods for each type");
  }

  /** Triggers a click in one of the right-side buttons, only if a primary button is available. */
  private void triggerDefaultAction() {
    for (final Button button : buttons) {
      if (button.isPrimary()) {
        button.getButton().click();
        break;
      }
    }
  }

  /**
   * Sets up auto-focus for our first form element.
   * 
   * Focus is really annoying because it only works if DOM elements are: a) attached, and b) visible. So if the DOM is
   * initially unattached, or initially hidden while we fetch results, our focus() calls will be no-ops.
   * 
   * Instead we watch for attach+load events and call focus each time they happen.
   */
  private void setupFocusHandlers() {
    // auto-focus on attach
    view.addAttachHandler(new AttachEvent.Handler() {
      public void onAttachOrDetach(final AttachEvent event) {
        if (event.isAttached() && autoFocus) {
          triggerAutoFocus();
        }
      }
    });
    // or when loading changes to false
    loading.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(final PropertyChangedEvent<Boolean> event) {
        if (FALSE.equals(event.getNewValue()) && autoFocus) {
          triggerAutoFocus();
        }
      }
    });
  }

  private void triggerAutoFocus() {
    for (final AbstractLine line : lines) {
      if (line instanceof CanAutoFocus) {
        ((CanAutoFocus) line).autoFocus();
        break;
      }
    }
  }

  private static void deferIfInGwt(ScheduledCommand command) {
    if (GWT.isClient()) {
      Scheduler.get().scheduleDeferred(command);
    } else {
      command.execute();
    }
  }

}
