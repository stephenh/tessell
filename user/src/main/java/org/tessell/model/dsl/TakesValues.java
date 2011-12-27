package org.tessell.model.dsl;

import org.tessell.widgets.IsImage;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;

/**
 * Utility methods to create {@link TakesValue} wrappers around common properties of widgets.
 *
 * Tangentially, I think this is reimplementing editors, sans magic, and sans cool features like nesting.
 */
public class TakesValues {

  public static TakesValue<String> textOf(final HasText target) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getText();
      }

      @Override
      public void setValue(String value) {
        target.setText(value);
      }
    };
  }

  public static TakesValue<String> htmlOf(final HasHTML target) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getHTML();
      }

      @Override
      public void setValue(String value) {
        target.setHTML(value);
      }
    };
  }

  public static TakesValue<String> urlOf(final IsImage target) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getUrl();
      }

      @Override
      public void setValue(String value) {
        target.setUrl(value);
      }
    };
  }

}
