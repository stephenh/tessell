package org.tessell.gwt.user.client.ui;

import java.util.HashMap;
import java.util.Map;

public class StubKeyCodeMapping {

  private static final Map<Character, Integer> m = new HashMap<Character, Integer>();

  public static int map(char c) {
    Integer i = m.get(Character.toUpperCase(c));
    return i == null ? 0 : i.intValue();
  }

  static {
    // only handles 0-9 and A-Z
    m.put('0', 48);
    m.put('1', 49);
    m.put('2', 50);
    m.put('3', 51);
    m.put('4', 52);
    m.put('5', 53);
    m.put('6', 54);
    m.put('7', 55);
    m.put('8', 56);
    m.put('9', 57);
    m.put('A', 65);
    m.put('B', 66);
    m.put('C', 67);
    m.put('D', 68);
    m.put('E', 69);
    m.put('F', 70);
    m.put('G', 71);
    m.put('H', 72);
    m.put('I', 73);
    m.put('J', 74);
    m.put('K', 75);
    m.put('L', 76);
    m.put('M', 77);
    m.put('N', 78);
    m.put('O', 79);
    m.put('P', 80);
    m.put('Q', 81);
    m.put('R', 82);
    m.put('S', 83);
    m.put('T', 84);
    m.put('U', 85);
    m.put('V', 86);
    m.put('W', 87);
    m.put('X', 88);
    m.put('Y', 89);
    m.put('Z', 90);
  }

}
