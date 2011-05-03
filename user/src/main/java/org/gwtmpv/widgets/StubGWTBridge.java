package org.gwtmpv.widgets;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWTBridge;
import com.google.gwt.core.client.impl.SchedulerImpl;

public class StubGWTBridge extends GWTBridge {

  private static final Logger log = Logger.getLogger(StubGWTBridge.class.getName());
  private static final StubGWTBridge bridge = new StubGWTBridge();
  private StubScheduler scheduler;

  public static StubScheduler getScheduler() {
    return bridge.scheduler;
  }

  public static void install() {
    try {
      final Method m = GWT.class.getDeclaredMethod("setBridge", GWTBridge.class);
      m.setAccessible(true);
      m.invoke(null, bridge);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T create(final Class<?> classLiteral) {
    if (classLiteral == SchedulerImpl.class) {
      if (scheduler == null) {
        scheduler = new StubScheduler();
      }
      return (T) scheduler;
    }
    return null;
  }

  @Override
  public String getVersion() {
    return "stub";
  }

  @Override
  public boolean isClient() {
    return false;
  }

  @Override
  public void log(final String message, final Throwable e) {
    log.log(Level.SEVERE, message, e);
  }

}
