package org.gwtmpv.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;

public class StubIsStyle implements IsStyle {

  private final Map<String, String> style = new HashMap<String, String>();

  @Override
  public String getOpacity() {
    return style.get("opacity");
  }

  @Override
  public void setOpacity(final double value) {
    style.put("opacity", String.valueOf(value));
  }

  public Map<String, String> getStyle() {
    return style;
  }

  @Override
  public void setWidth(final double value, final Unit unit) {
    style.put("width", value + unit.getType());
  }

  @Override
  public void setHeight(final double value, final Unit unit) {
    style.put("height", value + unit.getType());
  }

  @Override
  public void setColor(final String value) {
    style.put("color", value);
  }

  @Override
  public void setFontWeight(final FontWeight value) {
    style.put("font-weight", value.getCssName());
  }

  @Override
  public void setBackgroundColor(final String value) {
    style.put("backgroundColor", value);
  }

  @Override
  public void setLeft(final double value, final Unit unit) {
    style.put("left", value + unit.getType());
  }

  @Override
  public void setTop(final double value, final Unit unit) {
    style.put("top", value + unit.getType());
  }

  @Override
  public void setMargin(final double value, final Unit unit) {
    style.put("margin", value + unit.getType());
  }

  @Override
  public void setMarginBottom(final double value, final Unit unit) {
    style.put("margin-bottom", value + unit.getType());
  }

  @Override
  public void setMarginLeft(final double value, final Unit unit) {
    style.put("margin-left", value + unit.getType());
  }

  @Override
  public void setMarginRight(final double value, final Unit unit) {
    style.put("margin-right", value + unit.getType());
  }

  @Override
  public void setMarginTop(final double value, final Unit unit) {
    style.put("margin-top", value + unit.getType());
  }

  @Override
  public void setOverflow(final Overflow value) {
    style.put("overflow", value.getCssName());
  }

  @Override
  public void setBorderColor(final String value) {
    style.put("border-color", value);
  }

  @Override
  public void setBorderStyle(final BorderStyle value) {
    style.put("border-style", value.getCssName());

  }

  @Override
  public void setBorderWidth(final double value, final Unit unit) {
    style.put("border-width", value + unit.getType());
  }

}
