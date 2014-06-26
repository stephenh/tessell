package org.tessell.examples.shared.messages;

import org.tessell.GenDispatch;
import org.tessell.In;
import org.tessell.Out;
import org.tessell.examples.shared.dtos.EmployeeDto;

@GenDispatch
public class GetEmployeeSpec {
  @In(1)
  Long id;
  
  @Out(1)
  EmployeeDto dto;
}
