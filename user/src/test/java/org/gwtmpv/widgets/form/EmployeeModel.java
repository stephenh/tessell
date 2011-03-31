package org.gwtmpv.widgets.form;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.properties.StringProperty;

public class EmployeeModel {
  public final StringProperty firstName = stringProperty("firstName");
  public final StringProperty lastName = stringProperty("lastName");
  public final IntegerProperty employerId = integerProperty("employerId");
}
