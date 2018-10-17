package com.github.curiousleo.cqengine.phtree.attribute;

import static com.google.common.truth.Truth.assertThat;

import ch.ethz.globis.phtree.pre.PreProcessorPointF.IEEE;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.googlecode.cqengine.query.option.QueryOptions;
import org.junit.jupiter.api.Test;

class PreProcessedPointTest {

  @Test
  void test() {
    final IEEE ieee = new IEEE();
    final double[] pre = new double[]{1d, 2d};
    final Object[] post = {ieee.pre(pre[0]), ieee.pre(pre[1])};
    final PreProcessedPoint<Object> preProcessedPoint = new PreProcessedPoint<>(
        "testAttribute", __ -> pre, ieee, Object.class);
    final Point postPoint = preProcessedPoint.getValue(new Object(), new QueryOptions());

    assertThat(postPoint.point()).asList().containsExactly(post);
  }
}
