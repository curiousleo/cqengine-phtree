package(default_visibility = ["//visibility:public"])

java_library(
    name = "phtree",
    exports = ["@ch_ethz_globis_phtree//jar"],
    runtime_deps = [":concurrent_trees"],
)

java_library(
    name = "cqengine",
    exports = ["@com_googlecode_cqengine//jar"],
)

java_library(
    name = "concurrent_trees",
    exports = ["@com_googlecode_concurrent_trees//jar"],
)

java_library(
    name = "testing",
    testonly = True,
    exports = [
        "@com_google_guava//jar",
        "@com_google_truth//jar",
        "@junit_junit4//jar",
        "@org_hamcrest_core//jar",
    ],
)
