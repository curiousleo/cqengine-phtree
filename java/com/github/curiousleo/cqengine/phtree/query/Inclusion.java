package com.github.curiousleo.cqengine.phtree.query;

import com.github.curiousleo.cqengine.phtree.common.Rectangle;
import com.googlecode.cqengine.attribute.Attribute;

public final class Inclusion<O, A extends Rectangle> extends AbstractRectangleQuery<O, A> {

  public Inclusion(Attribute<O, A> attribute, A rectangle) {
    super(attribute, rectangle);
  }

  @Override
  boolean matchesAttribute(A attributeValue) {
    return lessThanInEachDimension(getRectangle().lower(), attributeValue.lower())
        && lessThanInEachDimension(attributeValue.upper(), getRectangle().upper());
  }
}
