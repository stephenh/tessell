package org.tessell.gwt.http.client;

import com.google.gwt.http.client.UrlBuilder;

public class GwtUrlBuilder implements IsUrlBuilder {

  private final UrlBuilder b;

  public GwtUrlBuilder(UrlBuilder b) {
    this.b = b;
  }

  @Override
  public String buildString() {
    return b.buildString();
  }

  @Override
  public IsUrlBuilder removeParameter(String name) {
    b.removeParameter(name);
    return this;
  }

  @Override
  public IsUrlBuilder setHash(String hash) {
    b.setHash(hash);
    return this;
  }

  @Override
  public IsUrlBuilder setHost(String host) {
    b.setHost(host);
    return this;
  }

  @Override
  public IsUrlBuilder setParameter(String key, String... values) {
    b.setParameter(key, values);
    return this;
  }

  @Override
  public IsUrlBuilder setPath(String path) {
    b.setPath(path);
    return this;
  }

  @Override
  public IsUrlBuilder setPort(int port) {
    b.setPort(port);
    return this;
  }

  @Override
  public IsUrlBuilder setProtocol(String protocol) {
    b.setProtocol(protocol);
    return this;
  }

}
