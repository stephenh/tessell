package org.tessell.model.dsl;

import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.gwt.user.client.ui.IsImage;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.widgets.HasEnsureDebugIdSuffix;

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

  public static TakesValue<String> textOf(final IsElement target) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getInnerText();
      }

      @Override
      public void setValue(String value) {
        target.setInnerText(value);
      }
    };
  }

  public static TakesValue<String> innerTextOf(final IsWidget target) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getIsElement().getInnerText();
      }

      @Override
      public void setValue(String value) {
        target.getIsElement().setInnerText(value);
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

  public static TakesValue<String> htmlOf(final IsElement target) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getInnerHTML();
      }

      @Override
      public void setValue(String value) {
        target.setInnerHTML(value);
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

  public static TakesValue<String> debugId(final IsWidget w) {
    return new TakesValue<String>() {
      @Override
      public void setValue(String value) {
        w.ensureDebugId(value);
      }

      @Override
      public String getValue() {
        return w.getIsElement().getAttribute("id");
      }
    };
  }

  public static SetsValue<String> debugIdSuffix(final HasEnsureDebugIdSuffix w) {
    return new SetsValue<String>() {
      @Override
      public void setValue(String value) {
        w.ensureDebugIdSuffix(value);
      }
    };
  }

  public static TakesValue<String> styleOf(final HasCss target) {
    return new TakesValue<String>() {
      private String lastAddedStyle;

      @Override
      public String getValue() {
        return lastAddedStyle;
      }

      @Override
      public void setValue(String value) {
        if (lastAddedStyle != null) {
          target.removeStyleName(lastAddedStyle);
          lastAddedStyle = null;
        }
        if (value != null && !"".equals(value)) {
          target.addStyleName(value);
          lastAddedStyle = value;
        }
      }
    };
  }

  public static TakesValue<String> attributeOf(final IsElement target, final String attribute) {
    return new TakesValue<String>() {
      @Override
      public String getValue() {
        return target.getAttribute(attribute);
      }

      @Override
      public void setValue(final String value) {
        target.setAttribute(attribute, value);
      }
    };
  }

}
