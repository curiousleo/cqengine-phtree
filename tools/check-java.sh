#!/usr/bin/env bash

set -e -u -x -o pipefail
cd "$(dirname "$0")/../"

bazel run //thirdparty:java_format -- --dry-run --set-exit-if-changed $(find $PWD -type f \( -iname '*.java' \))
