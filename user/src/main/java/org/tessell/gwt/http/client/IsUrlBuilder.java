package org.tessell.gwt.http.client;

public interface IsUrlBuilder {

  public String buildString();

  public IsUrlBuilder removeParameter(String name);

  public IsUrlBuilder setHash(String hash);

  public IsUrlBuilder setHost(String host);

  public IsUrlBuilder setParameter(String key, String... values);

  public IsUrlBuilder setPath(String path);

  public IsUrlBuilder setPort(int port);

  public IsUrlBuilder setProtocol(String protocol);

}
