package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.setValue;
import static org.tessell.model.properties.NewProperty.stringProperty;

import java.util.ArrayList;

import org.bindgen.Bindable;
import org.junit.Test;
import org.tessell.model.AbstractModel;
import org.tessell.model.Dto;
import org.tessell.model.properties.DtoListProperty;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.Valid;
import org.tessell.model.values.Value;
import org.tessell.tests.model.properties.dtoListPropertyTest.DBinding;

public class DtoListPropertyTest {

  final Value<ArrayList<D>> dtos = setValue("dtos", new ArrayList<D>());
  final Value<ArrayList<M>> models = setValue("models", new ArrayList<M>());

  @Test
  public void dtosToModels() {
    dtos.get().add(new D("d1"));
    // models is initially empty
    DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos);
    assertThat(l.get().size(), is(1));
  }

  @Test
  public void modelsToDtos() {
    DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos);
    l.add(new M(new D("d1")));
    assertThat(dtos.get().size(), is(1));
    assertThat(dtos.get().get(0).name, is("d1"));
  }

  @Test
  public void allValidRule() {
    dtos.get().add(new D(null));
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos).reqAllValid();
    assertThat(l.touch(), is(Valid.NO));
  }

  @Test
  public void allValidRuleTouchesAllEEntries() {
    dtos.get().add(new D(null));
    dtos.get().add(new D(null));
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos).reqAllValid();
    assertThat(l.touch(), is(Valid.NO));
    assertThat(models.get().get(0).name.isTouched(), is(true));
    assertThat(models.get().get(1).name.isTouched(), is(true));
  }

  @Test
  public void addTouchesTheList() {
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos).reqAllValid();
    l.add(new M(null));
    assertThat(l.isTouched(), is(true));
  }

  @Test
  public void addWithoutTouchingTheList() {
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos).reqAllValid();
    l.add(new M(null), false);
    assertThat(l.isTouched(), is(false));
  }

  @Test
  public void addWithoutTouchingDoesNotValidateAll() {
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos).reqAllValid();
    l.add(new M(null), false);
    assertThat(l.get().get(0).name.isTouched(), is(false));
  }

  @Test
  public void removeTouchesTheList() {
    dtos.get().add(new D(null));
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, dtos);
    assertThat(l.isTouched(), is(false));

    l.remove(l.get().get(0));
    assertThat(l.isTouched(), is(true));
  }

  @Test
  public void nullInitialDtosValue() {
    final Value<ArrayList<D>> nullDtos = setValue("dtos", null);
    final DtoListProperty<M, D> l = new DtoListProperty<M, D>(models, nullDtos);
    assertThat(l.get().size(), is(0));

    // changing the value automatically gets picked up on the next get
    nullDtos.set(new ArrayList<D>());
    nullDtos.get().add(new D("d1"));
    assertThat(l.get().size(), is(1));
  }

  public static class M extends AbstractModel<D> {
    private final DBinding b = new DBinding();
    public final StringProperty name = stringProperty(b.name()).req().in(all);

    public M(D dto) {
      b.set(dto);
    }

    @Override
    public void merge(D dto) {
    }

    @Override
    public D getDto() {
      return b.get();
    }
  }

  @Bindable
  public static class D implements Dto<M> {
    public String name;

    public D(String name) {
      this.name = name;
    }

    @Override
    public M toModel() {
      return new M(this);
    }
  }

}
