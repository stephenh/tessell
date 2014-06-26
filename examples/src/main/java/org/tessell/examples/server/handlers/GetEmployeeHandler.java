package org.tessell.examples.server.handlers;

import org.tessell.dispatch.server.ExecutionContext;
import org.tessell.dispatch.server.handlers.ActionHandler;
import org.tessell.examples.shared.dtos.EmployeeDto;
import org.tessell.examples.shared.messages.GetEmployeeAction;
import org.tessell.examples.shared.messages.GetEmployeeResult;

public class GetEmployeeHandler implements ActionHandler<GetEmployeeAction, GetEmployeeResult> {

  @Override
  public Class<GetEmployeeAction> getActionType() {
    return GetEmployeeAction.class;
  }

  @Override
  public GetEmployeeResult execute(GetEmployeeAction action, ExecutionContext context) {
    // do database lookup/etc.
    return new GetEmployeeResult(new EmployeeDto(action.getId(), "name", new Double(10.00)));
  }

  @Override
  public boolean skipCSRFCheck() {
    return false;
  }

}
