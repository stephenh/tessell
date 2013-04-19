package org.tessell.bootstrap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;
import static org.tessell.testing.TessellMatchers.hidden;
import static org.tessell.testing.TessellMatchers.shown;

import org.junit.Test;
import org.tessell.bootstrap.views.StubFormView;
import org.tessell.gwt.user.client.ui.StubTextBox;
import org.tessell.model.AbstractModel;
import org.tessell.model.commands.HasActive;
import org.tessell.model.commands.UiCommand;
import org.tessell.model.dsl.Binder;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.StringProperty;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;

public class FormTest extends AbstractPresenterTest {

  private final Binder b = new Binder();
  private final Form form = new Form();
  private final StubFormView v = (StubFormView) form.getView();

  @Test
  public void showsInitially() {
    assertThat(v.lines(), is(shown()));
    assertThat(v.actions(), is(shown()));
  }

  @Test
  public void hidesLinesAndActionsWhenLoading() {
    form.loading().set(true);
    assertThat(v.lines(), is(hidden()));
    assertThat(v.actions(), is(hidden()));
    assertThat(v.title(), is(shown()));
  }

  @Test
  public void reshowsLinesAndActionsWhenDoneLoading() {
    form.loading().set(true);
    form.loading().set(false);
    assertThat(v.lines(), is(shown()));
    assertThat(v.actions(), is(shown()));
  }

  @Test
  public void enterKeySkipsNonPrimaryButton() {
    // Given a non-primary submit button
    final Button button = new Button();
    form.add(button);
    final DummyUiCommand ui = new DummyUiCommand();
    b.bind(ui).to(button);
    // And a text line
    final TextLine line1 = new TextLine();
    form.add(line1);
    // When the user hits enter
    ((StubTextBox) line1.getTextBox()).press((char) KeyCodes.KEY_ENTER);
    // Then we ignore it
    assertThat(ui.executes, is(0));
  }

  @Test
  public void enterKeyUsesThePrimaryButton() {
    // Given a two buttons, one of them primary
    final Button b1 = new Button();
    form.add(b1);
    final Button b2 = new Button();
    b2.setPrimary(true);
    form.add(b2);
    // And a ui command bound to the primary button
    final DummyUiCommand ui = new DummyUiCommand();
    b.bind(ui).to(b2);
    // And a text line
    final TextLine line1 = new TextLine();
    form.add(line1);
    // When the user hits enter
    ((StubTextBox) line1.getTextBox()).press((char) KeyCodes.KEY_ENTER);
    // Then we fired the UiCommand
    assertThat(ui.executes, is(1));
  }

  @Test
  public void autoFocusFirstLineOnAttach() {
    // Given two lines
    final TextLine line1 = new TextLine();
    final TextLine line2 = new TextLine();
    form.add(line1);
    form.add(line2);
    // When the form is attached
    AttachEvent.fire(form, true);
    // Then the line1 is focused
    assertThat(((StubTextBox) line1.getTextBox()).isFocused(), is(true));
    // And line2 is not
    assertThat(((StubTextBox) line2.getTextBox()).isFocused(), is(false));
  }

  @Test
  public void autoFocusAfterLoaded() {
    // Given two lines
    final TextLine line1 = new TextLine();
    final TextLine line2 = new TextLine();
    form.add(line1);
    form.add(line2);
    // When the form is loaded
    form.loading().set(true);
    form.loading().set(false);
    // Then the line1 is focused
    assertThat(((StubTextBox) line1.getTextBox()).isFocused(), is(true));
    // And line2 is not
    assertThat(((StubTextBox) line2.getTextBox()).isFocused(), is(false));
  }

  @Test
  public void submitCancelledIfFormNotValid() {
    // Given a required line
    final StringProperty p1 = stringProperty("p1").req();
    final TextLine line1 = new TextLine();
    form.add(line1);
    line1.bind(p1);
    // And a submit button
    final DummyUiCommand buttonCommand = new DummyUiCommand();
    final Button button1 = new Button();
    form.add(button1);
    button1.bind(buttonCommand);
    // When the user clicks submit
    button1.getButton().click();
    // Then the property is touched
    assertThat(p1.isTouched(), is(true));
    // And the form is invalid
    assertThat(line1.valid().get(), is(false));
    assertThat(form.valid().get(), is(false));
    // And the execute didn't happen
    assertThat(buttonCommand.executes, is(0));

    // When the user corrects the problem
    ((StubTextBox) line1.getTextBox()).type("foo");
    // Then the form is no longer invalid
    assertThat(form.valid().get(), is(true));
    // And can be submitted now
    button1.getButton().click();
    assertThat(buttonCommand.executes, is(1));
  }

  @Test
  public void submitCancelledIfModelNotValid() {
    // Given a model
    final DummyModel m = new DummyModel();
    form.watch(m);
    // And a submit button
    final DummyUiCommand buttonCommand = new DummyUiCommand();
    final Button button1 = new Button();
    form.add(button1);
    button1.bind(buttonCommand);
    // When the user clicks submit
    button1.getButton().click();
    // Then the property is touched
    assertThat(m.name.isTouched(), is(true));
    // And the form is invalid
    assertThat(form.valid().get(), is(false));
    // And the execute didn't happen
    assertThat(buttonCommand.executes, is(0));
  }

  @Test
  public void buttonsAreDisabledDuringSubmit() {
    // Given a submit button
    final DummyUiCommand buttonCommand = new DummyUiCommand();
    final Button button1 = new Button();
    form.add(button1);
    button1.bind(buttonCommand);
    // And a cancel button
    final Button button2 = new Button();
    form.add(button2);
    // When the user clicks submit
    button1.getButton().click();
    // Then the buttons are disabled
    assertThat(button1.getButton().isEnabled(), is(false));
    assertThat(button2.getButton().isEnabled(), is(false));
    // And when the AJAX call is done
    buttonCommand.done();
    // Then the buttons are re-enabled
    assertThat(button1.getButton().isEnabled(), is(true));
    assertThat(button2.getButton().isEnabled(), is(true));
  }

  @Test
  public void linesAreDisabledDuringSubmit() {
    // Given a text line
    final TextLine line1 = new TextLine();
    form.add(line1);
    // And a submit button
    final DummyUiCommand buttonCommand = new DummyUiCommand();
    final Button button1 = new Button();
    form.add(button1);
    button1.bind(buttonCommand);
    // When the user clicks submit
    button1.getButton().click();
    // Then the text field is disabled
    assertThat(line1.getTextBox().isEnabled(), is(false));
    // And when the AJAX call is done
    buttonCommand.done();
    // Then the buttons are re-enabled
    assertThat(line1.getTextBox().isEnabled(), is(true));
  }

  private static class DummyUiCommand extends UiCommand implements HasActive {
    private int executes;
    private final BooleanProperty active = booleanProperty("active");

    @Override
    protected void doExecute() {
      executes++;
      active.set(true);
    }

    @Override
    public Property<Boolean> active() {
      return active;
    }

    private void done() {
      active.set(false);
    }
  }

  private static class DummyModel extends AbstractModel {
    public final StringProperty name = add(stringProperty("name")).req();
  }
}
