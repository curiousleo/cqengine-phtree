package com.github.curiousleo.cqengine.phtree.bench;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class TestSet {

  public static Set<TestObject> createTestSet(int size, long[] min, long[] max) {
    final int dimensions = min.length;
    if (dimensions < 1 || dimensions != max.length) {
      throw new IllegalArgumentException("min and max must be nonempty and have equal length");
    }
    for (int i = 0; i < dimensions; i++) {
      if (max[i] < min[i]) {
        throw new IllegalArgumentException("max must not be less than min in any dimension");
      }
    }

    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final LinkedHashSet<TestObject> result = new LinkedHashSet<>(size);
    for (int i = 0; i < size; i++) {
      long[] key = new long[dimensions];
      for (int j = 0; j < dimensions; j++) {
        key[j] = random.nextLong(min[j], max[j]);
      }
      result.add(new TestObject(Integer.toString(i), key));
    }
    return result;
  }
}
