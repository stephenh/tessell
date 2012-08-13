package org.tessell.gwt.user.client;

import java.util.List;
import java.util.Map;

import org.tessell.gwt.http.client.IsUrlBuilder;

import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface IsWindow {

  void alert(String message);

  void open(String url, String name, String features);

  void scrollTo(int x, int y);

  int getScrollTop();

  int getScrollLeft();

  int getScrollHeight();

  int getScrollWidth();

  int getClientHeight();

  int getClientWidth();

  HandlerRegistration addResizeHandler(ResizeHandler handler);

  // location methods

  void assign(String newUrl);

  void reload();

  void replace(String newUrl);

  IsUrlBuilder createUrlBuilder();

  String getHash();

  String getHost();

  String getHostName();

  String getHref();

  String getPath();

  String getPort();

  String getProtocol();

  String getQueryString();

  String getParameter(String name);

  String getUserAgent();

  Map<String, List<String>> getParameterMap();

}
