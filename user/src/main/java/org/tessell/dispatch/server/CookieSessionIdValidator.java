package org.tessell.dispatch.server;

import java.util.UUID;

import javax.servlet.http.Cookie;

import com.google.gwt.user.server.Util;

/**
 * Reads/writes the secure CSRF value from a cookie.
 *
 * By default we use "CSRFToken" as the cookie name.
 *
 * See {@link #createNewCookie(String)} if you want to customize the cookie that
 * is created for the CSRF value, e.g. to use an HTTPS cookie (recommended in production).
 */
public class CookieSessionIdValidator implements SessionIdValidator {

  private final String cookieName;

  public CookieSessionIdValidator() {
    this("CSRFToken");
  }

  public CookieSessionIdValidator(String cookieName) {
    this.cookieName = cookieName;
  }

  @Override
  public String getToken(final ExecutionContext context) {
    Cookie c = Util.getCookie(context.getRequest(), cookieName, false);
    if (c != null) {
      return c.getValue();
    }
    return null;
  }

  @Override
  public void setTokenIfNeeded(ExecutionContext context) {
    Cookie c = Util.getCookie(context.getRequest(), cookieName, false);
    if (c == null) {
      context.getResponse().addCookie(createNewCookie(cookieName));
    }
  }

  /**
   * Creates a new cookie with a new session id.
   *
   * By default we use a UUID, as the value does not matter.
   *
   * Subclasses can override to, for example, use secure cookies in production.
   */
  protected Cookie createNewCookie(String cookieName) {
    final Cookie cookie = new Cookie(cookieName, UUID.randomUUID().toString());
    cookie.setPath("/");
    return cookie;
  }

}
