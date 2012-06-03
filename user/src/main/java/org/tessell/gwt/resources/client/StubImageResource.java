package org.tessell.gwt.resources.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public class StubImageResource implements ImageResource {

  private final String name;
  private final String url;

  public StubImageResource(String name, String url) {
    this.name = name;
    this.url = url;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public SafeUri getSafeUri() {
    return UriUtils.fromTrustedString(url);
  }

  @Override
  public String getURL() {
    return url;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public int getLeft() {
    return 0;
  }

  @Override
  public int getTop() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public boolean isAnimated() {
    return false;
  }

}
