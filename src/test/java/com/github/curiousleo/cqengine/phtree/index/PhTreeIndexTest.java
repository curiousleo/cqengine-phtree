package com.github.curiousleo.cqengine.phtree.index;

import static com.google.common.truth.Truth.assertThat;

import ch.ethz.globis.phtree.PhTree;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.github.curiousleo.cqengine.phtree.query.Cube;
import com.github.curiousleo.cqengine.phtree.testing.TestObject;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.persistence.support.CollectionWrappingObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.googlecode.cqengine.resultset.stored.StoredSetBasedResultSet;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class PhTreeIndexTest {

  private static final int DIMENSIONS = 2;
  private static final QueryOptions QUERY_OPTIONS = new QueryOptions();
  private static final Factory<StoredResultSet<TestObject>> STORED_RESULT_SET_FACTORY =
      () -> new StoredSetBasedResultSet<>(new HashSet<>());

  private static final TestObject A = new TestObject("A", new long[] {1, 1});
  private static final CollectionWrappingObjectStore<TestObject> OBJECT_STORE_CONTAINING_A =
      new CollectionWrappingObjectStore<>(Stream.of(A).collect(Collectors.toList()));

  @Test
  void testIndex() {
    final Cube<TestObject, Point> cube = new Cube<>(TestObject.KEY, A::getKey, A::getKey);
    final PhTreeIndex<TestObject, Point> phTreeIndex =
        new PhTreeIndex<>(TestObject.KEY, STORED_RESULT_SET_FACTORY, PhTree.create(DIMENSIONS));
    phTreeIndex.init(OBJECT_STORE_CONTAINING_A, QUERY_OPTIONS);

    assertThat(phTreeIndex.retrieve(cube, QUERY_OPTIONS)).containsExactly(A);
  }

  @Test
  void testIndexedCollection() {
    final Cube<TestObject, Point> cube = new Cube<>(TestObject.KEY, A::getKey, A::getKey);
    final IndexedCollection<TestObject> indexedCollection = new ConcurrentIndexedCollection<>();
    indexedCollection.addIndex(
        new PhTreeIndex<>(TestObject.KEY, STORED_RESULT_SET_FACTORY, PhTree.create(DIMENSIONS)));
    indexedCollection.add(A);

    assertThat(indexedCollection.retrieve(cube)).containsExactly(A);
  }
}
