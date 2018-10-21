package com.github.curiousleo.cqengine.phtree.query;

import com.github.curiousleo.cqengine.phtree.common.Rectangle;
import com.googlecode.cqengine.attribute.Attribute;

public final class Intersection<O, A extends Rectangle> extends AbstractRectangleQuery<O, A> {

  public Intersection(Attribute<O, A> attribute, A rectangle) {
    super(attribute, rectangle);
  }

  @Override
  boolean matchesAttribute(A attributeValue) {
    return lessThanInEachDimension(attributeValue.lower(), getRectangle().upper())
        && lessThanInEachDimension(getRectangle().lower(), attributeValue.upper());
  }
}
