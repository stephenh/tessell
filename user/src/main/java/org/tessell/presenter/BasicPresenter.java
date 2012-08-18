package org.tessell.presenter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.tessell.bus.AbstractBound;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.commands.UiCommand;
import org.tessell.model.dsl.*;
import org.tessell.model.properties.*;
import org.tessell.model.validation.rules.Rule;

import com.google.gwt.user.client.ui.Widget;

/** A basic presenter that tracks bound handler registrations. */
public abstract class BasicPresenter<V extends IsWidget> extends AbstractBound implements Presenter, com.google.gwt.user.client.ui.IsWidget {

  protected final V view;
  private ArrayList<Presenter> children;

  public BasicPresenter(final V view) {
    this.view = view;
  }

  /** @return The view for the presenter. */
  @Override
  public V getView() {
    if (!isBound()) {
      throw new IllegalStateException(this + " has not been bound");
    }
    return view;
  }

  /**
   * So we can pretend we're a widget to GWT APIs.
   *
   * I'm not entirely sure when this would be useful--perhaps for referencing
   * presenters in ui.xml files directly instead of needing panels.
   */
  @Override
  public Widget asWidget() {
    return getView().asWidget();
  }

  @Override
  protected void onBind() {
    super.onBind();
    // bind anybody that got added before we were bound
    if (children != null) {
      for (final Presenter child : children) {
        child.bind();
      }
    }
  }

  @Override
  protected void onUnbind() {
    super.onUnbind();
    if (children != null) {
      for (final Presenter child : children) {
        child.unbind();
      }
    }
  }

  /** Adds {@code child} as a child presenter, and binds it if we're already bound. */
  public <C extends Presenter> C addPresenter(final C child) {
    if (children().add(child)) {
      if (isBound()) {
        child.bind();
      }
    }
    return child;
  }

  /** Removes {@code child} as a child presenter, and unbinds it. */
  public void removePresenter(final Presenter child) {
    if (children().remove(child)) {
      child.unbind();
    } else {
      throw new NoSuchElementException("Presenter was not a child of ours " + child);
    }
  }

  /** @return our list of child presenters, lazily instantiated. */
  protected ArrayList<Presenter> children() {
    if (children == null) {
      children = new ArrayList<Presenter>();
    }
    return children;
  }

  // the Presenter.bind method means subclasses cannot use the
  // Binder.bind method as a static import, so we setup forwarding
  // methods here.
  protected static <P> PropertyBinder<P> bind(Property<P> property) {
    return Binder.bind(property);
  }

  protected static <P> ListPropertyBinder<P> bind(ListProperty<P> property) {
    return Binder.bind(property);
  }

  protected static RuleBinder bind(Rule rule) {
    return Binder.bind(rule);
  }

  protected static <P> StringPropertyBinder bind(StringProperty property) {
    return Binder.bind(property);
  }

  protected static <P> BooleanPropertyBinder bind(BooleanProperty property) {
    return Binder.bind(property);
  }

  protected static <E extends Enum<E>> EnumPropertyBinder<E> bind(EnumProperty<E> property) {
    return Binder.bind(property);
  }

  protected static UiCommandBinder bind(UiCommand command) {
    return Binder.bind(command);
  }
}
