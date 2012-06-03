package org.tessell.gwt.resources.client;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public class StubDataResource implements DataResource {

  private final String name;
  private final String url;

  public StubDataResource(final String name, final String url) {
    this.name = name;
    this.url = url;
  }

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public SafeUri getSafeUri() {
    return UriUtils.fromTrustedString(url);
  }

}
