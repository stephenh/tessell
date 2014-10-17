package org.tessell.tests.model.validation.rules;

import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.rules.Length;
import org.tessell.model.validation.rules.Required;

public class Foo {

  public PropertyGroup all = new PropertyGroup("some");
  public StringProperty name = stringProperty("name");
  public StringProperty description = stringProperty("description");
  public BooleanProperty condition = booleanProperty("condition", true);

  public Foo() {
    name.addRule(new Required("name required"));
    name.addRule(new Length("name length"));
    description.addRule(new Required("description required"));
    all.add(name, description);
  }

}
