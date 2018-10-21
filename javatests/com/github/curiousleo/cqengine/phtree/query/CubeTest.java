package com.github.curiousleo.cqengine.phtree.query;

import static com.google.common.truth.Truth.assertThat;

import ch.ethz.globis.phtree.PhTree;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.github.curiousleo.cqengine.phtree.testing.TestObject;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class CubeTest {

  /*
  (y)
   +-1-2-3-4-+
   1 A     B |
   2   C     |
   3     D   |
   +---------+ (x)
   */

  private static final TestObject A = new TestObject("A", new long[] {1, 1});
  private static final TestObject B = new TestObject("B", new long[] {1, 4});
  private static final TestObject C = new TestObject("C", new long[] {2, 2});
  private static final TestObject D = new TestObject("D", new long[] {3, 3});

  private static final QueryOptions EMPTY_QUERY_OPTIONS = new QueryOptions();

  @Test
  public void new_CreatesHyperCube_WhenMinLessThanMax() {
    assertThat(new Cube<>(TestObject.KEY, wrap(A), wrap(C))).isNotNull();
  }

  @Test
  public void new_CreatesHyperCube_WhenMinEqualsMax() {
    assertThat(new Cube<>(TestObject.KEY, wrap(A), wrap(A))).isNotNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void new_Throws_WhenMinAndMaxHaveDifferentDimensions() {
    long[] max1d = {1};
    new Cube<>(TestObject.KEY, wrap(A), () -> max1d);
  }

  @Test(expected = IllegalArgumentException.class)
  public void new_Throws_WhenMinNotSmallerThanMax() {
    new Cube<>(TestObject.KEY, wrap(C), wrap(A));
  }

  @Test
  public void match_Succeeds_ExactlyWhenPhTreeQueryMatches() {
    final TestObject[] all = new TestObject[] {A, B, C, D};
    testCase(/* min */ A, /* max */ A, all, /* expected */ new TestObject[] {A});
    testCase(/* min */ A, /* max */ B, all, /* expected */ new TestObject[] {A, B});
    testCase(/* min */ A, /* max */ C, all, /* expected */ new TestObject[] {A, C});
    testCase(/* min */ A, /* max */ D, all, /* expected */ new TestObject[] {A, C, D});
    testCase(/* min */ C, /* max */ D, all, /* expected */ new TestObject[] {C, D});

    // Test empty result set.
    testCase(
        /* min */ A, /* max */
        C,
        /* all */ new TestObject[] {B, D}, /* expected */
        new TestObject[] {});
  }

  private void testCase(
      final TestObject min,
      final TestObject max,
      final TestObject[] all,
      final TestObject[] expected) {
    {
      // Test PhTree behaviour
      final PhTree<TestObject> phTree = PhTree.create(2);
      Arrays.asList(all).forEach(object -> phTree.put(object.getKey(), object));

      final Iterable<TestObject> matches = () -> phTree.query(min.getKey(), max.getKey());
      assertThat(matches).containsExactlyElementsIn(Arrays.asList(expected));
    }

    {
      // Test query behaviour
      final Cube<TestObject, Point> cube = new Cube<>(TestObject.KEY, wrap(min), wrap(max));

      final List<TestObject> matches =
          Arrays.stream(all)
              .filter(testObject -> cube.matches(testObject, EMPTY_QUERY_OPTIONS))
              .collect(Collectors.toList());
      assertThat(matches).containsExactlyElementsIn(Arrays.asList(expected));
    }
  }

  private static Point wrap(final TestObject testObject) {
    return testObject::getKey;
  }
}
