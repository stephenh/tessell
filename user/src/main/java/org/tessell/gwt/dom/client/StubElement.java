package org.tessell.gwt.dom.client;

import static org.tessell.util.StringUtils.join;

import java.util.*;

import org.tessell.widgets.StubWidget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;

public class StubElement implements IsElement {

  private String innerText = "";
  private String innerHTML = "";
  private StubWidget widget;

  private int scrollTop;
  private int scrollLeft;
  private final ArrayList<String> styleNames = new ArrayList<String>();
  private final ArrayList<StubElement> children = new ArrayList<StubElement>();
  private final Map<String, String> attributes = new HashMap<String, String>();
  private final StubStyle style = new StubStyle();
  public StubElement parent;
  public int offsetHeight;
  public int offsetWidth;
  public int offsetTop;
  public int offsetLeft;
  public int clientHeight;
  public int clientWidth;
  public int scrollHeight;
  public int scrollWidth;
  public boolean wasRemovedFromParent;
  public boolean wasScrolledIntoView;

  @Override
  public String getInnerText() {
    return innerText;
  }

  @Override
  public void setInnerText(final String innerText) {
    this.innerText = innerText == null ? "" : innerText;
    innerHTML = "";
  }

  @Override
  public void addStyleName(final String styleName) {
    if (styleName == null || "".equals(styleName)) {
      throw new RuntimeException("styleName must not be empty");
    }
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
    innerHTML = html == null ? "" : html;
    innerText = "";
  }

  @Override
  public void ensureDebugId(final String id) {
    // don't bother with a gwt-debug prefix for stubs
    setId(id);
  }

  @Override
  public String toString() {
    return getId();
  }

  @Override
  public String getStyleName() {
    return join(styleNames, " ");
  }

  @Override
  public void setStyleName(String styleName) {
    styleNames.clear();
    styleNames.addAll(Arrays.asList(styleName.split(" ")));
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
  public int getOffsetTop() {
    return offsetTop;
  }

  @Override
  public int getOffsetLeft() {
    return offsetLeft;
  }

  @Override
  public int getOffsetHeight() {
    return offsetHeight;
  }

  @Override
  public int getOffsetWidth() {
    return offsetWidth;
  }

  @Override
  public String getId() {
    return getAttribute("id");
  }

  @Override
  public void setId(final String id) {
    setAttribute("id", id);
  }

  @Override
  public Element asElement() {
    throw new IllegalStateException("This is a stub");
  }

  @Override
  public void insertFirst(IsElement element) {
    children.add(0, (StubElement) element);
    setParent(element, this);
  }

  @Override
  public void appendChild(IsElement element) {
    children.add((StubElement) element);
    setParent(element, this);
  }

  public ArrayList<StubElement> getChildren() {
    return children;
  }

  public StubWidget getWidget() {
    return widget;
  }

  public void setWidget(StubWidget owner) {
    widget = owner;
  }

  @Override
  public int getClientHeight() {
    return clientHeight;
  }

  @Override
  public int getClientWidth() {
    return clientWidth;
  }

  @Override
  public int getScrollTop() {
    return scrollTop;
  }

  @Override
  public void setScrollTop(int scrollTop) {
    this.scrollTop = scrollTop;
  }

  @Override
  public int getScrollLeft() {
    return scrollLeft;
  }

  @Override
  public void setScrollLeft(int scrollLeft) {
    this.scrollLeft = scrollLeft;
  }

  @Override
  public int getScrollHeight() {
    return scrollHeight;
  }

  @Override
  public int getScrollWidth() {
    return scrollWidth;
  }

  @Override
  public void removeFromParent() {
    if (parent != null) {
      // removeChild will unset our parent
      parent.removeChild(this);
    }
    wasRemovedFromParent = true;
  }

  @Override
  public void setInnerSafeHtml(SafeHtml html) {
    setInnerHTML(html.asString());
  }

  @Override
  public void scrollIntoView() {
    wasScrolledIntoView = true;
  }

  @Override
  public void removeChild(IsElement element) {
    children.remove(element);
    setParent(element, null);
  }

  @Override
  public void removeAllChildren() {
    for (Iterator<StubElement> i = children.iterator(); i.hasNext();) {
      setParent(i.next(), null);
      i.remove();
    }
  }

  @Override
  public int getChildCount() {
    return children.size();
  }

  @Override
  public IsElement getParentElement() {
    return parent;
  }

  private static void setParent(IsElement element, IsElement parent) {
    ((StubElement) element).parent = (StubElement) parent;
  }

}
