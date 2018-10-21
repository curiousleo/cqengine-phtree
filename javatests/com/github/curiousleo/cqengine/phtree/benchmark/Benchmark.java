package com.github.curiousleo.cqengine.phtree.benchmark;

import ch.ethz.globis.phtree.PhTree;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.github.curiousleo.cqengine.phtree.index.PhTreeIndex;
import com.github.curiousleo.cqengine.phtree.query.Cube;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.AuxCounters;
import org.openjdk.jmh.annotations.AuxCounters.Type;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Benchmark {

  private static final QueryOptions QUERY_OPTIONS = new QueryOptions();
  private static final Factory<StoredResultSet<TestObject>> STORED_RESULT_SET_FACTORY =
      () -> new StoredSetBasedResultSet<>(new HashSet<>());
  private static final int SIDE_LENGTH = 10_000;
  private static final int COLLECTION_SIZE = 100_000;

  @State(Scope.Thread)
  @AuxCounters(Type.OPERATIONS)
  public static class Context {

    Set<TestObject> objects;
    IndexedCollection<TestObject> objectsIndexed;

    Cube<TestObject, Point> cube;

    @Param({"0.001", "0.01", "0.1", "0.5", "1.0"})
    private double cover;

    @Param({"1", "2", "5", "10"})
    private int dimensions;

    public long retrieved = 0;

    @Setup
    public void setUp() {
      {
        final long[] min = new long[dimensions];
        final long[] max = new long[dimensions];
        Arrays.fill(min, 0);
        Arrays.fill(max, SIDE_LENGTH);
        objects = TestSet.createTestSet(COLLECTION_SIZE, min, max);
      }
      objectsIndexed = new ConcurrentIndexedCollection<>();
      objectsIndexed.addIndex(
          new PhTreeIndex<>(TestObject.KEY, STORED_RESULT_SET_FACTORY, PhTree.create(dimensions)));
      objectsIndexed.addAll(objects);

      {
        final long querySide = (long) (SIDE_LENGTH * Math.pow(cover, 1d / dimensions));
        final long minValue = (SIDE_LENGTH - querySide) / 2;
        final long maxValue = (SIDE_LENGTH + querySide) / 2;
        final long[] min = new long[dimensions];
        final long[] max = new long[dimensions];
        Arrays.fill(min, minValue);
        Arrays.fill(max, maxValue);
        cube = new Cube<>(TestObject.KEY, () -> min, () -> max);
      }
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void testWithoutIndex(Context context, Blackhole blackhole) {
    for (TestObject testObject : context.objects) {
      if (context.cube.matches(testObject, QUERY_OPTIONS)) {
        context.retrieved++;
      }
      blackhole.consume(testObject);
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void testWithIndex(Context context, Blackhole blackhole) {
    for (TestObject testObject : context.objectsIndexed.retrieve(context.cube, QUERY_OPTIONS)) {
      context.retrieved++;
      blackhole.consume(testObject);
    }
  }

  public static void main(String[] args) throws RunnerException {
    Options options =
        new OptionsBuilder()
            .include(Benchmark.class.getSimpleName())
            .forks(1)
            .warmupIterations(2)
            .measurementIterations(2)
            .timeUnit(TimeUnit.MILLISECONDS)
            .build();
    new Runner(options).run();
  }
}
