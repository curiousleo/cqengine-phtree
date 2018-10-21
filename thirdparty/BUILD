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

genrule(
    name = "buildifier",
    srcs = select({
        "@bazel_tools//src/conditions:darwin": ["@buildifier_darwin//file"],
        "//conditions:default": ["@buildifier_linux//file"],
    }),
    outs = ["buildifier.bin"],
    cmd = "cp $(SRCS) $@",
    executable = True,
)

java_import(
    name = "java_format_import",
    jars = ["@google_java_format_jar//file"],
)

java_binary(
    name = "java_format",
    main_class = "com.google.googlejavaformat.java.Main",
    runtime_deps = [":java_format_import"],
)

java_plugin(
    name = "jmh_plugin",
    testonly = True,
    processor_class = "org.openjdk.jmh.generators.BenchmarkProcessor",
    visibility = ["//visibility:private"],
    deps = [
        "@org_openjdk_jmh_core//jar",
        "@org_openjdk_jmh_generator_annprocess//jar",
        "@org_openjdk_jmh_generator_bytecode//jar",
        "@org_openjdk_jmh_generator_reflection//jar",
    ],
)

java_library(
    name = "jmh",
    testonly = True,
    exported_plugins = [":jmh_plugin"],
    exports = [
        "@org_openjdk_jmh_core//jar",
    ],
    runtime_deps = [
        "@org_apache_commons_math3//jar",
    ],
)
