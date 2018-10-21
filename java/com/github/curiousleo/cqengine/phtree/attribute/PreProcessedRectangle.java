package com.github.curiousleo.cqengine.phtree.attribute;

import ch.ethz.globis.phtree.pre.PreProcessorPointF;
import com.github.curiousleo.cqengine.phtree.common.Rectangle;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.function.Function;

/**
 * Turns a pair of {@code double[]} attributes, represented by a pair of functions from {@code O} to
 * {@code double[]}, into a {@link Rectangle} attribute using a given {@link PreProcessorPointF}
 * preprocessor.
 */
public final class PreProcessedRectangle<O> extends SimpleAttribute<O, Rectangle> {

  private final Function<O, double[]> lowerAttribute;
  private final Function<O, double[]> upperAttribute;
  private final PreProcessorPointF preProcessor;

  public PreProcessedRectangle(
      String attributeName,
      Function<O, double[]> lowerAttribute,
      Function<O, double[]> upperAttribute,
      Class<O> objectType,
      PreProcessorPointF preProcessor) {
    super(objectType, Rectangle.class, attributeName);
    this.lowerAttribute = lowerAttribute;
    this.preProcessor = preProcessor;
    this.upperAttribute = upperAttribute;
  }

  @Override
  public Rectangle getValue(O object, QueryOptions queryOptions) {
    final double[] lowerPre = lowerAttribute.apply(object);
    final long[] lowerPost = new long[lowerPre.length];
    preProcessor.pre(lowerPre, lowerPost);

    final double[] upperPre = upperAttribute.apply(object);
    final long[] upperPost = new long[upperPre.length];
    preProcessor.pre(upperPre, upperPost);

    return new Rectangle() {
      @Override
      public long[] lower() {
        return lowerPost;
      }

      @Override
      public long[] upper() {
        return upperPost;
      }
    };
  }
}
