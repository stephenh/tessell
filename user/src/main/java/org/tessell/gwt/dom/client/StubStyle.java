package org.tessell.gwt.dom.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;

public class StubStyle implements IsStyle {

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

  @Override
  public String getDisplay() {
    return style.get("display");
  }

  @Override
  public void setDisplay(final Display value) {
    style.put("display", value.getCssName());
  }

  @Override
  public void clearProperty(final String name) {
    style.remove(name);
  }

  @Override
  public String getProperty(final String name) {
    return style.get(name);
  }

  @Override
  public void setProperty(final String name, final String value) {
    style.put(name, value);
  }

  @Override
  public void setProperty(final String name, final double value, final Unit unit) {
    style.put(name, value + unit.getType());
  }

  @Override
  public void setPropertyPx(final String name, final int value) {
    style.put(name, value + Unit.PX.getType());
  }

  @Override
  public String getFontSize() {
    return style.get("font-size");
  }

  @Override
  public String getPosition() {
    return style.get("position");
  }

  @Override
  public void setFontSize(final double value, final Unit unit) {
    style.put("font-size", value + unit.getType());
  }

  @Override
  public void setPosition(final Position position) {
    style.put("position", position.getCssName());
  }

  @Override
  public void clearFloat() {
    style.remove("float");
  }

  @Override
  public void setFloat(final Float value) {
    style.put("float", value.getCssName());
  }

  @Override
  public void clearDisplay() {
    style.remove("display");
  }

  @Override
  public void clearHeight() {
    style.remove("height");
  }

  @Override
  public String getWidth() {
    return style.get("width");
  }

  @Override
  public String getFontWeight() {
    return style.get("font-weight");
  }

  @Override
  public String getHeight() {
    return style.get("height");
  }

  @Override
  public void setBottom(double value, Unit unit) {
    style.put("bottom", value + unit.getType());
  }

  @Override
  public void setRight(double value, Unit unit) {
    style.put("right", value + unit.getType());
  }

  @Override
  public void setZIndex(int value) {
    style.put("zIndex", Integer.toString(value));
  }

  @Override
  public void clearZIndex() {
    style.remove("zIndex");
  }

  @Override
  public void setVisibility(Visibility value) {
    style.put("visibility", value.getCssName());
  }

  @Override
  public void clearVisibility() {
    style.remove("visibility");
  }

  @Override
  public void clearFontWeight() {
    style.remove("font-weight");
  }

  @Override
  public void clearColor() {
    style.remove("color");
  }

  @Override
  public void clearLeft() {
    style.remove("left");
  }

  @Override
  public void clearTop() {
    style.remove("top");
  }

  @Override
  public void clearBottom() {
    style.remove("bottom");
  }

  @Override
  public void clearRight() {
    style.remove("right");
  }

  @Override
  public void clearMargin() {
    style.remove("margin");
  }

  @Override
  public void setCursor(Cursor value) {
    style.put("cursor", value.getCssName());
  }

  @Override
  public void clearCursor() {
    style.remove("cursor");
  }

  @Override
  public void setPadding(double value, Unit unit) {
    style.put("padding", value + unit.getType());
  }

  @Override
  public void clearPadding() {
    style.remove("padding");
  }

  @Override
  public void clearOpacity() {
    style.remove("opacity");
  }

  @Override
  public void clearWidth() {
    style.remove("width");
  }

  @Override
  public String getBackgroundImage() {
    return style.get("background-image");
  }

  @Override
  public void setBackgroundImage(String value) {
    style.put("background-image", value);
  }

  @Override
  public void clearBackgroundImage() {
    style.remove("background-image");
  }

}
