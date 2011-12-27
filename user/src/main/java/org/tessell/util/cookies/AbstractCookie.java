package org.tessell.util.cookies;

import java.util.Date;

import org.tessell.util.cookies.facade.IsCookies;

public abstract class AbstractCookie<T> implements Cookie<T> {

  private final IsCookies cookies;
  private final String name;
  private final String domain;
  private final Date expires;
  public final boolean secure;

  public AbstractCookie(final IsCookies cookies, final String name) {
    this.cookies = cookies;
    this.name = name;
    this.domain = null;
    this.expires = null;
    this.secure = false;
  }

  public AbstractCookie(IsCookies cookies, String name, String domain, Date expires, boolean secure) {
    this.cookies = cookies;
    this.name = name;
    this.domain = domain;
    this.expires = expires;
    this.secure = secure;
  }

  // TODO: Add a hashing/something approach for security
  // setCookie appends its value, so instead of null, pass empty string

  @Override
  public final T get() {
    String value = cookies.get(name);
    if (value != null) {
      return fromString(value);
    } else {
      return null;
    }
  }

  @Override
  public final void set(T value) {
    String asString = value == null ? null : toString(value);
    cookies.set(name, asString, domain, expires, secure);
  }

  @Override
  public final void unset() {
    cookies.remove(name);
  }

  // should use a ValueBox Parser instead
  protected abstract T fromString(String value);

  // should use a ValueBox Renderer instead
  protected abstract String toString(T value);

}
