# cqengine-phtree

[![Build Status](https://api.travis-ci.org/curiousleo/cqengine-phtree.svg?branch=master)](https://travis-ci.org/curiousleo/cqengine-phtree)

[PhTree](https://github.com/tzaeschke/phtree) indices for [CQEngine](https://github.com/npgall/cqengine).

## Features

This Java library provides CQEngine-compatible spatial queries and indices for points and rectangles with an arbitrary number of dimensions.

The `Cube` and `Sphere` query classes can be used on `Point` attributes. `PhTreeIndex` is a CQEngine index that supports these point queries efficiently.

The `Inclusion` and `Intersection` classes can be used on `Rectangle` attributes. `PhTreeSolidIndex` is a CQEngine index that supports these rectangle queries efficiently.

## Performance

Run the [JMH](https://openjdk.java.net/projects/code-tools/jmh/) benchmark suite as follows:

```
bazel run //javatests/com/github/curiousleo/cqengine/phtree/benchmark
```
