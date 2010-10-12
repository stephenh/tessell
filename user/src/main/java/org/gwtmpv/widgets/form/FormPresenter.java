package org.gwtmpv.widgets.form;

import static org.gwtmpv.widgets.Widgets.newFlowPanel;

import java.util.ArrayList;

import org.gwtmpv.bus.EventBus;
import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.presenter.BasicPresenter;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.IsFlowPanel;
import org.gwtmpv.widgets.IsHTMLPanel;
import org.gwtmpv.widgets.form.lines.FormLine;
import org.gwtmpv.widgets.form.lines.TextBoxFormLine;

import com.google.gwt.event.logical.shared.AttachEvent;

public class FormPresenter extends BasicPresenter<IsFlowPanel> {

  private final ArrayList<FormLine> lines = new ArrayList<FormLine>();
  private final Binder binder = new Binder(this);
  private boolean needsRender = true;

  public FormPresenter(EventBus eventBus) {
    super(newFlowPanel(), eventBus);
  }

  @Override
  public void onBind() {
    super.onBind();
    registerHandler(view.addAttachHandler(new OnViewAttached()));
  }

  /** Adds a {@link TextBoxFormLine} for {@code p}. */
  public void addTextBox(final Property<String> p) {
    add(new TextBoxFormLine(binder, p));
  }

  /** Renders all of our lines into our view. */
  protected void render() {
    HTMLPanelBuilder hb = new HTMLPanelBuilder();
    hb.add("<div>");
    for (FormLine line : lines) {
      line.render(hb);
    }
    hb.add("</div>");
    insertHtml(hb.toHTMLPanel());
    needsRender = false;
  }

  private void add(FormLine line) {
    lines.add(line);
    needsRender = true;
  }

  private void insertHtml(IsHTMLPanel panel) {
    if (view.getWidgetCount() > 0) {
      view.remove(0);
    }
    view.add(panel);
  }

  private final class OnViewAttached implements AttachEvent.Handler {
    public void onAttachOrDetach(AttachEvent event) {
      if (needsRender) {
        render();
      }
    }
  }

}
