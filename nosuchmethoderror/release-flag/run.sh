#!/usr/bin/env bash
# Shows why --release matters. Needs JDK 25 as java/javac on the PATH and a JDK 21 in JDK21_HOME.
# Run from this folder: JDK21_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ./run.sh
set -u
rm -rf out && mkdir -p out/target21 out/release21

echo "== javac -source 21 -target 21 (compiles against the JDK 25 class library)"
javac -source 21 -target 21 -d out/target21 src/com/howtodoinjava/release/ReaderDemo.java

echo "== Run on JDK 25"
java -cp out/target21 com.howtodoinjava.release.ReaderDemo

echo "== Run on JDK 21"
"$JDK21_HOME/bin/java" -cp out/target21 com.howtodoinjava.release.ReaderDemo

echo "== javac --release 21 (compiles against the JDK 21 API, fails early)"
javac --release 21 -d out/release21 src/com/howtodoinjava/release/ReaderDemo.java
