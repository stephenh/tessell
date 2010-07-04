package org.gwtmpv.tests.model.validation.rules;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.rules.Length;
import org.gwtmpv.model.validation.rules.Required;

public class Foo {

  public PropertyGroup all = new PropertyGroup("some", "some invalid");
  public StringProperty name = stringProperty("name");
  public StringProperty description = stringProperty("description");
  public BooleanProperty condition = booleanProperty("condition", true);

  public Foo() {
    new Required(name, "name required");
    new Length(name, "name length");
    new Required(description, "description required");
    all.add(name, description);
  }

}