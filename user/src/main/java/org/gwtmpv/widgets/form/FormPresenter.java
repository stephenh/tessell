package org.gwtmpv.widgets.form;

import static org.gwtmpv.widgets.Widgets.newFlowPanel;

import java.util.ArrayList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.presenter.BasicPresenter;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.util.OnEnterKeyHandler;
import org.gwtmpv.widgets.IsFlowPanel;
import org.gwtmpv.widgets.IsHTMLPanel;
import org.gwtmpv.widgets.form.actions.FormAction;
import org.gwtmpv.widgets.form.lines.FormLine;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.logical.shared.AttachEvent;

/**
 * Given a list of bindings, handles the boilerplate HTML layout/logic of forms.
 *
 * A form is broken down into: lines and actions.
 */
public class FormPresenter extends BasicPresenter<IsFlowPanel> {

  private final ArrayList<FormLine> formLines = new ArrayList<FormLine>();
  private final ArrayList<FormAction> formActions = new ArrayList<FormAction>();
  private final PropertyGroup all = new PropertyGroup("all", null);
  private final String id;
  private final Binder binder = new Binder(this);
  private final FormLayout layout;
  private boolean needsRender = true;
  private FormAction defaultAction;

  public FormPresenter(String id) {
    this(id, new DefaultFormLayout());
  }

  public FormPresenter(String id, FormLayout layout) {
    super(newFlowPanel());
    this.id = id;
    this.layout = layout;
  }

  @Override
  public void onBind() {
    super.onBind();
    registerHandler(view.addAttachHandler(new OnViewAttached()));
  }

  /** @return a property that all of our fields are valid. */
  public PropertyGroup allValid() {
    return all;
  }

  /** Renders all of our lines into our view. */
  private void render() {
    HTMLPanelBuilder hb = new HTMLPanelBuilder();
    layout.render(this, hb);
    insertHtml(hb.toHTMLPanel());
    needsRender = false;
  }

  /** Adds {@code line}. */
  public void add(FormLine line) {
    formLines.add(line);
    line.bind(this, all, binder);
    needsRender = true;
  }

  /** Adds {@code action}. */
  public void add(FormAction action) {
    formActions.add(action);
    action.bind(this, binder);
    needsRender = true;
    defaultAction = action;
  }

  public void focusFirstLine() {
    if (formLines.size() > 0) {
      formLines.get(0).focus();
    }
  }

  private void insertHtml(IsHTMLPanel panel) {
    if (view.getWidgetCount() > 0) {
      view.remove(0);
    }
    view.add(panel);
  }

  public String getId() {
    return id;
  }

  public ArrayList<FormLine> getFormLines() {
    return formLines;
  }

  public ArrayList<FormAction> getFormActions() {
    return formActions;
  }

  public void setDefaultAction(FormAction defaultAction) {
    this.defaultAction = defaultAction;
  }

  public void watchForEnterKey(final HasAllKeyHandlers source) {
    OnEnterKeyHandler h = new OnEnterKeyHandler(new Runnable() {
      public void run() {
        if (defaultAction != null) {
          defaultAction.trigger();
        }
      }
    });
    source.addKeyUpHandler(h);
    source.addKeyDownHandler(h);
  }

  private final class OnViewAttached implements AttachEvent.Handler {
    public void onAttachOrDetach(AttachEvent event) {
      if (event.isAttached() && needsRender) {
        render();
      }
    }
  }

}
