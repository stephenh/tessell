package org.gwtmpv.widgets;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

public interface IsStyle {

  String getOpacity();

  void setOpacity(double value);

  String getWidth();

  void setWidth(double value, Unit unit);

  String getHeight();

  void setHeight(double value, Unit unit);

  void clearHeight();

  void setFontWeight(FontWeight value);

  void setColor(String value);

  void setBackgroundColor(String value);

  void setLeft(double value, Unit unit);

  void setTop(double value, Unit unit);

  void setBottom(double value, Unit unit);

  void setRight(double value, Unit unit);

  void setMargin(double value, Unit unit);

  void setMarginTop(double value, Unit unit);

  void setMarginBottom(double value, Unit unit);

  void setMarginLeft(double value, Unit unit);

  void setMarginRight(double value, Unit unit);

  void setOverflow(Overflow value);

  void setBorderColor(String value);

  void setBorderStyle(BorderStyle value);

  void setBorderWidth(double value, Unit unit);

  String getDisplay();

  void setDisplay(Display value);

  void clearDisplay();

  String getProperty(String name);

  void setProperty(String name, String value);

  void setProperty(String name, double value, Unit unit);

  void setPropertyPx(String name, int value);

  String getPosition();

  void setPosition(Position position);

  String getFontSize();

  void setFontSize(double value, Unit unit);

  void setFloat(Float value);

  void clearFloat();

  void setZIndex(int value);

  void clearZIndex();

}
