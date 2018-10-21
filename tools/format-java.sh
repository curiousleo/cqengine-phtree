#!/usr/bin/env bash

bazel run //thirdparty:java_format -- --replace $(find $PWD -type f \( -iname '*.java' \))
