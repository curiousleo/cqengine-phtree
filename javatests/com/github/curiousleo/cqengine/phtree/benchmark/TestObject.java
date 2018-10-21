package com.github.curiousleo.cqengine.phtree.benchmark;

import com.github.curiousleo.cqengine.phtree.common.Point;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public final class TestObject {
  public static final Attribute<TestObject, Point> KEY =
      new SimpleAttribute<TestObject, Point>("key") {
        @Override
        public Point getValue(TestObject object, QueryOptions queryOptions) {
          return () -> object.key;
        }
      };

  private final long[] key;
  private final String name;

  public TestObject(String name, long[] key) {
    this.name = name;
    this.key = key;
  }

  @Override
  public String toString() {
    return "TestObject" + name;
  }

  public long[] getKey() {
    return key;
  }
}
