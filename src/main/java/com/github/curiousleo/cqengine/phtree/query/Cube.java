package com.github.curiousleo.cqengine.phtree.query;

import com.github.curiousleo.cqengine.phtree.common.Point;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.SimpleQuery;

public final class Cube<O, A extends Point> extends SimpleQuery<O, A> {

  private final A min;
  private final A max;

  public Cube(Attribute<O, A> attribute, A min, A max) {
    super(attribute);
    if (min.point().length != max.point().length) {
      throw new IllegalArgumentException("number of dimensions must be equal");
    }
    if (!lessThanOrEqualInEachDimension(min, max)) {
      throw new IllegalArgumentException("min must be less than or equal to max in all dimensions");
    }
    this.min = min;
    this.max = max;
  }

  @Override
  protected boolean matchesSimpleAttribute(
      SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
    A attributeValue = attribute.getValue(object, queryOptions);
    if (attributeValue.point().length != min.point().length) {
      throw new IllegalArgumentException("number of dimensions must be equal");
    }
    return lessThanOrEqualInEachDimension(min, attributeValue)
        && lessThanOrEqualInEachDimension(attributeValue, max);
  }

  @Override
  protected boolean matchesNonSimpleAttribute(
      Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
    Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
    for (A attributeValue : attributeValues) {
      if (lessThanOrEqualInEachDimension(min, attributeValue)
          && lessThanOrEqualInEachDimension(attributeValue, max)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected int calcHashCode() {
    int result = attribute.hashCode();
    result = 31 * result + min.hashCode();
    result = 31 * result + max.hashCode();
    return result;
  }

  public A getMin() {
    return min;
  }

  public A getMax() {
    return max;
  }

  private boolean lessThanOrEqualInEachDimension(A a1, A a2) {
    final long[] l1 = a1.point();
    final long[] l2 = a2.point();
    if (l1.length != l2.length) {
      throw new IllegalArgumentException("number of dimensions must be equal");
    }
    for (int i = 0; i < l1.length; i++) {
      if (l1[i] > l2[i]) {
        return false;
      }
    }
    return true;
  }
}
