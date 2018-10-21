package(default_visibility = ["//visibility:public"])

java_test(
    name = "index",
    srcs = glob(["*.java"]),
    deps = [
        "//src/java/com/github/curiousleo/cqengine/phtree/common",
        "//src/java/com/github/curiousleo/cqengine/phtree/query",
        "//src/java/com/github/curiousleo/cqengine/phtree/index",
        "//src/javatests/com/github/curiousleo/cqengine/phtree/testing",
        "//thirdparty:concurrent_trees",
        "//thirdparty:cqengine",
        "//thirdparty:phtree",
        "//thirdparty:testing",
    ],
)
