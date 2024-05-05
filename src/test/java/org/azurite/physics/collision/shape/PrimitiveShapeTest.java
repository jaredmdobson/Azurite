package org.azurite.physics.collision.shape;

import org.azurite.util.MathUtils;
import org.joml.Vector2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class PrimitiveShapeTest {

  final float deltaAcceptance = 0.001f;
  PrimitiveShape shape1;
  PrimitiveShape shape2;
  PrimitiveShape shape3;

  @BeforeEach
  public void setUp() {
    //a square
    shape1 = new Quadrilateral(new Vector2f(0, 0), new Vector2f(2, 0), new Vector2f(2, 2), new Vector2f(0, 2));
    //a triangle with equal sides
    shape2 = new Triangle(new Vector2f(-3, 0), new Vector2f(0, 3), new Vector2f(3, 0));
    //a polygon with 5 edges
    shape3 = new ConvexPolygon(new Vector2f(-3, 2), new Vector2f(0, 0), new Vector2f(5, 2), new Vector2f(3, 6), new Vector2f(-1, 5));
    shape1.setPosition(0, 0);
    shape2.setPosition(0, 0);
    shape3.setPosition(0, 0);
  }

  @Test
  public void rotationError() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      //if rotation around point is chosen, there has to be a point
      shape1.rotateShape(MathUtils.radian(90), RotationType.AROUND_POINT, null);
    });
  }

  @Test
  public void rotationError2() {
    //the type of rotation has to be specified
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      shape1.rotateShape(MathUtils.radian(90), null, null);
    });
  }

  @Test
  public void rotation() {

    for (int i = 1; i < 360; i++) {
      Vector2f[] absolutes = shape1.absolutes;
      shape1.rotateShape(MathUtils.radian(i), RotationType.AROUND_CENTER, null);
      shape1.rotateShape(MathUtils.radian(-i), RotationType.AROUND_CENTER, null);
      Assertions.assertArrayEquals(absolutes, shape1.absolutes);

      absolutes = shape2.absolutes;
      shape2.rotateShape(MathUtils.radian(i), RotationType.AROUND_CENTER, null);
      shape2.rotateShape(MathUtils.radian(-i), RotationType.AROUND_CENTER, null);
      Assertions.assertArrayEquals(absolutes, shape2.absolutes);

      absolutes = shape3.absolutes;
      shape3.rotateShape(MathUtils.radian(i), RotationType.AROUND_CENTER, null);
      shape3.rotateShape(MathUtils.radian(-i), RotationType.AROUND_CENTER, null);
      Assertions.assertArrayEquals(absolutes, shape3.absolutes);
    }

    for (int i = 1; i < 360; i++) {
      Vector2f[] absolutes = shape1.absolutes;
      shape1.rotateShape(MathUtils.radian(i), RotationType.AROUND_ORIGIN, null);
      shape1.rotateShape(MathUtils.radian(-i), RotationType.AROUND_ORIGIN, null);
      Assertions.assertArrayEquals(absolutes, shape1.absolutes);

      absolutes = shape2.absolutes;
      shape2.rotateShape(MathUtils.radian(i), RotationType.AROUND_ORIGIN, null);
      shape2.rotateShape(MathUtils.radian(-i), RotationType.AROUND_ORIGIN, null);
      Assertions.assertArrayEquals(absolutes, shape2.absolutes);

      absolutes = shape3.absolutes;
      shape3.rotateShape(MathUtils.radian(i), RotationType.AROUND_ORIGIN, null);
      shape3.rotateShape(MathUtils.radian(-i), RotationType.AROUND_ORIGIN, null);
      Assertions.assertArrayEquals(absolutes, shape3.absolutes);
    }

    //any artificial point
    Vector2f point = new Vector2f(8, 8);

    for (int i = 1; i < 360; i++) {
      Vector2f[] absolutes = shape1.absolutes;
      shape1.rotateShape(MathUtils.radian(i), RotationType.AROUND_POINT, point);
      shape1.rotateShape(MathUtils.radian(-i), RotationType.AROUND_POINT, point);
      Assertions.assertArrayEquals(absolutes, shape1.absolutes);

      absolutes = shape2.absolutes;
      shape2.rotateShape(MathUtils.radian(i), RotationType.AROUND_POINT, point);
      shape2.rotateShape(MathUtils.radian(-i), RotationType.AROUND_POINT, point);
      Assertions.assertArrayEquals(absolutes, shape2.absolutes);

      absolutes = shape3.absolutes;
      shape3.rotateShape(MathUtils.radian(i), RotationType.AROUND_POINT, point);
      shape3.rotateShape(MathUtils.radian(-i), RotationType.AROUND_POINT, point);
      Assertions.assertArrayEquals(absolutes, shape3.absolutes);
    }

  }

  @Test
  public void setPosition() {
    Vector2f pos = new Vector2f(5, -2);
    shape1.setPosition(pos);
    shape2.setPosition(pos);
    shape3.setPosition(pos);
    for (int i = 0; i < shape1.vertices(); i++) {
      Assertions.assertEquals(shape1.relatives[i].add(pos, new Vector2f()), shape1.getAbsolutePoints()[i]);
    }
    for (int i = 0; i < shape2.vertices(); i++) {
      Assertions.assertEquals(shape2.relatives[i].add(pos, new Vector2f()), shape2.getAbsolutePoints()[i]);
    }
    for (int i = 0; i < shape3.vertices(); i++) {
      Assertions.assertEquals(shape3.relatives[i].add(pos, new Vector2f()), shape3.getAbsolutePoints()[i]);
    }
    shape1.setPosition(0, 0);
    shape2.setPosition(0, 0);
    shape3.setPosition(0, 0);
  }

  @Test
  public void position() {
    Assertions.assertEquals(new Vector2f(0, 0), shape1.position());
    Assertions.assertEquals(new Vector2f(0, 0), shape2.position());
    Assertions.assertEquals(new Vector2f(0, 0), shape3.position());
  }

  @Test
  public void faces() {
    Assertions.assertEquals(4, shape1.faces().length);
    Assertions.assertEquals(3, shape2.faces().length);
    Assertions.assertEquals(5, shape3.faces().length);
  }

  @Test
  public void vertices() {
    Assertions.assertEquals(4, shape1.vertices());
    Assertions.assertEquals(3, shape2.vertices());
    Assertions.assertEquals(5, shape3.vertices());
  }

  @Test
  public void centroid() {
    Vector2f centroid1 = shape1.centroid();
    Vector2f centroid2 = shape2.centroid();
    Vector2f centroid3 = shape3.centroid();
    Assertions.assertEquals(1, centroid1.x, deltaAcceptance);
    Assertions.assertEquals(1, centroid1.y, deltaAcceptance);
    Assertions.assertEquals(0, centroid2.x, deltaAcceptance);
    Assertions.assertEquals(1, centroid2.y, deltaAcceptance);
    Assertions.assertEquals(1.046f, centroid3.x, deltaAcceptance);
    Assertions.assertEquals(2.954f, centroid3.y, deltaAcceptance);
  }

  @Test
  public void boundingSphere() {
    Circle circle1 = shape1.boundingSphere();
    Circle circle2 = shape2.boundingSphere();
    Circle circle3 = shape3.boundingSphere();

    Vector2f centroid1 = shape1.centroid();
    Vector2f max1 = null;
    for (Vector2f v : shape1.getAbsolutePoints()) {
      if (max1 == null || max1.distanceSquared(centroid1) < v.distanceSquared(centroid1))
        max1 = v;
    }

    Vector2f centroid2 = shape2.centroid();
    Vector2f max2 = null;
    for (Vector2f v : shape2.getAbsolutePoints()) {
      if (max2 == null || max2.distanceSquared(centroid2) < v.distanceSquared(centroid2))
        max2 = v;
    }

    Vector2f centroid3 = shape3.centroid();
    Vector2f max3 = null;
    for (Vector2f v : shape3.getAbsolutePoints()) {
      if (max3 == null || max3.distanceSquared(centroid3) < v.distanceSquared(centroid3))
        max3 = v;
    }

    //TODO insert circle radius checks here

    Assertions.assertEquals(circle1.centroid(), centroid1);
    Assertions.assertEquals(circle2.centroid(), centroid2);
    Assertions.assertEquals(circle3.centroid(), centroid3);
  }

  @Test
  public void supportPoint() {
    Assertions.assertEquals(new Vector2f(0, 0), shape1.supportPoint(new Vector2f(-1, -1)));
    Assertions.assertEquals(new Vector2f(2, 2), shape1.supportPoint(new Vector2f(1, 1)));
    Assertions.assertEquals(new Vector2f(2, 0), shape1.supportPoint(new Vector2f(1, 0)));
    Assertions.assertEquals(new Vector2f(0, 2), shape1.supportPoint(new Vector2f(-1, 1)));

    Assertions.assertEquals(new Vector2f(-3, 0), shape2.supportPoint(new Vector2f(-1, 0)));
    Assertions.assertEquals(new Vector2f(0, 3), shape2.supportPoint(new Vector2f(0, 1)));
    Assertions.assertEquals(new Vector2f(3, 0), shape2.supportPoint(new Vector2f(1, 0)));

    Assertions.assertEquals(new Vector2f(-3, 2), shape3.supportPoint(new Vector2f(-1, 0)));
    Assertions.assertEquals(new Vector2f(0, 0), shape3.supportPoint(new Vector2f(0, -1)));
    Assertions.assertEquals(new Vector2f(5, 2), shape3.supportPoint(new Vector2f(1, 0)));
    Assertions.assertEquals(new Vector2f(3, 6), shape3.supportPoint(new Vector2f(0, 1)));
    Assertions.assertEquals(new Vector2f(-1, 5), shape3.supportPoint(new Vector2f(-1, 1)));
  }

  @Test
  public void type() {
    Assertions.assertEquals(ShapeType.QUADRILATERAL, shape1.type());
    Assertions.assertEquals(ShapeType.TRIANGLE, shape2.type());
    Assertions.assertEquals(ShapeType.POLYGON, shape3.type());
  }

}