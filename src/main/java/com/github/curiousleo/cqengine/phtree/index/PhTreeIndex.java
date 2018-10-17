package com.github.curiousleo.cqengine.phtree.index;

import static com.googlecode.cqengine.index.support.IndexSupport.deduplicateIfNecessary;

import ch.ethz.globis.phtree.PhTree;
import com.github.curiousleo.cqengine.phtree.common.Point;
import com.github.curiousleo.cqengine.phtree.query.Cube;
import com.github.curiousleo.cqengine.phtree.query.Sphere;
import com.googlecode.concurrenttrees.common.LazyIterator;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.support.AbstractAttributeIndex;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.index.support.indextype.OnHeapTypeIndex;
import com.googlecode.cqengine.persistence.support.ObjectSet;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.query.simple.Equal;
import com.googlecode.cqengine.query.simple.In;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.iterator.IteratorUtil;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An index backed by a {@link PhTree}.
 *
 * <p>Supports query types:
 *
 * <ul>
 *   <li>{@link Equal}
 *   <li>{@link In}
 *   <li>{@link Cube}
 *   <li>{@link Sphere}
 * </ul>
 *
 * </ul>
 *
 * @param <O> The type of the object containing the attribute
 * @param <A> The type of the attribute on which the index is built. Must extend {@link Point}
 */
public final class PhTreeIndex<O, A extends Point> extends AbstractAttributeIndex<A, O>
    implements OnHeapTypeIndex {

  private static final int RETRIEVAL_COST = 15;
  private static final Set<Class<? extends Query>> SUPPORTED_QUERIES =
      Collections.unmodifiableSet(
          Stream.of(Equal.class, In.class, Cube.class, Sphere.class).collect(Collectors.toSet()));

  private final Factory<StoredResultSet<O>> valueSetFactory;
  private final PhTree<StoredResultSet<O>> phTree;

  public PhTreeIndex(
      Attribute<O, A> attribute,
      Factory<StoredResultSet<O>> valueSetFactory,
      PhTree<StoredResultSet<O>> phTree) {
    super(attribute, SUPPORTED_QUERIES);
    this.valueSetFactory = valueSetFactory;
    this.phTree = phTree;
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public boolean isQuantized() {
    return false;
  }

  @Override
  public ResultSet<O> retrieve(final Query<O> query, final QueryOptions queryOptions) {
    if (query instanceof Equal) {
      return retrieveEqual((Equal<O, A>) query, queryOptions);
    } else if (query instanceof In) {
      return retrieveIn((In<O, A>) query, queryOptions);
    } else if (query instanceof Sphere) {
      return retrieveSphere((Sphere<O, A>) query, queryOptions);
    } else if (query instanceof Cube) {
      return retrieveHyperCube((Cube<O, A>) query, queryOptions);
    } else {
      throw new IllegalArgumentException("Unsupported query: " + query);
    }
  }

  private ResultSet<O> retrieveEqual(final Equal<O, A> equal, final QueryOptions queryOptions) {
    return new ResultSet<O>() {
      @Override
      public Iterator<O> iterator() {
        final StoredResultSet<O> resultSet = phTree.get(equal.getValue().point());
        return resultSet != null ? resultSet.iterator() : Collections.emptyIterator();
      }

      @Override
      public boolean contains(O object) {
        StoredResultSet<O> resultSet = phTree.get(equal.getValue().point());
        return resultSet != null && resultSet.contains(object);
      }

      @Override
      public boolean matches(O object) {
        return equal.matches(object, queryOptions);
      }

      @Override
      public Query<O> getQuery() {
        return equal;
      }

      @Override
      public QueryOptions getQueryOptions() {
        return queryOptions;
      }

      @Override
      public int getRetrievalCost() {
        return RETRIEVAL_COST;
      }

      @Override
      public int getMergeCost() {
        return size();
      }

      @Override
      public int size() {
        StoredResultSet<O> resultSet = phTree.get(equal.getValue().point());
        return resultSet != null ? resultSet.size() : 0;
      }

      @Override
      public void close() {
        // do nothing
      }
    };
  }

  private ResultSet<O> retrieveIn(In<O, A> in, QueryOptions queryOptions) {
    final Iterable<ResultSet<O>> results =
        () ->
            new LazyIterator<ResultSet<O>>() {
              private final Iterator<A> values = in.getValues().iterator();

              @Override
              protected ResultSet<O> computeNext() {
                if (values.hasNext()) {
                  return retrieveEqual(new Equal<>(in.getAttribute(), values.next()), queryOptions);
                } else {
                  return endOfData();
                }
              }
            };
    return deduplicateIfNecessary(results, in, getAttribute(), queryOptions, RETRIEVAL_COST);
  }

  private ResultSet<O> retrieveSphere(final Sphere<O, A> sphere, final QueryOptions queryOptions) {
    return new ResultSet<O>() {
      @Override
      public Iterator<O> iterator() {
        return IteratorUtil.concatenate(
            phTree.rangeQuery(
                sphere.getRadius(), sphere.getDistance(), sphere.getCenter().point()));
      }

      @Override
      public boolean contains(O object) {
        return IteratorUtil.iterableContains(this, object);
      }

      @Override
      public boolean matches(O object) {
        return sphere.matches(object, queryOptions);
      }

      @Override
      public Query<O> getQuery() {
        return sphere;
      }

      @Override
      public QueryOptions getQueryOptions() {
        return queryOptions;
      }

      @Override
      public int getRetrievalCost() {
        return RETRIEVAL_COST;
      }

      @Override
      public int getMergeCost() {
        return size();
      }

      @Override
      public int size() {
        return IteratorUtil.countElements(this);
      }

      @Override
      public void close() {
        // do nothing
      }
    };
  }

  private ResultSet<O> retrieveHyperCube(Cube<O, A> cube, QueryOptions queryOptions) {
    return new ResultSet<O>() {
      @Override
      public Iterator<O> iterator() {
        return IteratorUtil.concatenate(phTree.query(cube.getMin().point(), cube.getMax().point()));
      }

      @Override
      public boolean contains(O object) {
        return IteratorUtil.iterableContains(this, object);
      }

      @Override
      public boolean matches(O object) {
        return cube.matches(object, queryOptions);
      }

      @Override
      public Query<O> getQuery() {
        return cube;
      }

      @Override
      public QueryOptions getQueryOptions() {
        return queryOptions;
      }

      @Override
      public int getRetrievalCost() {
        return RETRIEVAL_COST;
      }

      @Override
      public int getMergeCost() {
        return size();
      }

      @Override
      public int size() {
        return IteratorUtil.countElements(this);
      }

      @Override
      public void close() {
        // do nothing
      }
    };
  }

  @Override
  public Index<O> getEffectiveIndex() {
    return this;
  }

  @Override
  public boolean addAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
    try {
      boolean modified = false;

      for (O object : objectSet) {
        for (A attributeValue : getAttribute().getValues(object, queryOptions)) {
          StoredResultSet<O> valueSet = phTree.get(attributeValue.point());
          if (valueSet == null) {
            valueSet = valueSetFactory.create();
            phTree.put(attributeValue.point(), valueSet);
          }
          modified |= valueSet.add(object);
        }
      }
      return modified;
    } finally {
      objectSet.close();
    }
  }

  @Override
  public boolean removeAll(ObjectSet<O> objectSet, QueryOptions queryOptions) {
    try {
      boolean modified = false;

      for (O object : objectSet) {
        for (A attributeValue : getAttribute().getValues(object, queryOptions)) {
          final StoredResultSet<O> valueSet = phTree.get(attributeValue.point());
          if (valueSet != null) {
            modified |= valueSet.remove(object);
          }
        }
      }
      return modified;
    } finally {
      objectSet.close();
    }
  }

  @Override
  public void clear(QueryOptions queryOptions) {
    phTree.clear();
  }

  @Override
  public void init(ObjectStore<O> objectStore, QueryOptions queryOptions) {
    addAll(ObjectSet.fromObjectStore(objectStore, queryOptions), queryOptions);
  }
}
