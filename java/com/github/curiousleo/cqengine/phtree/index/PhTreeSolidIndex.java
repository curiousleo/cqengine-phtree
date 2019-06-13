package com.github.curiousleo.cqengine.phtree.index;

import static com.googlecode.cqengine.index.support.IndexSupport.deduplicateIfNecessary;

import ch.ethz.globis.phtree.PhTreeSolid;
import com.github.curiousleo.cqengine.phtree.common.Rectangle;
import com.github.curiousleo.cqengine.phtree.query.Inclusion;
import com.github.curiousleo.cqengine.phtree.query.Intersection;
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
 * An index backed by a {@link PhTreeSolid}.
 *
 * <p>Supports query types:
 *
 * <ul>
 *   <li>{@link Equal}
 *   <li>{@link In}
 *   <li>{@link Inclusion}
 *   <li>{@link Intersection}
 * </ul>
 *
 * @param <O> The type of the object containing the attribute
 * @param <A> The type of the attribute on which the index is built. Must extend {@link Rectangle}
 */
public final class PhTreeSolidIndex<O, A extends Rectangle> extends AbstractAttributeIndex<A, O>
    implements OnHeapTypeIndex {

  private static final int RETRIEVAL_COST = 15;
  private static final Set<Class<? extends Query>> SUPPORTED_QUERIES =
      Collections.unmodifiableSet(
          Stream.of(Equal.class, In.class, Inclusion.class, Intersection.class)
              .collect(Collectors.toSet()));

  private final Factory<StoredResultSet<O>> valueSetFactory;
  private final PhTreeSolid<StoredResultSet<O>> phTree;

  public PhTreeSolidIndex(
      Attribute<O, A> attribute,
      Factory<StoredResultSet<O>> valueSetFactory,
      PhTreeSolid<StoredResultSet<O>> phTree) {
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
    } else if (query instanceof Inclusion) {
      return retrieveInclusion((Inclusion<O, A>) query, queryOptions);
    } else if (query instanceof Intersection) {
      return retrieveIntersection((Intersection<O, A>) query, queryOptions);
    } else {
      throw new IllegalArgumentException("Unsupported query: " + query);
    }
  }

  private ResultSet<O> retrieveEqual(final Equal<O, A> equal, final QueryOptions queryOptions) {
    return new ResultSet<O>() {
      @Override
      public Iterator<O> iterator() {
        final StoredResultSet<O> resultSet =
            phTree.get(equal.getValue().lower(), equal.getValue().upper());
        return resultSet != null ? resultSet.iterator() : Collections.emptyIterator();
      }

      @Override
      public boolean contains(O object) {
        StoredResultSet<O> resultSet =
            phTree.get(equal.getValue().lower(), equal.getValue().upper());
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
        StoredResultSet<O> resultSet =
            phTree.get(equal.getValue().lower(), equal.getValue().upper());
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
            new Iterator<ResultSet<O>>() {
              private final Iterator<A> values = in.getValues().iterator();

              @Override
              public boolean hasNext() {
                return values.hasNext();
              }

              @Override
              public ResultSet<O> next() {

                return retrieveEqual(new Equal<>(in.getAttribute(), values.next()), queryOptions);
              }
            };
    return deduplicateIfNecessary(results, in, getAttribute(), queryOptions, RETRIEVAL_COST);
  }

  private ResultSet<O> retrieveInclusion(
      final Inclusion<O, A> inclusion, final QueryOptions queryOptions) {
    return new ResultSet<O>() {
      @Override
      public Iterator<O> iterator() {
        return IteratorUtil.concatenate(
            phTree.queryInclude(
                inclusion.getRectangle().lower(), inclusion.getRectangle().upper()));
      }

      @Override
      public boolean contains(O object) {
        return IteratorUtil.iterableContains(this, object);
      }

      @Override
      public boolean matches(O object) {
        return inclusion.matches(object, queryOptions);
      }

      @Override
      public Query<O> getQuery() {
        return inclusion;
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

  private ResultSet<O> retrieveIntersection(
      Intersection<O, A> intersection, QueryOptions queryOptions) {
    return new ResultSet<O>() {
      @Override
      public Iterator<O> iterator() {
        return IteratorUtil.concatenate(
            phTree.queryIntersect(
                intersection.getRectangle().lower(), intersection.getRectangle().upper()));
      }

      @Override
      public boolean contains(O object) {
        return IteratorUtil.iterableContains(this, object);
      }

      @Override
      public boolean matches(O object) {
        return intersection.matches(object, queryOptions);
      }

      @Override
      public Query<O> getQuery() {
        return intersection;
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
          StoredResultSet<O> valueSet = phTree.get(attributeValue.lower(), attributeValue.upper());
          if (valueSet == null) {
            valueSet = valueSetFactory.create();
            phTree.put(attributeValue.lower(), attributeValue.upper(), valueSet);
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
          final StoredResultSet<O> valueSet =
              phTree.get(attributeValue.lower(), attributeValue.upper());
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

  @Override
  public void destroy(QueryOptions queryOptions) {
    // No-op.
  }
}
