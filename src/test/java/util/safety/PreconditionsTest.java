package util.safety;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Juyas
 * @version 19.07.2021
 * @since 19.07.2021
 */
public class PreconditionsTest {

  @Test
  public void nonNull() {
    String object = "Any String, test";
    Assertions.assertEquals(object, Preconditions.nonNull(object));
  }

  @Test
  public void isNull() {
    Assertions.assertThrows(NullPointerException.class, () -> {
      Preconditions.nonNull(null);
    });
  }

  @Test
  public void isNull2() {
    String name = "obj";
    try {
      Preconditions.nonNull(name, null);
    } catch (NullPointerException e) {
      Assertions.assertEquals(name + " is null", e.getMessage());
    }
  }

}