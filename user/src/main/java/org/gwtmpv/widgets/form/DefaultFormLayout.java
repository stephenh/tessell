package org.gwtmpv.widgets.form;

import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.form.actions.FormAction;
import org.gwtmpv.widgets.form.lines.FormLine;

/**
 * Lays out a form with the {@code ol li div} tags.
 * 
 * Based loosely on:
 * 
 * http://articles.sitepoint.com/print/fancy-form-design-css
 */
public class DefaultFormLayout implements FormLayout {

  /** An interface for the css classes we use. */
  public static interface Style {
    String form();

    String value();

    String label();

    String errors();

    String action();

    String actions();

    String lines();
  }

  /** A static version of {@code Style} that returns non-obfuscated class names. */
  public static class StaticStyle implements Style {
    @Override
    public String form() {
      return "form";
    }

    @Override
    public String value() {
      return "value";
    }

    @Override
    public String label() {
      return "label";
    }

    @Override
    public String errors() {
      return "errors";
    }

    @Override
    public String action() {
      return "action";
    }

    @Override
    public String actions() {
      return "actions";
    }

    @Override
    public String lines() {
      return "lines";
    }
  }

  private final Style style;

  public DefaultFormLayout() {
    this(new StaticStyle());
  }

  public DefaultFormLayout(Style style) {
    this.style = style;
  }

  @Override
  public void render(FormPresenter p, HTMLPanelBuilder hb) {
    formBegin(p, hb);

    linesBegin(p, hb);
    for (FormLine line : p.getFormLines()) {
      lineBegin(p, hb);

      labelBegin(p, hb);
      line.renderLabel(hb);
      labelEnd(p, hb);

      valueBegin(p, hb);
      line.renderValue(hb);
      errorsBegin(p, hb);
      line.renderErrors(hb);
      errorsEnd(p, hb);
      valueEnd(p, hb);

      lineEnd(p, hb);
    }
    linesEnd(p, hb);

    if (p.getFormActions().size() > 0) {
      actionsBegin(p, hb);
      for (FormAction action : p.getFormActions()) {
        actionBegin(p, hb);
        action.renderAction(hb);
        actionEnd(p, hb);
      }
      actionsEnd(p, hb);
    }

    formEnd(p, hb);
  }

  @Override
  public void formBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div class=\"" + style.form() + "\">");
  }

  @Override
  public void formEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</div>");
  }

  @Override
  public void lineBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<li>");
  }

  @Override
  public void lineEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</li>");
  }

  @Override
  public void linesBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div class=\"" + style.lines() + "\">");
    hb.add("<ol>");
  }

  @Override
  public void linesEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</ol>");
    hb.add("</div>");
  }

  @Override
  public void labelBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div class=\"" + style.label() + "\">");
  }

  @Override
  public void labelEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</div>");
  }

  @Override
  public void valueBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div class=\"" + style.value() + "\">");
  }

  @Override
  public void valueEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</div>");
  }

  @Override
  public void errorsBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div class=\"" + style.errors() + "\">");
  }

  @Override
  public void errorsEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</div>");
  }

  @Override
  public void actionsBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div class=\"" + style.actions() + "\">");
  }

  @Override
  public void actionsEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</div>");
  }

  @Override
  public void actionBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<span class=\"" + style.action() + "\">");
  }

  @Override
  public void actionEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</span>");
  }

}
