package com.github.curiousleo.cqengine.phtree.common;

/**
 * Represents a rectangle with {@code long} valued coordinates with an arbitrary number of
 * dimensions.
 */

public interface Rectangle {

  long[] lower();

  long[] upper();
}
