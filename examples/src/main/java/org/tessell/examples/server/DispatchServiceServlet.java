package org.tessell.examples.server;

import org.tessell.dispatch.server.ActionDispatch;
import org.tessell.dispatch.server.DefaultActionDispatch;
import org.tessell.dispatch.server.servlet.AbstractDispatchServiceServlet;
import org.tessell.examples.server.handlers.GetEmployeeHandler;

public class DispatchServiceServlet extends AbstractDispatchServiceServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected ActionDispatch getActionDispatch() {
    DefaultActionDispatch d = new DefaultActionDispatch();
    d.addHandler(new GetEmployeeHandler());
    return d;
  }

}
