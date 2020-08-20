load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")

# Version check

BAZEL_SKYLIB_EXTERNAL_TAG = "1.0.2"

BAZEL_SKYLIB_EXTERNAL_SHA = "64ad2728ccdd2044216e4cec7815918b7bb3bb28c95b7e9d951f9d4eccb07625"

http_archive(
    name = "bazel_skylib",
    sha256 = BAZEL_SKYLIB_EXTERNAL_SHA,
    strip_prefix = "bazel-skylib-%s" % BAZEL_SKYLIB_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/bazel-skylib/archive/%s.zip" % BAZEL_SKYLIB_EXTERNAL_TAG,
)

load("@bazel_skylib//lib:versions.bzl", "versions")

versions.check(minimum_bazel_version = "2.0.0")

# Maven rule

RULES_JVM_EXTERNAL_TAG = "3.3"

RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

# Java tools

http_archive(
    name = "remote_java_tools_linux",
    sha256 = "0be37f4227590ecb6bc929a6a7e427c65e6239363e4c3b28b1a211718b9636c9",
    urls = [
        "https://mirror.bazel.build/bazel_java_tools/releases/javac11/v9.0/java_tools_javac11_linux-v9.0.zip",
        "https://github.com/bazelbuild/java_tools/releases/download/javac11-v9.0/java_tools_javac11_linux-v9.0.zip",
    ],
)

http_archive(
    name = "remote_java_tools_windows",
    sha256 = "14b679e3bf6a7b4aec36dc33f15ad0027aef43f1bc92e1e2f5abf3a93c156bb5",
    urls = [
        "https://mirror.bazel.build/bazel_java_tools/releases/javac11/v9.0/java_tools_javac11_windows-v9.0.zip",
        "https://github.com/bazelbuild/java_tools/releases/download/javac11-v9.0/java_tools_javac11_windows-v9.0.zip",
    ],
)

http_archive(
    name = "remote_java_tools_darwin",
    sha256 = "567f5fe77e0c561b454930dea412899543849510f48f9c092dfcff8192b4086f",
    urls = [
        "https://mirror.bazel.build/bazel_java_tools/releases/javac11/v9.0/java_tools_javac11_darwin-v9.0.zip",
        "https://github.com/bazelbuild/java_tools/releases/download/javac11-v9.0/java_tools_javac11_darwin-v9.0.zip",
    ],
)

# Maven dependencies

JMH_VERSION = "1.21"

maven_install(
    artifacts = [
        # Project dependencies
        "com.googlecode.cqengine:cqengine:3.4.0",
        "com.googlecode.concurrent-trees:concurrent-trees:2.6.1",
        "ch.ethz.globis.phtree:phtree:2.3.0",
        # Test dependencies
        "junit:junit:4.12",
        "com.google.truth:truth:0.45",
        "org.hamcrest:hamcrest-core:1.3",
        "com.google.guava:guava:27.1-jre",
        # Benchmark dependencies
        "org.openjdk.jmh:jmh-core:%s" % JMH_VERSION,
        "org.openjdk.jmh:jmh-generator-annprocess:%s" % JMH_VERSION,
        "org.openjdk.jmh:jmh-generator-reflection:%s" % JMH_VERSION,
        "org.openjdk.jmh:jmh-generator-bytecode:%s" % JMH_VERSION,
        "org.apache.commons:commons-math3:3.6.1",
    ],
    fetch_sources = True,
    # Run `bazel run @unpinned_maven//:pin` to update maven_install.json
    maven_install_json = "//:maven_install.json",
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

# Formatting

http_file(
    name = "buildifier_darwin",
    executable = True,
    sha256 = "66a569152bf59a527000941758a25f1dd03d6e26302d7982fd8aee25e552a10c",
    urls = ["https://github.com/bazelbuild/buildtools/releases/download/0.17.2/buildifier.osx"],
)

http_file(
    name = "buildifier_linux",
    executable = True,
    sha256 = "1cf35c463944003ceb3c3716d7fc489d3d70625e34a8127dfd8b272afad7e0fd",
    urls = ["https://github.com/bazelbuild/buildtools/releases/download/0.17.2/buildifier"],
)

http_file(
    name = "google_java_format_jar",
    downloaded_file_path = "google-java-format.jar",
    sha256 = "73faf7c9b95bffd72933fa24f23760a6b1d18499151cb39a81cda591ceb7a5f4",
    urls = ["https://github.com/google/google-java-format/releases/download/google-java-format-1.6/google-java-format-1.6-all-deps.jar"],
)
