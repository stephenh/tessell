package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Before;
import org.junit.Test;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.model.properties.StringProperty;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class PropertyGroupNestedTest extends AbstractRuleTest {

  private final PropertyGroup parentAll = new PropertyGroup("all", "parent invalid");
  private final StringProperty parentName = stringProperty("parentName").req();
  private final PropertyGroup childAll = new PropertyGroup("all", "child invalid");
  private final StringProperty childName = stringProperty("childName").req();

  @Before
  public void setup() {
    parentAll.add(parentName);
    parentAll.add(childAll);
    childAll.add(childName);
  }

  @Test
  public void bothInitiallyTrue() {
    assertThat(parentAll.get(), is(true));
    assertThat(childAll.get(), is(true));
  }

  @Test
  public void parentNameInvalidatesParent() {
    parentName.setTouched(true);
    assertThat(parentAll.get(), is(false));
  }

  @Test
  public void childNameInvalidatesChildAndParent() {
    childName.setTouched(true);
    assertThat(childAll.get(), is(false));
    assertThat(parentAll.get(), is(false));
    assertThat(parentAll.isTouched(), is(true));
    assertThat(parentName.isTouched(), is(false));
  }

  @Test
  public void childNameInvalidThenValidValidatesBothChildAndParent() {
    childName.set(null);
    assertThat(childAll.get(), is(false));
    assertThat(parentAll.get(), is(false));
    childName.set("asdf");
    assertThat(childAll.get(), is(true));
    assertThat(parentAll.get(), is(true));
    assertThat(parentName.isTouched(), is(false));
  }

  @Test
  public void touchingParentAllTouchesAll() {
    parentAll.setTouched(true);
    assertThat(parentName.isTouched(), is(true));
    assertThat(childAll.isTouched(), is(true));
    assertThat(childName.isTouched(), is(true));
  }

  @Test
  public void setTouchedFalseDoesNotBreakNesting() {
    childName.setTouched(true);
    childAll.setTouched(false);
    assertThat(childName.isTouched(), is(false));
    childName.setTouched(true);
    assertThat(childAll.get(), is(false));
    assertThat(parentAll.get(), is(false));
  }

}
