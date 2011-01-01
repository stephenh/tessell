package org.gwtmpv.widgets.form;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;
import static org.gwtmpv.widgets.Widgets.newFlowPanel;

import java.util.ArrayList;

import org.gwtmpv.model.commands.UiCommand;
import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.presenter.BasicPresenter;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.IsFlowPanel;
import org.gwtmpv.widgets.IsHTMLPanel;
import org.gwtmpv.widgets.form.lines.FormLine;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;

/** Given a list of bindings, handles the boilerplate HTML layout/logic of forms. */
public class FormPresenter extends BasicPresenter<IsFlowPanel> {

  private final ArrayList<FormLine> formLines = new ArrayList<FormLine>();
  private final PropertyGroup all = new PropertyGroup("all", null);
  private final String id;
  private final Binder binder = new Binder(this);
  private final FormLayout layout;
  private boolean needsRender = true;
  private UiCommand defaultCommand;

  public FormPresenter(String id) {
    super(newFlowPanel());
    this.id = id;
    layout = new DefaultFormLayout();
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

  public UiCommand getDefaultCommand() {
    return defaultCommand;
  }

  public void setDefaultCommand(UiCommand defaultCommand) {
    this.defaultCommand = defaultCommand;
  }

  public void triggerDefaultCommand() {
    if (defaultCommand != null) {
      defaultCommand.execute();
    }
  }

  public void watchForEnterKey(final HasKeyUpHandlers source) {
    source.addKeyUpHandler(new KeyUpHandler() {
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KEY_ENTER) {
          triggerDefaultCommand();
        }
      }
    });
  }

  private final class OnViewAttached implements AttachEvent.Handler {
    public void onAttachOrDetach(AttachEvent event) {
      if (needsRender) {
        render();
      }
    }
  }

}
