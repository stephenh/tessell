package org.tessell.widgets;

import java.util.ArrayList;
import java.util.List;

public class StubTextList extends StubWidget implements IsTextList {

  private final List<String> list = new ArrayList<String>();
  private boolean enabled = true;
  private String childTag;
  private String childStyleName;

  @Override
  public void add(final String text) {
    list.add(text);
  }

  @Override
  public void remove(final String text) {
    list.remove(text);
  }

  @Override
  public void clear() {
    list.clear();
  }

  public List<String> getList() {
    return list;
  }

  @Override
  public boolean hasErrors() {
    return list.size() > 0;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String getChildTag() {
    return childTag;
  }

  @Override
  public void setChildTag(String childTag) {
    this.childTag = childTag;
  }

  @Override
  public String getChildStyleName() {
    return childStyleName;
  }

  @Override
  public void setChildStyleName(String childStyleName) {
    this.childStyleName = childStyleName;
  }

}
