package org.azurite.physics.collision.shape;

import org.joml.Vector2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class LineTest {

  @Test
  public void generalizedTest() {
    Line line = new Line(new Vector2f(0, 0), new Vector2f(1, 1));
    Assertions.assertEquals(ShapeType.LINE, line.type());
    Assertions.assertEquals(2, line.vertices());
    //the line has technically 2 faces
    Assertions.assertEquals(2, line.faces().length);
  }

  @Test
  public void supportPoint() {
    //test random 100 different points
    //this test is technically not a deep test, but should be sufficient
    Random random = new Random(123456789);
    for (int i = 0; i < 100; i++) {
      float f1 = random.nextFloat() * 500;
      float f2 = random.nextFloat() * 500;
      float f3 = random.nextFloat() * -500;
      float f4 = random.nextFloat() * -500;
      Vector2f point = new Vector2f(f1, f2);
      Vector2f point2 = new Vector2f(f3, f4);
      Line l = new Line(point, point2);
      l.setPosition(0, 0);
      //point2 is ensured to be negative, point1 is ensured to be positive
      Assertions.assertEquals(point, l.supportPoint(new Vector2f(1, 0)));
      Assertions.assertEquals(point, l.supportPoint(new Vector2f(0, 1)));
      Assertions.assertEquals(point2, l.supportPoint(new Vector2f(0, -1)));
      Assertions.assertEquals(point2, l.supportPoint(new Vector2f(-1, 0)));
    }
  }

}