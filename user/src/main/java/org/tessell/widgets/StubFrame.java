package org.tessell.widgets;

public class StubFrame extends StubWidget implements IsFrame {

  private String url;

  @Override
  public void setSize(final String width, final String height) {
  }

  @Override
  public void setUrl(final String url) {
    this.url = url;
  }

  @Override
  public String getUrl() {
    return url;
  }

}
