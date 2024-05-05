package org.azurite.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Juyas
 * @version 19.07.2021
 * @since 19.07.2021
 */
public class PairTest {

  @Test
  public void getterSetter() {
    Pair<String, String> pair = new Pair<>("l", "r");
    Assertions.assertEquals("l", pair.getLeft());
    Assertions.assertEquals("r", pair.getRight());
    pair.setLeft("1");
    pair.setRight("2");
    Assertions.assertEquals("1", pair.getLeft());
    Assertions.assertEquals("2", pair.getRight());
  }

  @Test
  public void extend() {
    Pair<String, String> pair = new Pair<>("l", "r");
    Triple<String, String, String> rr = pair.extend("rr");
    Assertions.assertEquals("l", rr.getLeft());
    Assertions.assertEquals("r", rr.getMiddle());
    Assertions.assertEquals("rr", rr.getRight());
  }

}