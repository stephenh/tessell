package org.tessell.util.cookies;

import static java.lang.Boolean.TRUE;

import java.util.Date;

import org.tessell.gwt.user.client.IsCookies;

public class BooleanCookie extends AbstractCookie<Boolean> {

  public BooleanCookie(final IsCookies cookies, final String name) {
    super(cookies, name);
  }

  public BooleanCookie(final IsCookies cookies, String name, String domain, Date expires, boolean secure) {
    super(cookies, name, domain, expires, secure);
  }

  @Override
  protected Boolean fromString(String value) {
    return "true".equals(value);
  }

  @Override
  protected String toString(Boolean value) {
    return TRUE.equals(value) ? "true" : "false";
  }

}
