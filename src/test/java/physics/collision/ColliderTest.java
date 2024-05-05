package physics.collision;

import ecs.PolygonCollider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physics.collision.shape.ShapeType;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class ColliderTest {

  PolygonCollider rigidBody;
  PolygonCollider staticCollider;

  @BeforeEach
  public void setUp() throws Exception {
    rigidBody = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100));
    rigidBody.setLayer(0, true);
    rigidBody.setMask(5, true);
    staticCollider = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 10, 10));
    staticCollider.setLayer(5, true);
  }

  @Test
  public void getCollisionShape() {
    Assertions.assertEquals(ShapeType.QUADRILATERAL, rigidBody.getShape().type());
    Assertions.assertEquals(ShapeType.QUADRILATERAL, staticCollider.getShape().type());
  }

  @Test
  public void canCollideWith() {
    Assertions.assertTrue(rigidBody.canCollideWith(staticCollider));
    Assertions.assertTrue(staticCollider.canCollideWith(rigidBody));
  }

  @Test
  public void layers() {
    Assertions.assertEquals(0b100000000000000, rigidBody.layers());
    Assertions.assertEquals(0b000001000000000, staticCollider.layers());
  }

  @Test
  public void mask() {
    Assertions.assertEquals(0b000001000000000, rigidBody.mask());
    Assertions.assertEquals(0b000000000000000, staticCollider.mask());
  }

  @Test
  public void hasMask() {
    for (int i = 0; i < 14; i++) {
      Assertions.assertEquals(i == 5, rigidBody.hasMask(i));
      Assertions.assertFalse(staticCollider.hasMask(i));
    }
  }

  @Test
  public void isOnLayer() {
    for (int i = 0; i < 14; i++) {
      Assertions.assertEquals(i == 0, rigidBody.isOnLayer(i));
      Assertions.assertEquals(i == 5, staticCollider.isOnLayer(i));
    }
  }

  @Test
  public void setLayer() {
    Assertions.assertFalse(rigidBody.isOnLayer(6));
    rigidBody.setLayer(6, true);
    Assertions.assertTrue(rigidBody.isOnLayer(6));
    rigidBody.setLayer(6, false);
    Assertions.assertFalse(rigidBody.isOnLayer(6));

    Assertions.assertFalse(staticCollider.isOnLayer(6));
    staticCollider.setLayer(6, true);
    Assertions.assertTrue(staticCollider.isOnLayer(6));
    staticCollider.setLayer(6, false);
    Assertions.assertFalse(staticCollider.isOnLayer(6));
  }

  @Test
  public void setMask() {
    Assertions.assertFalse(rigidBody.hasMask(6));
    rigidBody.setMask(6, true);
    Assertions.assertTrue(rigidBody.hasMask(6));
    rigidBody.setMask(6, false);
    Assertions.assertFalse(rigidBody.hasMask(6));

    Assertions.assertFalse(staticCollider.hasMask(6));
    staticCollider.setMask(6, true);
    Assertions.assertTrue(staticCollider.hasMask(6));
    staticCollider.setMask(6, false);
    Assertions.assertFalse(staticCollider.hasMask(6));
  }

}