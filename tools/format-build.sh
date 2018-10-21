#!/usr/bin/env bash

bazel run //thirdparty:buildifier -- -showlog -mode=fix $(find . -type f \( -iname WORKSPACE -or -iname BUILD -or -iname BUILD.bazel \))
