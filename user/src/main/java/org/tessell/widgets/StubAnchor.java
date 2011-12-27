package org.tessell.widgets;

public class StubAnchor extends StubFocusWidget implements IsAnchor {

  private String text;
  private String href;
  private String target;

  @Override
  public void click() {
    // override because even disabled anchors get click events
    fireEvent(new DummyClickEvent());
  }

  @Override
  public String getHref() {
    return href;
  }

  @Override
  public String getTarget() {
    return target;
  }

  @Override
  public void setHref(final String href) {
    this.href = href;
  }

  @Override
  public void setTarget(final String target) {
    this.target = target;
  }

  @Override
  public HorizontalAlignmentConstant getHorizontalAlignment() {
    return null;
  }

  @Override
  public void setHorizontalAlignment(final HorizontalAlignmentConstant align) {
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public void setName(final String name) {
  }

  @Override
  public String getHTML() {
    return text;
  }

  @Override
  public void setHTML(final String html) {
    text = html;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(final String text) {
    this.text = text;
  }

  @Override
  public boolean getWordWrap() {
    return false;
  }

  @Override
  public void setWordWrap(final boolean wrap) {
  }

  @Override
  public Direction getDirection() {
    return null;
  }

  @Override
  public void setDirection(final Direction direction) {
  }

}