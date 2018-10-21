package com.github.curiousleo.cqengine.phtree.attribute;

import ch.ethz.globis.phtree.pre.PreProcessorPointF;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.function.Function;

/**
 * Turns a {@code double[]} attribute, represented by a function from {@code O} to {@code double[]},
 * into a {@link Point} attribute using a given {@link PreProcessorPointF} preprocessor.
 */
public final class PreProcessedPoint<O> extends SimpleAttribute<O, Point> {

  private final Function<O, double[]> pointAttribute;
  private final PreProcessorPointF preProcessor;

  public PreProcessedPoint(
      String attributeName,
      Function<O, double[]> pointAttribute,
      PreProcessorPointF preProcessor,
      Class<O> objectType) {
    super(objectType, Point.class, attributeName);
    this.pointAttribute = pointAttribute;
    this.preProcessor = preProcessor;
  }

  @Override
  public Point getValue(O object, QueryOptions queryOptions) {
    final double[] pre = pointAttribute.apply(object);
    final long[] post = new long[pre.length];
    preProcessor.pre(pre, post);
    return () -> post;
  }
}
