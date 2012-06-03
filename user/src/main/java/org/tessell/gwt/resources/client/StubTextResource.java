package org.tessell.gwt.resources.client;

import com.google.gwt.resources.client.TextResource;

public class StubTextResource implements TextResource {

  private final String name;
  private final String text;

  public StubTextResource(final String name, final String text) {
    this.name = name;
    this.text = text;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public String getName() {
    return name;
  }

}
