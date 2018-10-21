java_binary(
    name = "bench",
    testonly = True,
    srcs = glob(["*.java"]),
    main_class = "com.github.curiousleo.cqengine.phtree.bench.Benchmark",
    deps = [
        "//thirdparty:jmh",
        "//src/java/com/github/curiousleo/cqengine/phtree/attribute",
        "//src/java/com/github/curiousleo/cqengine/phtree/common",
        "//src/java/com/github/curiousleo/cqengine/phtree/index",
        "//src/java/com/github/curiousleo/cqengine/phtree/query",
        "//thirdparty:phtree",
        "//thirdparty:cqengine",
    ],
)
