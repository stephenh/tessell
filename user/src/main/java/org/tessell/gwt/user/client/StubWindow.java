package org.tessell.gwt.user.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tessell.gwt.http.client.IsUrlBuilder;
import org.tessell.gwt.http.client.StubUrlBuilder;
import org.tessell.place.tokenizer.Codec;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;

public class StubWindow implements IsWindow {

  private final EventBus handlers = new SimplerEventBus();
  public final List<String> alerts = new ArrayList<String>();
  public String opened;
  public String assigned;
  public String replaced;
  public boolean reloaded;
  public int x = -1;
  public int y = -1;
  public String protocol = "http";
  public String hostName = "server";
  public String path = "/app";
  public String port = "80";
  public String queryString = "";
  public String hash = "";
  public String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11";

  public void setLocation(String url) {
    try {
      URL u = new URL(url);
      protocol = u.getProtocol();
      hostName = u.getHost();
      path = u.getPath();
      port = u.getPort() == -1 ? "80" : Integer.toString(u.getPort());
      queryString = u.getQuery() == null ? "" : "?" + u.getQuery();
      hash = u.getRef() == null ? "" : "#" + u.getRef();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void open(final String url, final String name, final String features) {
    opened = url + "," + name + "," + features;
  }

  @Override
  public void scrollTo(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int getClientHeight() {
    return 0;
  }

  @Override
  public int getClientWidth() {
    return 0;
  }

  @Override
  public int getScrollLeft() {
    return x;
  }

  @Override
  public int getScrollTop() {
    return y;
  }

  @Override
  public int getScrollHeight() {
    return 0;
  }

  @Override
  public int getScrollWidth() {
    return 0;
  }

  @Override
  public HandlerRegistration addResizeHandler(final ResizeHandler handler) {
    return handlers.addHandler(ResizeEvent.getType(), handler);
  }

  @Override
  public void alert(String message) {
    alerts.add(message);
  }

  @Override
  public void reload() {
    reloaded = true;
  }

  @Override
  public IsUrlBuilder createUrlBuilder() {
    // copy/pasted from GWT's Window.Location.createUrlBuilder()
    IsUrlBuilder builder = new StubUrlBuilder();
    builder.setProtocol(getProtocol());
    builder.setHost(getHost());
    String path = getPath();
    if (path != null && path.length() > 0) {
      builder.setPath(path);
    }
    String hash = getHash();
    if (hash != null && hash.length() > 0) {
      builder.setHash(hash);
    }
    String port = getPort();
    if (port != null && port.length() > 0) {
      builder.setPort(Integer.parseInt(port));
    }
    // Add query parameters.
    Map<String, List<String>> params = buildListParamMap(getQueryString());
    for (Map.Entry<String, List<String>> entry : params.entrySet()) {
      List<String> values = new ArrayList<String>(entry.getValue());
      builder.setParameter(entry.getKey(), values.toArray(new String[values.size()]));
    }
    return builder;
  }

  @Override
  public void assign(String newUrl) {
    assigned = newUrl;
  }

  @Override
  public void replace(String newUrl) {
    replaced = newUrl;
  }

  @Override
  public String getHash() {
    return hash;
  }

  @Override
  public String getHost() {
    return hostName + ":" + port;
  }

  @Override
  public String getHostName() {
    return hostName;
  }

  @Override
  public String getHref() {
    return protocol + "://" //
      + hostName
      + (port.equals("80") ? "" : ":" + port)
      + path
      + queryString // includes ?
      + (hash.equals("") ? "" : hash); // includes #
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public String getPort() {
    return port;
  }

  @Override
  public String getProtocol() {
    return protocol;
  }

  @Override
  public String getQueryString() {
    return queryString;
  }

  @Override
  public String getParameter(String name) {
    return buildParamMap(getQueryString()).get(name);
  }

  @Override
  public String getUserAgent() {
    return userAgent;
  }

  @Override
  public Map<String, List<String>> getParameterMap() {
    return buildListParamMap(getQueryString());
  }

  private static Map<String, String> buildParamMap(String queryString) {
    // copy/pasted from Window.Location.buildListParamMap
    Map<String, String> paramMap = new HashMap<String, String>();
    if (queryString != null && queryString.length() > 1) {
      String qs = queryString.substring(1);
      for (String kvPair : qs.split("&")) {
        String[] kv = kvPair.split("=", 2);
        if (kv.length > 1) {
          paramMap.put(kv[0], Codec.decodeQueryString(kv[1]));
        } else {
          paramMap.put(kv[0], "");
        }
      }
    }
    return paramMap;
  }

  private static Map<String, List<String>> buildListParamMap(String queryString) {
    // copy/pasted from Window.Location.buildListParamMap
    Map<String, List<String>> out = new HashMap<String, List<String>>();
    if (queryString != null && queryString.length() > 1) {
      String qs = queryString.substring(1);
      for (String kvPair : qs.split("&")) {
        String[] kv = kvPair.split("=", 2);
        if (kv[0].length() == 0) {
          continue;
        }
        List<String> values = out.get(kv[0]);
        if (values == null) {
          values = new ArrayList<String>();
          out.put(kv[0], values);
        }
        values.add(kv.length > 1 ? Codec.decodeQueryString(kv[1]) : "");
      }
    }
    for (Map.Entry<String, List<String>> entry : out.entrySet()) {
      entry.setValue(Collections.unmodifiableList(entry.getValue()));
    }
    out = Collections.unmodifiableMap(out);
    return out;
  }

}
