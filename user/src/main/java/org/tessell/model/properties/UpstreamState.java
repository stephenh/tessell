package org.tessell.model.properties;

import java.util.List;

import org.tessell.util.ListDiff;

public class UpstreamState {

  private final Property<?> owner;
  private List<Property<?>> lastUpstream;

  public UpstreamState(Property<?> owner) {
    this.owner = owner;
  }

  public void update(List<Property<?>> newUpstream) {
    // Only update our upstream properties if they've changed
    if (lastUpstream == null || !lastUpstream.equals(newUpstream)) {
      ListDiff<Property<?>> diff = ListDiff.of(lastUpstream, newUpstream);
      for (Property<?> removed : diff.removed) {
        removed.removeDerived(owner);
      }
      for (Property<?> added : diff.added) {
        if (added != owner) {
          added.addDerived(owner);
        }
      }
      // Remember for change tracking next time
      lastUpstream = newUpstream;
    }
  }
}
