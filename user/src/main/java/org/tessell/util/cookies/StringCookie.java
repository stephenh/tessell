package org.tessell.util.cookies;

import java.util.Date;

import org.tessell.util.cookies.facade.IsCookies;

public class StringCookie extends AbstractCookie<String> {

  public StringCookie(final IsCookies cookies, final String name) {
    super(cookies, name);
  }

  public StringCookie(final IsCookies cookies, String name, String domain, Date expires, boolean secure) {
    super(cookies, name, domain, expires, secure);
  }

  @Override
  protected String fromString(String value) {
    return value;
  }

  @Override
  protected String toString(String value) {
    return value;
  }

}
