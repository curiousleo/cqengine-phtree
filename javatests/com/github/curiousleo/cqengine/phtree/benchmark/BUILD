java_binary(
    name = "benchmark",
    testonly = True,
    srcs = glob(["*.java"]),
    main_class = "com.github.curiousleo.cqengine.phtree.benchmark.Benchmark",
    deps = [
        "//java/com/github/curiousleo/cqengine/phtree/attribute",
        "//java/com/github/curiousleo/cqengine/phtree/common",
        "//java/com/github/curiousleo/cqengine/phtree/index",
        "//java/com/github/curiousleo/cqengine/phtree/query",
        "//thirdparty:cqengine",
        "//thirdparty:jmh",
        "//thirdparty:phtree",
    ],
)
