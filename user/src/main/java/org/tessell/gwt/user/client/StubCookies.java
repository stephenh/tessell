package org.tessell.gwt.user.client;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class StubCookies implements IsCookies {

  private final Map<String, StubCookie> cookies = new LinkedHashMap<String, StubCookie>();

  public StubCookie getCookie(String name) {
    return cookies.get(name);
  }

  @Override
  public void remove(final String name) {
    cookies.remove(name);
  }

  @Override
  public void set(final String name, final String value) {
    cookies.put(name, new StubCookie(name, value, null, null, false));
  }

  @Override
  public void set(String name, String value, String domain, Date expires, boolean secure) {
    cookies.put(name, new StubCookie(name, value, domain, expires, secure));
  }

  @Override
  public String get(final String name) {
    StubCookie cookie = cookies.get(name);
    return cookie == null ? null : cookie.value;
  }

  public static class StubCookie {
    public final String name;
    public final String value;
    public final String domain;
    public final Date expires;
    public final boolean secure;

    public StubCookie(String name, String value, String domain, Date expires, boolean secure) {
      this.name = name;
      this.value = value;
      this.domain = domain;
      this.expires = expires;
      this.secure = secure;
    }
  }

}
