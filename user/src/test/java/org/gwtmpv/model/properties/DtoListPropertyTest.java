package org.gwtmpv.model.properties;

import static org.gwtmpv.model.properties.NewProperty.setValue;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;

import org.bindgen.Bindable;
import org.gwtmpv.model.AbstractModel;
import org.gwtmpv.model.Dto;
import org.gwtmpv.model.properties.dtoListPropertyTest.DBinding;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.values.Value;
import org.junit.Test;

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
