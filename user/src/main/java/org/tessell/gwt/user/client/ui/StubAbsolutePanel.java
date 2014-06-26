package org.tessell.gwt.user.client.ui;

import java.util.HashMap;
import java.util.Map;

public class StubAbsolutePanel extends StubComplexPanel implements IsAbsolutePanel {

  private final Map<IsWidget, Integer> lefts = new HashMap<IsWidget, Integer>();
  private final Map<IsWidget, Integer> tops = new HashMap<IsWidget, Integer>();

  @Override
  public void add(com.google.gwt.user.client.ui.IsWidget w, int left, int top) {
    lefts.put((IsWidget) w, left);
    tops.put((IsWidget) w, left);
    super.add(w);
  }

  @Override
  public int getIsWidgetLeft(com.google.gwt.user.client.ui.IsWidget w) {
    return lefts.get(w);
  }

  @Override
  public int getIsWidgetTop(com.google.gwt.user.client.ui.IsWidget w) {
    return tops.get(w);
  }

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget w, int beforeIndex) {
    add(beforeIndex, w);
  }

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget w, int left, int top, int beforeIndex) {
    add(beforeIndex, w);
    lefts.put((IsWidget) w, left);
    tops.put((IsWidget) w, left);
  }

  @Override
  public void setWidgetPosition(com.google.gwt.user.client.ui.IsWidget w, int left, int top) {
    lefts.put((IsWidget) w, left);
    tops.put((IsWidget) w, left);
  }

}
