package org.tessell.widgets.form;

import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.StringProperty;

public class EmployeeModel {
  public final StringProperty firstName = stringProperty("firstName");
  public final StringProperty lastName = stringProperty("lastName");
  public final IntegerProperty employerId = integerProperty("employerId");
}
