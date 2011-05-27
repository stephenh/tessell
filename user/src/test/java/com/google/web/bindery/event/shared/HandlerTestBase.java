/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.web.bindery.event.shared;

import java.util.HashSet;

import junit.framework.TestCase;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.EventHandler;

/** Support code for handler tests. */
public abstract class HandlerTestBase extends TestCase {

  private final HashSet<EventHandler> active = new HashSet<EventHandler>();

  Adaptor adaptor1 = new Adaptor();

  MouseDownHandler mouse1 = new DummyMouseDownHandler(1);
  MouseDownHandler mouse2 = new DummyMouseDownHandler(2);
  MouseDownHandler mouse3 = new DummyMouseDownHandler(3);

  ClickHandler click1 = new DummyClickHandler(1);
  ClickHandler click2 = new DummyClickHandler(2);
  ClickHandler click3 = new DummyClickHandler(3);

  void add(EventHandler handler) {
    active.add(handler);
  }

  void assertFired(EventHandler... handler) {
    for (int i = 0; i < handler.length; i++) {
      assertTrue(handler[i] + " should have fired", active.contains(handler[i]));
    }
  }

  void assertNotFired(EventHandler... handler) {
    for (int i = 0; i < handler.length; i++) {
      assertFalse(handler[i] + " should not have fired", active.contains(handler[i]));
    }
  }

  void reset() {
    active.clear();
  }

  private final class DummyClickHandler implements ClickHandler {
    private final int number;

    public DummyClickHandler(int number) {
      this.number = number;
    }

    public void onClick(ClickEvent event) {
      add(this);
    }

    @Override
    public String toString() {
      return "click " + number;
    }
  }

  private final class DummyMouseDownHandler implements MouseDownHandler {
    private final int number;

    public DummyMouseDownHandler(int number) {
      this.number = number;
    }

    public void onMouseDown(MouseDownEvent event) {
      add(this);
    }

    @Override
    public String toString() {
      return "mouse " + number;
    }
  }

  class Adaptor implements ClickHandler, MouseDownHandler {
    public void onClick(ClickEvent event) {
      add(this);
    }

    public void onMouseDown(MouseDownEvent event) {
      add(this);
    }

    @Override
    public String toString() {
      return "adaptor 1";
    }
  }

}
