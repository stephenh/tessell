package org.tessell.examples.client.model;

import org.tessell.examples.shared.dtos.EmployeeDto;

public class EmployeeModel extends EmployeeModelCodegen {

  public EmployeeModel(EmployeeDto dto) {
    super(dto);
    addRules();
    merge(dto);
  }

  private void addRules() {
  }

}
