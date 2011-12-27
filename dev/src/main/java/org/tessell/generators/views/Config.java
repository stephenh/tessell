package org.tessell.generators.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.tessell.widgets.*;
import org.tessell.widgets.datepicker.GwtDatePicker;
import org.tessell.widgets.datepicker.IsDatePicker;
import org.tessell.widgets.datepicker.StubDatePicker;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.ValueListBox;
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
    String stubType = typeToStub.get(type);
    if (stubType == null) {
      throw new RuntimeException("No stub for " + type);
    }
    return stubType;
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
    map(Button.class, IsButton.class, GwtButton.class, StubButton.class);
    map(DockLayoutPanel.class, IsDockLayoutPanel.class, GwtDockLayoutPanel.class, StubDockLayoutPanel.class);
    map(CellPanel.class, IsCellPanel.class, GwtCellPanel.class, StubCellPanel.class);
    map(HorizontalPanel.class, IsHorizontalPanel.class, GwtHorizontalPanel.class, StubHorizontalPanel.class);
    map(FormPanel.class, IsFormPanel.class, GwtFormPanel.class, StubFormPanel.class);
    map(FileUpload.class, IsFileUpload.class, GwtFileUpload.class, StubFileUpload.class);

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
