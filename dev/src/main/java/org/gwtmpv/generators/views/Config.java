package org.gwtmpv.generators.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.gwtmpv.widgets.ColumnsPanel;
import org.gwtmpv.widgets.GwtAnchor;
import org.gwtmpv.widgets.GwtCellTable;
import org.gwtmpv.widgets.GwtCheckBox;
import org.gwtmpv.widgets.GwtElement;
import org.gwtmpv.widgets.GwtFadingDialogBox;
import org.gwtmpv.widgets.GwtFlowPanel;
import org.gwtmpv.widgets.GwtFocusPanel;
import org.gwtmpv.widgets.GwtFrame;
import org.gwtmpv.widgets.GwtHTML;
import org.gwtmpv.widgets.GwtHTMLPanel;
import org.gwtmpv.widgets.GwtHyperlink;
import org.gwtmpv.widgets.GwtImage;
import org.gwtmpv.widgets.GwtInlineHyperlink;
import org.gwtmpv.widgets.GwtInlineLabel;
import org.gwtmpv.widgets.GwtLabel;
import org.gwtmpv.widgets.GwtListBox;
import org.gwtmpv.widgets.GwtPasswordTextBox;
import org.gwtmpv.widgets.GwtPopupPanel;
import org.gwtmpv.widgets.GwtRadioButton;
import org.gwtmpv.widgets.GwtScrollPanel;
import org.gwtmpv.widgets.GwtSimplePanel;
import org.gwtmpv.widgets.GwtSubmitButton;
import org.gwtmpv.widgets.GwtTextArea;
import org.gwtmpv.widgets.GwtTextBox;
import org.gwtmpv.widgets.IsAnchor;
import org.gwtmpv.widgets.IsCellTable;
import org.gwtmpv.widgets.IsCheckBox;
import org.gwtmpv.widgets.IsColumnsPanel;
import org.gwtmpv.widgets.IsElement;
import org.gwtmpv.widgets.IsFadingDialogBox;
import org.gwtmpv.widgets.IsFlowPanel;
import org.gwtmpv.widgets.IsFocusPanel;
import org.gwtmpv.widgets.IsFrame;
import org.gwtmpv.widgets.IsHTML;
import org.gwtmpv.widgets.IsHTMLPanel;
import org.gwtmpv.widgets.IsHyperlink;
import org.gwtmpv.widgets.IsImage;
import org.gwtmpv.widgets.IsInlineHyperlink;
import org.gwtmpv.widgets.IsInlineLabel;
import org.gwtmpv.widgets.IsLabel;
import org.gwtmpv.widgets.IsListBox;
import org.gwtmpv.widgets.IsPasswordTextBox;
import org.gwtmpv.widgets.IsPopupPanel;
import org.gwtmpv.widgets.IsRadioButton;
import org.gwtmpv.widgets.IsRowTable;
import org.gwtmpv.widgets.IsScrollPanel;
import org.gwtmpv.widgets.IsSimplePanel;
import org.gwtmpv.widgets.IsSubmitButton;
import org.gwtmpv.widgets.IsTextArea;
import org.gwtmpv.widgets.IsTextBox;
import org.gwtmpv.widgets.IsTextList;
import org.gwtmpv.widgets.IsValueListBox;
import org.gwtmpv.widgets.RowTable;
import org.gwtmpv.widgets.StubAnchor;
import org.gwtmpv.widgets.StubCellTable;
import org.gwtmpv.widgets.StubCheckBox;
import org.gwtmpv.widgets.StubFadingDialogBox;
import org.gwtmpv.widgets.StubFlowPanel;
import org.gwtmpv.widgets.StubFocusPanel;
import org.gwtmpv.widgets.StubFrame;
import org.gwtmpv.widgets.StubHTML;
import org.gwtmpv.widgets.StubHTMLPanel;
import org.gwtmpv.widgets.StubHyperlink;
import org.gwtmpv.widgets.StubImage;
import org.gwtmpv.widgets.StubInlineHyperlink;
import org.gwtmpv.widgets.StubInlineLabel;
import org.gwtmpv.widgets.StubColumnsPanel;
import org.gwtmpv.widgets.StubElement;
import org.gwtmpv.widgets.StubLabel;
import org.gwtmpv.widgets.StubListBox;
import org.gwtmpv.widgets.StubPasswordTextBox;
import org.gwtmpv.widgets.StubPopupPanel;
import org.gwtmpv.widgets.StubRadioButton;
import org.gwtmpv.widgets.StubRowTable;
import org.gwtmpv.widgets.StubScrollPanel;
import org.gwtmpv.widgets.StubSimplePanel;
import org.gwtmpv.widgets.StubSubmitButton;
import org.gwtmpv.widgets.StubTextArea;
import org.gwtmpv.widgets.StubTextBox;
import org.gwtmpv.widgets.StubTextList;
import org.gwtmpv.widgets.StubValueListBox;
import org.gwtmpv.widgets.TextList;
import org.gwtmpv.widgets.ValueListBox;
import org.gwtmpv.widgets.datepicker.GwtDatePicker;
import org.gwtmpv.widgets.datepicker.IsDatePicker;
import org.gwtmpv.widgets.datepicker.StubDatePicker;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FadingDialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/** Holds mappings of ui.xml type -> interface type, stub type, and subclass type. */
public class Config {

  private final Map<String, String> typeToInterface = new HashMap<String, String>();
  private final Map<String, String> typeToStub = new HashMap<String, String>();
  private final Map<String, String> typeToSub = new HashMap<String, String>();

  public Config() {
    setupDefault();
    setupUsersViewGenDotProperties();
  }

  public String getInterface(final String type) {
    final String interfaceType = typeToInterface.get(type);
    if (interfaceType == null) {
      throw new RuntimeException("No interface for " + type);
    }
    return interfaceType;
  }

  public String getStub(final String type) {
    return typeToStub.get(type);
  }

  public String getSubclass(final String type) {
    return typeToSub.get(type);
  }

  private void setupDefault() {
    // gwt
    map(Element.class, IsElement.class, GwtElement.class, StubElement.class);
    map(Anchor.class, IsAnchor.class, GwtAnchor.class, StubAnchor.class);
    map(CheckBox.class, IsCheckBox.class, GwtCheckBox.class, StubCheckBox.class);
    map(TextBox.class, IsTextBox.class, GwtTextBox.class, StubTextBox.class);
    map(TextArea.class, IsTextArea.class, GwtTextArea.class, StubTextArea.class);
    map(Label.class, IsLabel.class, GwtLabel.class, StubLabel.class);
    map(SubmitButton.class, IsSubmitButton.class, GwtSubmitButton.class, StubSubmitButton.class);
    map(PasswordTextBox.class, IsPasswordTextBox.class, GwtPasswordTextBox.class, StubPasswordTextBox.class);
    map(Hyperlink.class, IsHyperlink.class, GwtHyperlink.class, StubHyperlink.class);
    map(SimplePanel.class, IsSimplePanel.class, GwtSimplePanel.class, StubSimplePanel.class);
    map(FocusPanel.class, IsFocusPanel.class, GwtFocusPanel.class, StubFocusPanel.class);
    map(FlowPanel.class, IsFlowPanel.class, GwtFlowPanel.class, StubFlowPanel.class);
    map(PopupPanel.class, IsPopupPanel.class, GwtPopupPanel.class, StubPopupPanel.class);
    map(InlineLabel.class, IsInlineLabel.class, GwtInlineLabel.class, StubInlineLabel.class);
    map(InlineHyperlink.class, IsInlineHyperlink.class, GwtInlineHyperlink.class, StubInlineHyperlink.class);
    map(Image.class, IsImage.class, GwtImage.class, StubImage.class);
    map(HTMLPanel.class, IsHTMLPanel.class, GwtHTMLPanel.class, StubHTMLPanel.class);
    map(HTML.class, IsHTML.class, GwtHTML.class, StubHTML.class);
    map(RadioButton.class, IsRadioButton.class, GwtRadioButton.class, StubRadioButton.class);
    map(ListBox.class, IsListBox.class, GwtListBox.class, StubListBox.class);
    map(ValueListBox.class, IsValueListBox.class, ValueListBox.class, StubValueListBox.class);
    map(Frame.class, IsFrame.class, GwtFrame.class, StubFrame.class);
    map(ScrollPanel.class, IsScrollPanel.class, GwtScrollPanel.class, StubScrollPanel.class);

    map(DatePicker.class, IsDatePicker.class, GwtDatePicker.class, StubDatePicker.class);
    map(CellTable.class, IsCellTable.class, GwtCellTable.class, StubCellTable.class);

    // custom
    map(RowTable.class, IsRowTable.class, RowTable.class, StubRowTable.class);
    map(TextList.class, IsTextList.class, TextList.class, StubTextList.class);
    map(ColumnsPanel.class, IsColumnsPanel.class, ColumnsPanel.class, StubColumnsPanel.class);
    map(FadingDialogBox.class, IsFadingDialogBox.class, GwtFadingDialogBox.class, StubFadingDialogBox.class);
  }

  private void setupUsersViewGenDotProperties() {
    final InputStream in = ViewGenerator.class.getResourceAsStream("/viewgen.properties");
    if (in != null) {
      final Properties p = new Properties();
      try {
        p.load(in);
      } catch (final IOException io) {
        throw new RuntimeException(io);
      }
      for (final Entry<Object, Object> e : p.entrySet()) {
        final String type = e.getKey().toString();
        final String packageName = StringUtils.substringBeforeLast(type, ".");
        final String[] parts = e.getValue().toString().split(",");
        typeToInterface.put(type, prependPackageIfNeeded(packageName, parts[0].trim()));
        typeToSub.put(type, prependPackageIfNeeded(packageName, parts[1].trim()));
        typeToStub.put(type, prependPackageIfNeeded(packageName, parts[2].trim()));
      }
    }
  }

  /** Prepends {@code packageName} if the class name {@name} has no existing package. */
  private String prependPackageIfNeeded(final String packageName, final String name) {
    if (name.contains(".")) {
      return name;
    } else {
      return packageName + "." + name;
    }
  }

  private void map(final Class<?> type, final Class<?> interfaceType, final Class<?> subType, final Class<?> stubType) {
    typeToInterface.put(type.getName(), interfaceType.getName());
    typeToSub.put(type.getName(), subType.getName());
    typeToStub.put(type.getName(), stubType.getName());
  }

}
