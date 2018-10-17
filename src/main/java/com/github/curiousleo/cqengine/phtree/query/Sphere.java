package com.github.curiousleo.cqengine.phtree.query;

import ch.ethz.globis.phtree.PhDistance;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.SimpleQuery;

public final class Sphere<O, A extends Point> extends SimpleQuery<O, A> {

  private final A center;
  private final double radius;
  private final PhDistance distance;

  Sphere(Attribute<O, A> attribute, A center, double radius, PhDistance distance) {
    super(attribute);
    if (radius < 0) {
      throw new IllegalArgumentException("radius must not be negative");
    }
    this.center = center;
    this.radius = radius;
    this.distance = distance;
  }

  @Override
  protected boolean matchesSimpleAttribute(SimpleAttribute<O, A> attribute, O object,
      QueryOptions queryOptions) {
    A attributeValue = attribute.getValue(object, queryOptions);
    return isWithinRadius(attributeValue);
  }

  @Override
  protected boolean matchesNonSimpleAttribute(Attribute<O, A> attribute, O object,
      QueryOptions queryOptions) {
    Iterable<A> attributeValues = attribute.getValues(object, queryOptions);
    for (A attributeValue : attributeValues) {
      if (isWithinRadius(attributeValue)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected int calcHashCode() {
    int result = attribute.hashCode();
    result = 31 * result + center.hashCode();
    result = 31 * result + Double.hashCode(radius);
    result = 31 * result + distance.hashCode();
    return result;
  }

  public A getCenter() {
    return center;
  }

  public double getRadius() {
    return radius;
  }

  public PhDistance getDistance() {
    return distance;
  }

  private boolean isWithinRadius(A attributeValue) {
    return distance.dist(center.point(), attributeValue.point()) <= radius;
  }
}
