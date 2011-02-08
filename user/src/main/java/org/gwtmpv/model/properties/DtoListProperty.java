package org.gwtmpv.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;

import java.util.ArrayList;

import org.gwtmpv.model.Dto;
import org.gwtmpv.model.Model;
import org.gwtmpv.model.events.ValueAddedEvent;
import org.gwtmpv.model.events.ValueAddedHandler;
import org.gwtmpv.model.events.ValueRemovedEvent;
import org.gwtmpv.model.events.ValueRemovedHandler;
import org.gwtmpv.model.validation.Valid;
import org.gwtmpv.model.validation.rules.Custom;
import org.gwtmpv.model.values.DerivedValue;
import org.gwtmpv.model.values.Value;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Provides a property of model objects that is really backed by a list of dto objects.
 *
 * Changes to this property (adding/removing models) will add/remove the corresponding dtos to the original dto list.
 * 
 * @param E the model type
 * @param F the real backing dto type
 */
public class DtoListProperty<E extends Model<F>, F extends Dto<E>> extends AbstractProperty<ArrayList<E>, DtoListProperty<E, F>> {

  /** The real dto array. */
  private final Value<ArrayList<F>> dtoList;
  private boolean hasCopiedDtos = false;

  public DtoListProperty(final Value<ArrayList<E>> modelList, final Value<ArrayList<F>> dtoList) {
    super(modelList);
    this.dtoList = dtoList;
  }

  @Override
  public ArrayList<E> get() {
    // funny dance--check dtoList field because our base class constructor
    // calls this method and the dtoList field may not have been set yet
    if (dtoList != null && !hasCopiedDtos) {
      if (dtoList.get() != null) {
        for (F dto : dtoList.get()) {
          super.get().add(dto.toModel());
        }
        // putting hasCopiedDtos here instead of outside of the null check
        // is a hack because it means we'll automatically notice the /first/
        // null->List change in our value, but no subsequent ones. So it
        // will likely be surprising to the user.
        hasCopiedDtos = true;
      }
    }
    return super.get();
  }

  /** Adds {@code item}, firing a {@link ValueAddedEvent}. */
  public void add(final E item) {
    add(item, true);
  }

  /** Adds {@code item}, firing a {@link ValueAddedEvent}. */
  public void add(final E item, boolean touch) {
    get().add(item); // model
    dtoList().add(item.getDto()); // dto
    if (touch) {
      setTouched(true);
    }
    fireEvent(new ValueAddedEvent<E>(this, item));
    reassess();
  }

  /** Removes {@code item}, firing a {@link ValueRemovedEvent}. */
  public void remove(final E item) {
    if (get().remove(item)) { // model
      dtoList().remove(item.getDto()); // dto
      setTouched(true);
      fireEvent(new ValueRemovedEvent<E>(this, item));
      reassess();
    }
  }

  /** Removes all entries, firing a {@link ValueRemovedEvent} for each. */
  public void clear() {
    final int size = get().size();
    for (int i = size - 1; i >= 0; i--) {
      final E value = get().remove(i); // model
      dtoList().remove(value.getDto()); // dto
      fireEvent(new ValueRemovedEvent<E>(this, value));
    }
    reassess();
  }

  /** Adds a rule that all models in this property must be valid. */
  public DtoListProperty<E, F> reqAllValid() {
    new Custom(this, "Some models are invalid", new DerivedValue<Boolean>() {
      public Boolean get() {
        if (!isTouched()) {
          return false;
        }
        boolean allValid = true;
        for (E model : DtoListProperty.this.get()) {
          if (model.allValid().touch() == Valid.NO) {
            allValid = false;
          }
        }
        return allValid;
      }
    });
    return this;
  }

  /** @return a derived property that reflects this list's size. */
  public IntegerProperty size() {
    return addDerived(integerProperty(new DerivedValue<Integer>() {
      public Integer get() {
        final ArrayList<E> current = DtoListProperty.this.get();
        return (current == null) ? null : current.size();
      }
    }));
  }

  /** Registers {@code handler} to be called when new values are added. */
  public HandlerRegistration addValueAddedHandler(final ValueAddedHandler<E> handler) {
    return addHandler(ValueAddedEvent.getType(), handler);
  }

  /** Registers {@code handler} to be called when values are removed. */
  public HandlerRegistration addValueRemovedHandler(final ValueRemovedHandler<E> handler) {
    return addHandler(ValueRemovedEvent.getType(), handler);
  }

  @Override
  protected DtoListProperty<E, F> getThis() {
    return this;
  }

  @Override
  protected ArrayList<E> copyLastValue(ArrayList<E> newValue) {
    if (newValue == null) {
      return null;
    }
    return new ArrayList<E>(newValue);
  }

  private ArrayList<F> dtoList() {
    return dtoList.get();
  }

}
