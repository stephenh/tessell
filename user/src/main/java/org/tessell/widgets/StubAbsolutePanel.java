package org.tessell.widgets;

import java.util.HashMap;
import java.util.Map;

public class StubAbsolutePanel extends StubComplexPanel implements IsAbsolutePanel {

  private final Map<IsWidget, Integer> lefts = new HashMap<IsWidget, Integer>();
  private final Map<IsWidget, Integer> tops = new HashMap<IsWidget, Integer>();

  @Override
  public void add(IsWidget w, int left, int top) {
    lefts.put(w, left);
    tops.put(w, left);
    super.add(w);
  }

  @Override
  public int getIsWidgetLeft(IsWidget w) {
    return lefts.get(w);
  }

  @Override
  public int getIsWidgetTop(IsWidget w) {
    return tops.get(w);
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    widgets.add(beforeIndex, w);
  }

  @Override
  public void insert(IsWidget w, int left, int top, int beforeIndex) {
    widgets.add(beforeIndex, w);
    lefts.put(w, left);
    tops.put(w, left);
  }

  @Override
  public void setWidgetPosition(IsWidget w, int left, int top) {
    lefts.put(w, left);
    tops.put(w, left);
  }

}
