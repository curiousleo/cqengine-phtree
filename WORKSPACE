load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_file")

# Build dependencies

maven_jar(
    name = "com_googlecode_cqengine",
    artifact = "com.googlecode.cqengine:cqengine:3.0.0",
    sha1 = "bb73d6730d133596e43ed95adfba3705276ab47c",
    sha1_src = "33aa3172c5352ff975cf4b781b6f86e672377b96",
)

maven_jar(
    name = "com_googlecode_concurrent_trees",
    artifact = "com.googlecode.concurrent-trees:concurrent-trees:2.6.1",
    sha1 = "9b647240522ab67c003de9b6702ca81ac0c15efc",
    sha1_src = "4c2e816b8fa6c525c1cb235c840dfb6b0a320db4",
)

maven_jar(
    name = "ch_ethz_globis_phtree",
    artifact = "ch.ethz.globis.phtree:phtree:2.0.1",
    sha1 = "f7d9e115656309a5b9d6276cac00fa6751d0464b",
    sha1_src = "a7686af548d4f9291f3e9e842f292b822aa2522e",
)

# Test dependencies

maven_jar(
    name = "junit_junit4",
    artifact = "junit:junit:4.12",
    sha1 = "2973d150c0dc1fefe998f834810d68f278ea58ec",
    sha1_src = "a6c32b40bf3d76eca54e3c601e5d1470c86fcdfa",
)

maven_jar(
    name = "com_google_truth",
    artifact = "com.google.truth:truth:0.42",
    sha1 = "b5768f644b114e6cf5c3962c2ebcb072f788dcbb",
    sha1_src = "7e72d98ff8a87f4f62f54850ff1387112eb8b917",
)

maven_jar(
    name = "org_hamcrest_core",
    artifact = "org.hamcrest:hamcrest-core:1.3",
    sha1 = "42a25dc3219429f0e5d060061f71acb49bf010a0",
    sha1_src = "1dc37250fbc78e23a65a67fbbaf71d2e9cbc3c0b",
)

maven_jar(
    name = "com_google_guava",
    artifact = "com.google.guava:guava:26.0-jre",
    sha1 = "6a806eff209f36f635f943e16d97491f00f6bfab",
    sha1_src = "2e35b51c759ae1a6edbb967b65c525105536d382",
)

# Benchmark dependencies

JMH_VERSION = "1.21"

maven_jar(
    name = "org_openjdk_jmh_core",
    artifact = "org.openjdk.jmh:jmh-core:" + JMH_VERSION,
    sha1 = "442447101f63074c61063858033fbfde8a076873",
    sha1_src = "a6fe84788bf8cf762b0e561bf48774c2ea74e370",
)

maven_jar(
    name = "org_openjdk_jmh_generator_annprocess",
    artifact = "org.openjdk.jmh:jmh-generator-annprocess:" + JMH_VERSION,
    sha1 = "7aac374614a8a76cad16b91f1a4419d31a7dcda3",
    sha1_src = "fb48e2a97df95f8b9dced54a1a37749d2a64d2ae",
)

maven_jar(
    name = "org_openjdk_jmh_generator_reflection",
    artifact = "org.openjdk.jmh:jmh-generator-reflection:" + JMH_VERSION,
    sha1 = "ed5a2bdca04daafac41c53cf82c3b9733fd91e89",
    sha1_src = "e51461822eb497f6e01ff0624b4d247a9b5aa1ce",
)

maven_jar(
    name = "org_openjdk_jmh_generator_bytecode",
    artifact = "org.openjdk.jmh:jmh-generator-bytecode:" + JMH_VERSION,
    sha1 = "6a52cbbd7f5e2cf7a0163984241750cdd6cb1257",
    sha1_src = "92ca8e5cfc3b660ae981265700ae5d833adef0aa",
)

maven_jar(
    name = "org_apache_commons_math3",
    artifact = "org.apache.commons:commons-math3:3.6.1",
    sha1 = "e4ba98f1d4b3c80ec46392f25e094a6a2e58fcbf",
    sha1_src = "8fab23986ea8886af34818daf32a718e81dc98ba",
)

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
