#!/usr/bin/env bash

set -e -u -x -o pipefail
cd "$(dirname "$0")/../"

bazel run //thirdparty:buildifier -- -mode=fix $(find . -type f \( -iname WORKSPACE -or -iname BUILD -or -iname BUILD.bazel \))
