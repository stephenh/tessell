package org.tessell.examples.server.handlers;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.tessell.examples.shared.messages.GetEmployeeAction;
import org.tessell.examples.shared.messages.GetEmployeeResult;

public class GetEmployeeHandlerTest {
  
  private final GetEmployeeHandler handler = new GetEmployeeHandler();

  @Test
  public void testGet() {
    GetEmployeeResult r = execute(1L);
    assertThat(r.getDto().id, is(1L));
  }
  
  private GetEmployeeResult execute(Long id) {
    // instead of null, use stub/mock http request/response if needed
    return handler.execute(new GetEmployeeAction(id), null);
  }
}
