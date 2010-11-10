package org.gwtmpv.widgets.form;

import static org.gwtmpv.widgets.Widgets.newFlowPanel;

import java.util.ArrayList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.presenter.BasicPresenter;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.IsFlowPanel;
import org.gwtmpv.widgets.IsHTMLPanel;
import org.gwtmpv.widgets.form.lines.FormLine;

import com.google.gwt.event.logical.shared.AttachEvent;

public class FormPresenter extends BasicPresenter<IsFlowPanel> {

  private final ArrayList<FormLine> formLines = new ArrayList<FormLine>();

  private final String id;
  private final Binder binder = new Binder(this);
  private final FormLayout layout;
  private boolean needsRender = true;

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
    line.bind(id, binder);
    needsRender = true;
  }

  private void insertHtml(IsHTMLPanel panel) {
    if (view.getWidgetCount() > 0) {
      view.remove(0);
    }
    view.add(panel);
  }

  public ArrayList<FormLine> getFormLines() {
    return formLines;
  }

  private final class OnViewAttached implements AttachEvent.Handler {
    public void onAttachOrDetach(AttachEvent event) {
      if (needsRender) {
        render();
      }
    }
  }

}
