java_binary(
    name = "bench",
    testonly = True,
    srcs = glob(["*.java"]),
    main_class = "com.github.curiousleo.cqengine.phtree.bench.Benchmark",
    deps = [
        "//thirdparty:jmh",
        "//java/com/github/curiousleo/cqengine/phtree/attribute",
        "//java/com/github/curiousleo/cqengine/phtree/common",
        "//java/com/github/curiousleo/cqengine/phtree/index",
        "//java/com/github/curiousleo/cqengine/phtree/query",
        "//thirdparty:phtree",
        "//thirdparty:cqengine",
    ],
)
