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

# Maven dependencies

JMH_VERSION = "1.25"

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
    sha256 = "3c30fcddfea8b515fff75127788c16dca5d901873ec4cf2102225cccbffc1702",
    urls = ["https://github.com/bazelbuild/buildtools/releases/download/3.4.0/buildifier.mac"],
)

http_file(
    name = "buildifier_linux",
    executable = True,
    sha256 = "5d47f5f452bace65686448180ff63b4a6aaa0fb0ce0fe69976888fa4d8606940",
    urls = ["https://github.com/bazelbuild/buildtools/releases/download/3.4.0/buildifier"],
)

http_file(
    name = "google_java_format_jar",
    downloaded_file_path = "google-java-format.jar",
    sha256 = "29c864e58db8784028f4871fa4ef1e9cfcc0e5b9939ead09c7f1fc59e64737be",
    urls = ["https://github.com/google/google-java-format/releases/download/google-java-format-1.8/google-java-format-1.8-all-deps.jar"],
)
