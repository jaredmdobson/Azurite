package org.azurite.physics.collision.shape;

import org.azurite.physics.collision.RayCastResult;
import org.joml.Vector2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class CircleTest {

  Circle small;
  Circle big;

  @BeforeEach
  public void setUp() throws Exception {
    small = new Circle(new Vector2f(0, 0), 5);
    big = new Circle(new Vector2f(0, 0), 500);
    //so that they should intersection at a small point
    small.setPosition(505, 0);
    big.setPosition(0, 0);
  }

  @Test
  public void supportPoint() {
    Assertions.assertEquals(new Vector2f(505, 5), small.supportPoint(new Vector2f(0, 1)));
    Assertions.assertEquals(new Vector2f(505, -5), small.supportPoint(new Vector2f(0, -1)));
    Assertions.assertEquals(new Vector2f(510, 0), small.supportPoint(new Vector2f(1, 0)));
    Assertions.assertEquals(new Vector2f(500, 0), small.supportPoint(new Vector2f(-1, 0)));

    Assertions.assertEquals(new Vector2f(0, 500), big.supportPoint(new Vector2f(0, 1)));
    Assertions.assertEquals(new Vector2f(0, -500), big.supportPoint(new Vector2f(0, -1)));
    Assertions.assertEquals(new Vector2f(500, 0), big.supportPoint(new Vector2f(1, 0)));
    Assertions.assertEquals(new Vector2f(-500, 0), big.supportPoint(new Vector2f(-1, 0)));
  }

  @Test
  public void intersection() {
    Assertions.assertTrue(small.intersection(big));
    Assertions.assertTrue(big.intersection(small));
    small.setPosition(505, 1);
    Assertions.assertFalse(small.intersection(big));
    Assertions.assertFalse(big.intersection(small));
    small.setPosition(505, 0);
  }

  @Test
  public void rayCast() {
    RayCastResult rayCastResult = small.rayCast(new Vector2f(515, 0), new Vector2f(-1, 0), 5f);
    RayCastResult rayCastResult2 = small.rayCast(new Vector2f(515, 0), new Vector2f(-1, 0), 4.99f);
    Assertions.assertTrue(rayCastResult.didHit());
    Assertions.assertFalse(rayCastResult2.didHit());
    Assertions.assertEquals(new Vector2f(510, 0), rayCastResult.getPoint());
    Assertions.assertEquals(5, rayCastResult.getStrikeLength(), 0.001);
  }

}