package(default_visibility = ["//visibility:public"])

java_library(
    name = "phtree",
    exports = ["@maven//:ch_ethz_globis_phtree_phtree"],
    runtime_deps = [":concurrent_trees"],
)

java_library(
    name = "cqengine",
    exports = ["@maven//:com_googlecode_cqengine_cqengine"],
)

java_library(
    name = "concurrent_trees",
    exports = ["@maven//:com_googlecode_concurrent_trees_concurrent_trees"],
)

java_library(
    name = "testing",
    testonly = True,
    exports = [
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
        "@maven//:org_hamcrest_hamcrest_core",
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
        "@maven//:org_openjdk_jmh_jmh_core",
        "@maven//:org_openjdk_jmh_jmh_generator_annprocess",
        "@maven//:org_openjdk_jmh_jmh_generator_bytecode",
        "@maven//:org_openjdk_jmh_jmh_generator_reflection",
    ],
)

java_library(
    name = "jmh",
    testonly = True,
    exported_plugins = [":jmh_plugin"],
    exports = [
        "@maven//:org_openjdk_jmh_jmh_core",
    ],
    runtime_deps = [
        "@maven//:org_apache_commons_commons_math3",
    ],
)
