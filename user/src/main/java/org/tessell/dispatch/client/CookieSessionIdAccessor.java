package org.tessell.dispatch.client;

import com.google.gwt.user.client.Cookies;

/**
 * Uses a cookie value for the CSRF session id.
 *
 * By default we use "CSRFToken" as the cookie name.
 */
public class CookieSessionIdAccessor implements SessionIdAccessor {

  private final String cookieName;

  public CookieSessionIdAccessor() {
    this("CSRFToken");
  }

  public CookieSessionIdAccessor(String cookieName) {
    this.cookieName = cookieName;
  }

  @Override
  public String getSessionId() {
    return Cookies.getCookie(cookieName);
  }

}
