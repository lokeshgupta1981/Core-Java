#!/usr/bin/env bash
# Compiles every example with javac and prints the compiler output.
# Run from the project folder: bash examples/compile-examples.sh
cd "$(dirname "$0")"
OUT=$(mktemp -d)

run() {
  echo "\$ $*"
  "$@" 2>&1
  echo "exit code: $?"
  echo
}

run javac -d "$OUT" AmbiguousList.java
run javac -d "$OUT" OnDemandFix.java
run javac -d "$OUT" AmbiguousDate.java
run javac -d "$OUT" WholeApi.java
run javac --add-modules java.se -d "$OUT" WholeApi.java
run java -cp "$OUT" WholeApi
run java Hello.java
