package com.github.curiousleo.cqengine.phtree.query;

import static com.google.common.truth.Truth.assertThat;

import ch.ethz.globis.phtree.PhDistance;
import ch.ethz.globis.phtree.PhDistanceL;
import ch.ethz.globis.phtree.PhTree;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.github.curiousleo.cqengine.phtree.testing.TestObject;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class SphereTest {
  /*
  (y)
   +-1-2-3-4-+
   1       D |
   2 A B     |
   3     C   |
   +---------+ (x)
   */

  private static final TestObject A = new TestObject("A", new long[] {1, 2});
  private static final TestObject B = new TestObject("B", new long[] {2, 2});
  private static final TestObject C = new TestObject("C", new long[] {3, 3});
  private static final TestObject D = new TestObject("D", new long[] {4, 1});

  private static final PhDistance DISTANCE = PhDistanceL.THIS;
  private static final QueryOptions EMPTY_QUERY_OPTIONS = new QueryOptions();

  @Test
  public void new_Succeeds_WhenRadiusZero() {
    assertThat(new Sphere<>(TestObject.KEY, wrap(A), 0, DISTANCE)).isNotNull();
  }

  @Test
  public void new_Succeeds_WhenRadiusPositive() {
    assertThat(new Sphere<>(TestObject.KEY, wrap(A), 1, DISTANCE)).isNotNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void new_Fails_WhenRadiusNegative() {
    new Sphere<>(TestObject.KEY, wrap(A), -1, DISTANCE);
  }

  @Test
  public void match_Succeeds_ExactlyWhenPhTreeRangeQueryMatches() {
    final TestObject[] all = new TestObject[] {A, B, C, D};
    final double aToB = DISTANCE.dist(A.getKey(), B.getKey());
    final double bToD = DISTANCE.dist(B.getKey(), D.getKey());

    testCase(/* radius */ 0, /* center */ A, all, /* expected */ new TestObject[] {A});
    testCase(/* radius */ aToB / 2d, /* center */ A, all, /* expected */ new TestObject[] {A});
    testCase(/* radius */ aToB, /* center */ A, all, /* expected */ new TestObject[] {A, B});
    testCase(/* radius */ bToD, /* center */ B, all, /* expected */ all);
  }

  private void testCase(
      final double radius,
      final TestObject center,
      final TestObject[] all,
      final TestObject[] expected) {
    {
      // Test PhTree behaviour
      final PhTree<TestObject> phTree = PhTree.create(2);
      Arrays.asList(all).forEach(object -> phTree.put(object.getKey(), object));

      final Iterable<TestObject> matches =
          () -> phTree.rangeQuery(radius, DISTANCE, center.getKey());
      assertThat(matches).containsExactlyElementsIn(Arrays.asList(expected));
    }

    {
      // Test query behaviour
      final Sphere<TestObject, Point> sphere =
          new Sphere<>(TestObject.KEY, wrap(center), radius, DISTANCE);

      final List<TestObject> matches =
          Arrays.stream(all)
              .filter(testObject -> sphere.matches(testObject, EMPTY_QUERY_OPTIONS))
              .collect(Collectors.toList());
      assertThat(matches).containsExactlyElementsIn(Arrays.asList(expected));
    }
  }

  private static Point wrap(final TestObject testObject) {
    return testObject::getKey;
  }
}
