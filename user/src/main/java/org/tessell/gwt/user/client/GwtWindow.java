package org.tessell.gwt.user.client;

import java.util.List;
import java.util.Map;

import org.tessell.gwt.http.client.GwtUrlBuilder;
import org.tessell.gwt.http.client.IsUrlBuilder;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;

public class GwtWindow implements IsWindow {

  @Override
  public void open(final String url, final String name, final String features) {
    Window.open(url, name, features);
  }

  @Override
  public void scrollTo(final int x, final int y) {
    Window.scrollTo(x, y);
  }

  @Override
  public int getClientHeight() {
    return Window.getClientHeight();
  }

  @Override
  public int getClientWidth() {
    return Window.getClientWidth();
  }

  @Override
  public int getScrollLeft() {
    return Window.getScrollLeft();
  }

  @Override
  public int getScrollTop() {
    return Window.getScrollTop();
  }

  @Override
  public HandlerRegistration addResizeHandler(final ResizeHandler handler) {
    return Window.addResizeHandler(handler);
  }

  @Override
  public void alert(String message) {
    Window.alert(message);
  }

  @Override
  public int getScrollHeight() {
    return Document.get().getScrollHeight();
  }

  @Override
  public int getScrollWidth() {
    return Document.get().getScrollWidth();
  }

  @Override
  public void reload() {
    Window.Location.reload();
  }

  @Override
  public IsUrlBuilder createUrlBuilder() {
    return new GwtUrlBuilder(Window.Location.createUrlBuilder());
  }

  @Override
  public void assign(String newUrl) {
    Window.Location.assign(newUrl);
  }

  @Override
  public void replace(String newUrl) {
    Window.Location.replace(newUrl);
  }

  @Override
  public String getHash() {
    return Window.Location.getHash();
  }

  @Override
  public String getHost() {
    return Window.Location.getHost();
  }

  @Override
  public String getHostName() {
    return Window.Location.getHostName();
  }

  @Override
  public String getHref() {
    return Window.Location.getHref();
  }

  @Override
  public String getPath() {
    return Window.Location.getPath();
  }

  @Override
  public String getPort() {
    return Window.Location.getPort();
  }

  @Override
  public String getProtocol() {
    return Window.Location.getProtocol();
  }

  @Override
  public String getQueryString() {
    return Window.Location.getQueryString();
  }

  @Override
  public String getParameter(String name) {
    return Window.Location.getParameter(name);
  }

  @Override
  public String getUserAgent() {
    return Window.Navigator.getUserAgent();
  }

  @Override
  public Map<String, List<String>> getParameterMap() {
    return Window.Location.getParameterMap();
  }

}
