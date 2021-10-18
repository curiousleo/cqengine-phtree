load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive", "http_file")

# Version check

BAZEL_SKYLIB_EXTERNAL_TAG = "1.1.1"

BAZEL_SKYLIB_EXTERNAL_SHA = "fc64d71583f383157e3e5317d24e789f942bc83c76fde7e5981cadc097a3c3cc"

http_archive(
    name = "bazel_skylib",
    sha256 = BAZEL_SKYLIB_EXTERNAL_SHA,
    strip_prefix = "bazel-skylib-%s" % BAZEL_SKYLIB_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/bazel-skylib/archive/%s.zip" % BAZEL_SKYLIB_EXTERNAL_TAG,
)

load("@bazel_skylib//lib:versions.bzl", "versions")

versions.check(minimum_bazel_version = "2.0.0")

# Maven rule

RULES_JVM_EXTERNAL_TAG = "4.1"

RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

# Maven dependencies

JMH_VERSION = "1.33"

maven_install(
    artifacts = [
        # Project dependencies
        "com.googlecode.cqengine:cqengine:3.6.0",
        "com.googlecode.concurrent-trees:concurrent-trees:2.6.1",
        "ch.ethz.globis.phtree:phtree:2.5.0",
        # Test dependencies
        "junit:junit:4.13.2",
        "com.google.truth:truth:1.1.3",
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

BUILDIFIER_VERSION = "4.2.2"

http_file(
    name = "buildifier_darwin",
    executable = True,
    sha256 = "105353d741d7dd9788a7c886591e3d3f41520d4eec624a36410b2b7456ef11c6",
    urls = ["https://github.com/bazelbuild/buildtools/releases/download/%s/buildifier-darwin-amd64" % BUILDIFIER_VERSION],
)

http_file(
    name = "buildifier_linux",
    executable = True,
    sha256 = "3f0e450cd852dbfd89aa2761d85f9fbeb6f0faccfc5d4fbe48952cfe0712922a",
    urls = ["https://github.com/bazelbuild/buildtools/releases/download/%s/buildifier-linux-amd64" % BUILDIFIER_VERSION],
)

http_file(
    name = "google_java_format_jar",
    downloaded_file_path = "google-java-format.jar",
    sha256 = "2a5273633c2b1c1607b60b5e17671e6a535dedbcdef74a127629a027297ab7c7",
    urls = ["https://github.com/google/google-java-format/releases/download/v1.11.0/google-java-format-1.11.0-all-deps.jar"],
)
