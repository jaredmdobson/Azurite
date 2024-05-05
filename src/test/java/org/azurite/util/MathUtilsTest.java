package org.azurite.util;

import org.azurite.physics.collision.shape.Circle;
import org.azurite.physics.collision.shape.ConvexPolygon;
import org.azurite.physics.collision.shape.Point;
import org.joml.Random;
import org.joml.Vector2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * @author Juyas
 * @version 16.07.2021
 * @since 16.07.2021
 */
public class MathUtilsTest {

  final float minimalDelta = Float.MIN_VALUE;
  final float smallDelta = 0.0001f;

  @Test
  public void radian() {
    for (int i = 0; i <= 360; i++) {
      Assertions.assertEquals(Math.toRadians(i), MathUtils.radian(i), smallDelta);
    }
  }

  @Test
  public void rotateAroundOrigin() {
    for (int i = 0; i < 100; i++) {
      Vector2f point = new Vector2f((float) Math.random() * 10 - 5, (float) Math.random() * 10 - 5);
      //a circle with the radius as the distance from the origin to the point, where the origin of both is 0,0
      Circle circle = new Circle(new Vector2f(0, 0), point.length());
      circle.setPosition(0, 0);
      Assertions.assertTrue(circle.supportPoint(point).equals(point, smallDelta));
      //rotate 360 different angles, should be sufficient
      for (int a = 1; i <= 360; i++) {
        Vector2f p = MathUtils.rotateAroundOrigin(point, MathUtils.radian(a));
        Assertions.assertTrue(circle.supportPoint(p).equals(p, smallDelta));
      }
    }
  }

  @Test
  public void rotateAroundPoint() {
    for (int i = 0; i < 100; i++) {
      Vector2f pointToRotate = new Vector2f((float) Math.random() * 10 - 5, (float) Math.random() * 10 - 5);
      Vector2f pointToRotateAround = new Vector2f((float) Math.random() * 20 - 10, (float) Math.random() * 20 - 10);
      Vector2f diff = pointToRotate.sub(pointToRotateAround, new Vector2f());
      //a circle with the radius as the distance from the origin to the point, where the origin of both is 0,0
      Circle circle = new Circle(new Vector2f(0, 0), diff.length());
      circle.setPosition(pointToRotateAround);
      Assertions.assertTrue(circle.supportPoint(diff).equals(pointToRotate, smallDelta));
      //rotate 360 different angles, should be sufficient
      for (int a = 1; i <= 360; i++) {
        Vector2f p = MathUtils.rotateAroundPoint(pointToRotate, pointToRotateAround, MathUtils.radian(a));
        diff = p.sub(pointToRotateAround, new Vector2f());
        Assertions.assertTrue(circle.supportPoint(diff).equals(p, smallDelta));
      }
    }
  }

  @Test
  public void encode() {
    Assertions.assertEquals(0b110100000000000, MathUtils.encode(new int[]{0, 1, 3}));
    Assertions.assertEquals(0b000100000000000, MathUtils.encode(3));
    Assertions.assertEquals(0b000000000000001, MathUtils.encode(14));
  }

  @Test
  public void copy() {
    Vector2f[] vectors = new Vector2f[3];
    vectors[0] = new Vector2f(1, 0);
    vectors[1] = new Vector2f(17, -4);
    vectors[2] = new Vector2f(-9, -5);
    Vector2f[] copy = MathUtils.copy(vectors);
    //not the same, but equals
    Assertions.assertArrayEquals(vectors, copy);
    Assertions.assertNotSame(vectors, copy);
    Assertions.assertNotSame(vectors[0], copy[0]);
    Assertions.assertNotSame(vectors[1], copy[1]);
    Assertions.assertNotSame(vectors[2], copy[2]);
    Assertions.assertEquals(vectors[0], copy[0]);
    Assertions.assertEquals(vectors[1], copy[1]);
    Assertions.assertEquals(vectors[2], copy[2]);
  }

  @Test
  public void map() {
    Assertions.assertEquals(75, MathUtils.map(0.5f, 0, 1, 50, 100), 0.0001f);
    Assertions.assertEquals(0, MathUtils.map(0.5f, 0, 1, -100, 100), 0.0001f);
  }

  @Test
  public void random() {
    double last = -1;
    for (int i = 0; i < 100; i++) {
      float random = MathUtils.random(0, 1);
      Assertions.assertNotEquals(last, random, minimalDelta);
    }

    int lastInt = -1;
    //THIS TEST MIGHT FAIL TO RANDOMNESS - TRY AGAIN IN THIS CASE
    for (int i = 0; i < 100; i++) {
      int rand = MathUtils.randomInt(0, Integer.MAX_VALUE);
      Assertions.assertNotEquals(lastInt, rand);
    }
  }

  @Test
  public void round() {
    for (int i = 0; i < 100; i++) {
      float random = MathUtils.random(0, 100);
      Assertions.assertEquals(Math.round(random), MathUtils.round(random), minimalDelta);
    }
  }

  @Test
  public void dist() {
    for (int i = 0; i < 100; i++) {
      Vector2f a = new Vector2f(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
      Vector2f b = new Vector2f(MathUtils.random(-100, 100), MathUtils.random(-100, 100));
      Assertions.assertEquals(a.distance(b), MathUtils.dist(a, b), smallDelta);
      Assertions.assertEquals(a.distance(b), MathUtils.dist(a.x, a.y, b.x, b.y), smallDelta);
    }
  }

  @Test
  public void constrain() {
    for (int i = 0; i < 10; i++) {
      int constrained = MathUtils.constrain(i, 3, 6);
      float constrainedF = MathUtils.constrain(i, 3.0f, 6.0f);
      if (i <= 3) {
        Assertions.assertEquals(3, constrained);
        Assertions.assertEquals(3.0f, constrainedF, minimalDelta);
      } else if (i < 6) {
        Assertions.assertEquals(i, constrained);
        Assertions.assertEquals(i, constrainedF, minimalDelta);
      } else {
        Assertions.assertEquals(6, constrained);
        Assertions.assertEquals(6.0f, constrainedF, minimalDelta);
      }
    }
  }

  @Test
  public void lerp() {
    Assertions.assertEquals(0.5f, MathUtils.lerp(0, 1, 0.5f), minimalDelta);
  }

  @Test
  public void shiftOverwrite() {
    //error checks
    float[] arr1 = new float[]{1, 2, 3, 4, 5};
    float[] arr1Copy = new float[]{1, 2, 3, 4, 5};
    //without indices 1 and 2
    float[] reduced = new float[]{1, 4, 5, 4, 5};

    //case: given indices doesnt match size
    int newL = MathUtils.shiftOverwrite(arr1, 0, 6);
    Assertions.assertEquals(arr1Copy.length, newL);
    Assertions.assertArrayEquals(arr1Copy, arr1, Float.MIN_VALUE);

    //case: given indices doesnt match size
    int newL2 = MathUtils.shiftOverwrite(arr1, 5, 5);
    Assertions.assertEquals(arr1Copy.length, newL2);
    Assertions.assertArrayEquals(arr1Copy, arr1, Float.MIN_VALUE);

    //case: given to < from
    int newL3 = MathUtils.shiftOverwrite(arr1, 3, 2);
    Assertions.assertEquals(arr1Copy.length, newL3);
    Assertions.assertArrayEquals(arr1Copy, arr1, Float.MIN_VALUE);

    //remove from first and keep last 2
    int newL4 = MathUtils.shiftOverwrite(arr1, 1, arr1.length - 2);
    Assertions.assertEquals(3, newL4);
    Assertions.assertArrayEquals(reduced, arr1, 0);

  }

//    @Test
//    public void gjksmCollision() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void expandingPolytopeAlgorithm() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void maxDotPointMinkDiff() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void maxDotPoint() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void solveSimultaneousEquations() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void rayCastIntersectionPoint() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void rayCastIntersection() {
//        //TODO hard to test
//    }
//
//    @Test
//    public void rayCastToLineIntersectionPoint() {
//        //TODO hard to test
//    }

  @Test
  public void centroidAndSphere() {
    List<Vector2f> points = new ArrayList<>(10);
    for (int i = 0; i < 10; i++) {
      //chance to have less points
      if (Math.random() > 0.3) {
        points.add(new Vector2f(MathUtils.random(-100, 100), MathUtils.random(-100, 100)));
      }
    }
    Vector2f[] convexHull = MathUtils.convexHull(points.toArray(new Vector2f[0]));
    //no real checks for convexHull
    Assertions.assertTrue(convexHull.length <= points.size());
    Vector2f centroid = MathUtils.polygonCentroid(convexHull);
    float radius = MathUtils.boundingSphere(centroid, convexHull);
    //checks for boundingSphere
    for (Vector2f point : convexHull) {
      Assertions.assertTrue(radius >= centroid.distance(point));
    }
  }

  @Test
  public void inCircle() {
    Random random = new Random(123456789);
    for (int i = 0; i < 100; i++) {
      float x1 = random.nextFloat() * 100 - 50;
      float y1 = random.nextFloat() * 100 - 50;
      float px1 = random.nextFloat() * 100 - 50;
      float py1 = random.nextFloat() * 100 - 50;
      float r = random.nextFloat() * 10;
      Vector2f p = new Vector2f(px1, py1);
      Vector2f c = new Vector2f(x1, y1);
      boolean inCircle = c.distanceSquared(p) <= r * r;
      Assertions.assertEquals(inCircle, MathUtils.inCircle(p, c, r));
      Assertions.assertEquals(inCircle, MathUtils.inCircle(p, x1, y1, r));
      Assertions.assertEquals(inCircle, MathUtils.inCircle(px1, py1, x1, y1, r));
    }
  }

  @Test
  public void inRect() {
    Vector2f in = new Vector2f(4, 4);
    Assertions.assertTrue(MathUtils.inRect(in, 0, 0, 10, 10));
    Assertions.assertFalse(MathUtils.inRect(in, 0, 5, 10, 10));
    Assertions.assertTrue(MathUtils.inRect(in.x, in.y, 0, 0, 10, 10));
    Assertions.assertFalse(MathUtils.inRect(in.x, in.y, 0, 5, 10, 10));
  }

  @Test
  public void rectInRect() {
    Transform transform = new Transform(0, 0, 10, 10);
    Transform inside = new Transform(1, 1, 7, 9);
    Transform notInside = new Transform(1, 1, 7, 19);
    Assertions.assertTrue(MathUtils.rectInRect(inside, transform));
    Assertions.assertFalse(MathUtils.rectInRect(notInside, transform));
  }

  @Test
  public void convexHull() {
    for (int test = 0; test < 10; test++) {
      Vector2f[] vectors = new Vector2f[(int) (Math.random() * 50 + 4)];
      for (int i = 0; i < vectors.length; i++) {
        vectors[i] = new Vector2f((float) (Math.random() * -1000 + 500), (float) (Math.random() * -1000 + 500));
      }
      ConvexPolygon polygon = new ConvexPolygon(vectors);
      polygon.setPosition(0, 0);
      // all non-edge points shall be inside the polygon
      for (Vector2f vec : Arrays.stream(vectors).filter(v -> !Arrays.asList(polygon.getAbsolutePoints()).contains(v)).collect(Collectors.toList())) {
        Point point = new Point(vec);
        point.setPosition(0, 0);
        Assertions.assertTrue(MathUtils.gjksmCollision(point, polygon).collision());
      }
    }
  }

  @Test
  public void boundingSphere() {
    for (int test = 0; test < 10; test++) {
      Vector2f[] vectors = new Vector2f[(int) (Math.random() * 15 + 4)];
      for (int i = 0; i < vectors.length; i++) {
        vectors[i] = new Vector2f((float) (Math.random() * -1000 + 500), (float) (Math.random() * -1000 + 500));
      }
      Vector2f[] vector2fs = MathUtils.convexHull(vectors);
      Vector2f c = MathUtils.polygonCentroid(vector2fs);
      float radius = MathUtils.boundingSphere(c, vector2fs);
      Assertions.assertNotNull(c);
      OptionalDouble max = Arrays.stream(vector2fs).mapToDouble(v -> v.distance(c)).max();
      Assertions.assertTrue(max.isPresent());
      double d = max.getAsDouble();
      Assertions.assertTrue(d <= radius);
    }
  }

//    @Test
//    public void polygonCentroid() {
//        //how to test?
//    }

  @Test
  public void randomInt() {
    //exhaustive testing for range
    int max = 1000000;
    for (int i = 0; i < max; i += 500) {
      int random = MathUtils.randomInt(i, max);
      Assertions.assertTrue(random >= i);
      Assertions.assertTrue(random <= max);
    }
  }


  @Test
  public void fastRandom() {
    //exhaustive testing for range
    int seedInt = (int) (Math.random() * Long.MAX_VALUE);
    for (int i = 0; i < 1000000; i += 500) {
      float random = MathUtils.fastRandom(i, seedInt);
      Assertions.assertTrue(random >= 0);
      Assertions.assertTrue(random <= 1);
    }
    long seedLong = (long) (Math.random() * Long.MAX_VALUE);
    for (long i = 0; i < 1000000000000L; i += 500000000L) {
      double random = MathUtils.fastRandom(i, seedLong);
      Assertions.assertTrue(random >= 0);
      Assertions.assertTrue(random <= 1);
    }
  }

  @Test
  public void fastFloor() {
    //exhaustive testing for precise output
    for (int i = 0; i < 100000; i++) {
      double d = Math.random() * Integer.MAX_VALUE;
      float f = (float) (Math.random() * Integer.MAX_VALUE);
      Assertions.assertEquals((int) Math.floor(d), MathUtils.fastFloor(d));
      Assertions.assertEquals((int) Math.floor(f), MathUtils.fastFloor(f));
    }
  }

}