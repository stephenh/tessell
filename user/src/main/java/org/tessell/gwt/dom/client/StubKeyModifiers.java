package org.tessell.gwt.dom.client;

public class StubKeyModifiers {

  boolean alt;
  boolean control;
  boolean meta;
  boolean shift;

  public StubKeyModifiers withAlt() {
    alt = true;
    return this;
  }

  public StubKeyModifiers withControl() {
    control = true;
    return this;
  }

  public StubKeyModifiers withMeta() {
    meta = true;
    return this;
  }

  public StubKeyModifiers withShift() {
    shift = true;
    return this;
  }

}
