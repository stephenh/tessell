package org.tessell.util.cookies;

import com.google.gwt.user.client.TakesValue;

/** Abstracts out getting/settings values out of a cookie. */
public interface IsCookie<T> extends TakesValue<T> {

  void clear();

}
