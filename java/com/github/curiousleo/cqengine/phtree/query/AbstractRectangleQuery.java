package com.github.curiousleo.cqengine.phtree.query;

import com.github.curiousleo.cqengine.phtree.common.Rectangle;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.SimpleQuery;

abstract class AbstractRectangleQuery<O, A extends Rectangle> extends SimpleQuery<O, A> {

  private final A rectangle;

  AbstractRectangleQuery(Attribute<O, A> attribute, A rectangle) {
    super(attribute);
    if (rectangle.lower().length != rectangle.upper().length) {
      throw new IllegalArgumentException("number of dimensions must be equal");
    }
    if (!lessThanOrEqualInEachDimension(rectangle.lower(), rectangle.upper())) {
      throw new IllegalArgumentException(
          "rectangle must be less than or equal to max in all dimensions");
    }
    this.rectangle = rectangle;
  }

  @Override
  protected boolean matchesSimpleAttribute(
      SimpleAttribute<O, A> attribute, O object, QueryOptions queryOptions) {
    A attributeValue = attribute.getValue(object, queryOptions);
    if (attributeValue.lower().length != getRectangle().lower().length
        || attributeValue.upper().length != getRectangle().upper().length) {
      throw new IllegalArgumentException("number of dimensions must be equal");
    }
    return matchesAttribute(attributeValue);
  }

  @Override
  protected boolean matchesNonSimpleAttribute(
      Attribute<O, A> attribute, O object, QueryOptions queryOptions) {
    Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
    for (A attributeValue : attributeValues) {
      if (attributeValue.lower().length != getRectangle().lower().length
          || attributeValue.upper().length != getRectangle().upper().length) {
        throw new IllegalArgumentException("number of dimensions must be equal");
      }
      if (matchesAttribute(attributeValue)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected int calcHashCode() {
    int result = attribute.hashCode();
    result = 31 * result + rectangle.hashCode();
    return result;
  }

  public A getRectangle() {
    return rectangle;
  }

  abstract boolean matchesAttribute(A attributeValue);

  boolean lessThanInEachDimension(long[] l1, long[] l2) {
    if (l1.length != l2.length) {
      throw new IllegalArgumentException("number of dimensions must be equal");
    }
    for (int i = 0; i < l1.length; i++) {
      if (l1[i] >= l2[i]) {
        return false;
      }
    }
    return true;
  }

  private boolean lessThanOrEqualInEachDimension(long[] l1, long[] l2) {
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
