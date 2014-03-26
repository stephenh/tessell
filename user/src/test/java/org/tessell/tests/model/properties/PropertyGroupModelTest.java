package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.listProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Before;
import org.junit.Test;
import org.tessell.model.AbstractModel;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.model.properties.StringProperty;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;
import org.tessell.util.PropertyUtils;

public class PropertyGroupModelTest extends AbstractRuleTest {

  private final PropertyGroup all = new PropertyGroup("all", "parent invalid");
  private final ListProperty<TestModel> models = listProperty("models");
  private final TestModel m1 = new TestModel();
  private final TestModel m2 = new TestModel();

  @Before
  public void setup() {
    PropertyUtils.syncModelsToGroup(all, models);
    models.add(m1);
    models.add(m2);
  }

  @Test
  public void childNameInvalidatesChildAndParent() {
    m1.name.touch();
    // m1 is invalid
    assertThat(m1.allValid().get(), is(false));
    // so the group is
    assertThat(all.get(), is(false));
    assertThat(all.isTouched(), is(true));
    // but m2 is still okay
    assertThat(m2.allValid().get(), is(true));
    assertThat(m2.name.isTouched(), is(false));
  }

  @Test
  public void touchingParentAllTouchesAll() {
    all.setTouched(true);
    assertThat(m1.name.isTouched(), is(true));
    assertThat(m2.name.isTouched(), is(true));
  }

  @Test
  public void setTouchedFalseDoesNotBreakNesting() {
    // name touched, invalid
    m1.name.setTouched(true);
    // but untouching all, untouches the children resets us to valid
    all.setTouched(false);
    assertThat(m1.name.isTouched(), is(false));
    assertThat(all.get(), is(true));
    // name retouched, invalid
    m1.name.setTouched(true);
    assertThat(all.get(), is(false));
  }

  @Test
  public void removingModelFromListRemovesInvalidates() {
    // start out invalid
    m1.name.setTouched(true);
    assertThat(all.get(), is(false));
    // but when m1 is removed
    models.remove(m1);
    // we're back to valid
    assertThat(all.get(), is(true));
  }

  @Test
  public void syncAddsExistingModelsToGroup() {
    PropertyGroup all = new PropertyGroup("all", "parent invalid");
    ListProperty<TestModel> models = listProperty("models");
    models.add(m1);
    PropertyUtils.syncModelsToGroup(all, models);
    assertThat(all.getProperties().size(), is(1));
  }

  @Test
  public void shouldNotFailWhenTheModelListIsNull() {
    PropertyGroup all = new PropertyGroup("all", "parent invalid");
    ListProperty<TestModel> models = listProperty("models", null);
    PropertyUtils.syncModelsToGroup(all, models);
  }

  private static class TestModel extends AbstractModel {
    public final StringProperty name = add(stringProperty("name")).req();
  }

}
