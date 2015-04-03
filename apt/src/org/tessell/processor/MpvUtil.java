package org.tessell.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.VariableElement;

import org.exigencecorp.aptutil.Prop;

public class MpvUtil {

  public static List<Prop> toProperties(Collection<VariableElement> fields) {
    List<Prop> props = new ArrayList<Prop>();
    for (VariableElement f : fields) {
      props.add(new Prop(f.getSimpleName().toString(), f.asType().toString()));
    }
    return props;
  }

}
