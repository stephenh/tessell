package org.tessell.gwt.user.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.tessell.widgets.StubWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class StubSuggestBox extends StubWidget implements IsSuggestBox {

  private String text;
  private int limit;
  private final SuggestOracle oracle;
  private final SuggestionDisplay display;
  private final List<Suggestion> lastSuggestions = new ArrayList<Suggestion>();

  public StubSuggestBox() {
    this(new MultiWordSuggestOracle());
  }

  public StubSuggestBox(SuggestOracle oracle) {
    this(oracle, null);
  }

  public StubSuggestBox(SuggestOracle oracle, SuggestionDisplay display) {
    this.oracle = oracle;
    this.display = display;
  }

  /** The user types some text, but focus doesn't leave the box. */
  public void typeSome(String query) {
    if (query == null || "".equals(query)) {
      oracle.requestDefaultSuggestions(new Request(null, limit), new Callback() {
        public void onSuggestionsReady(Request request, Response response) {
          lastSuggestions.clear();
          lastSuggestions.addAll(response.getSuggestions());
        }
      });
    } else {
      oracle.requestSuggestions(new Request(query, limit), new Callback() {
        public void onSuggestionsReady(Request request, Response response) {
          lastSuggestions.clear();
          lastSuggestions.addAll(response.getSuggestions());
        }
      });
    }
  }

  /** The user types some text and focus leaves the box. */
  public void type(final String value) {
    typeSome(value);
    setValue(value, true);
  }

  public void select(String text) {
    for (Suggestion s : lastSuggestions) {
      if (s.getDisplayString().equals(text)) {
        SelectionEvent.fire(this, s);
        setValue(s.getReplacementString(), true);
        return;
      }
    }
    throw new IllegalArgumentException("Suggestion " + text + " wasn't found");
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean isAnimationEnabled() {
    return false;
  }

  @Override
  public void setAnimationEnabled(boolean enable) {
  }

  @Override
  public String getValue() {
    return text;
  }

  @Override
  public void setValue(String value) {
    setValue(value, false);
  }

  @Override
  public void setValue(String value, boolean fireEvents) {
    text = value == null ? "" : value;
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    return handlers.addHandler(KeyUpEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
    return handlers.addHandler(KeyDownEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
    return handlers.addHandler(KeyPressEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<Suggestion> handler) {
    return handlers.addHandler(SelectionEvent.getType(), handler);
  }

  @Override
  public SuggestionDisplay getSuggestionDisplay() {
    return display;
  }

  @Override
  public SuggestOracle getSuggestOracle() {
    return oracle;
  }

  @Override
  public void setAutoSelectEnabled(boolean selectsFirstItem) {
  }

  @Override
  public void setLimit(int limit) {
    this.limit = limit;
  }

  @Override
  public int getLimit() {
    return limit;
  }

  @Override
  public void showSuggestionList() {
  }

}
