package org.tessell.widgets;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * A marker interface for stub widgets.
 *
 * This is a hack, but allows slipping IsWidget components into UiBinder 
 * files.
 *
 * If you have a construct:
 * 
 * <my:Foo>
 *   <gwt:FlowPanel />
 * </my:Foo>
 *
 * UiBinder really wants your {@code Foo} to implement {@link HasWidgets}. However,
 * if you implement {@code add(Widget)} for the GWT view, you'll also need a
 * {@code add(IsWidget)} for the stub view. Which means when GWT compiles the
 * ui.xml file, the Tessell-ized add(GwtFlowPanel) call will match both methods
 * (because it is both a Widget and IsWidget) and fail the compile.
 *
 * Our hack is to only have add(Widget) and add(IsStubWidget), which will not
 * have overlapping inheritance hierarchies. This allows the two add methods
 * to be disambiguous and for things to (surprisingly) just work.
 *
 * Oh, and we use the marker interface because all {@code StubXxx} types
 * are excluded from GWT compiles. 
 */
public interface IsStubWidget {
}
