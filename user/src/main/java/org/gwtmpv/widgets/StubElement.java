package org.gwtmpv.widgets;

import static org.gwtmpv.util.StringUtils.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;

public class StubElement implements IsElement, HasStubCss {

  private String id;
  private String debugId;
  private String innerText;
  private String innerHTML;
  private StubWidget widget;
  private final ArrayList<String> styleNames = new ArrayList<String>();
  private final ArrayList<StubElement> children = new ArrayList<StubElement>();
  private final Map<String, String> attributes = new HashMap<String, String>();
  private final StubStyle style = new StubStyle();

  @Override
  public String getInnerText() {
    return innerText;
  }

  @Override
  public void setInnerText(final String innerText) {
    this.innerText = innerText == null ? "" : innerText;
  }

  @Override
  public void addStyleName(final String styleName) {
    if (!styleNames.contains(styleName)) {
      styleNames.add(styleName);
    }
  }

  @Override
  public void removeStyleName(final String styleName) {
    styleNames.remove(styleName);
  }

  public List<String> getStyleNames() {
    return styleNames;
  }

  @Override
  public StubStyle getStyle() {
    return style;
  }

  @Override
  public String getInnerHTML() {
    return innerHTML;
  }

  @Override
  public void setInnerHTML(final String html) {
    innerHTML = html;
  }

  @Override
  public void ensureDebugId(final String id) {
    debugId = id;
  }

  @Override
  public String toString() {
    return debugId;
  }

  @Override
  public String getStyleName() {
    return join(styleNames, " ");
  }

  @Override
  public String getText() {
    return getInnerText();
  }

  @Override
  public void setText(final String text) {
    setInnerText(text);
  }

  @Override
  public String getAttribute(final String name) {
    return attributes.get(name);
  }

  @Override
  public void setAttribute(final String name, final String value) {
    attributes.put(name, value);
  }

  @Override
  public int getOffsetHeight() {
    return 0;
  }

  @Override
  public int getOffsetWidth() {
    return 0;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(final String id) {
    this.id = id;
  }

  @Override
  public Element asElement() {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public void appendChild(IsElement element) {
    children.add((StubElement) element);
  }

  public ArrayList<StubElement> getChildren() {
    return children;
  }

  @Override
  public void appendChild(IsWidget widget) {
    children.add((StubElement) widget.getIsElement());
  }

  public StubWidget getWidget() {
    return widget;
  }

  public void setWidget(StubWidget owner) {
    widget = owner;
  }

}
