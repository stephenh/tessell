package org.gwtmpv.widgets;

import com.google.gwt.resources.client.DataResource;

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

}
