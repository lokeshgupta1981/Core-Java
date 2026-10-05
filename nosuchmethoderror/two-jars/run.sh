#!/usr/bin/env bash
# Reproduces NoSuchMethodError with plain javac and java (no Maven).
# Run from this folder: ./run.sh
set -u
OUT=out
rm -rf "$OUT" && mkdir -p "$OUT/v1" "$OUT/v2" "$OUT/app-v2" "$OUT/app-v1"

# 1. Build both versions of the library
javac -d "$OUT/v1" ../greeter-v1/src/main/java/com/howtodoinjava/greeter/Greeter.java
javac -d "$OUT/v2" ../greeter-v2/src/main/java/com/howtodoinjava/greeter/Greeter.java
jar --create --file "$OUT/greeter-1.0.0.jar" -C "$OUT/v1" .
jar --create --file "$OUT/greeter-2.0.0.jar" -C "$OUT/v2" .

# 2. Compile GreetApp against 2.0.0 and CountApp against 1.0.0
javac -cp "$OUT/greeter-2.0.0.jar" -d "$OUT/app-v2" src/com/howtodoinjava/app/GreetApp.java
javac -cp "$OUT/greeter-1.0.0.jar" -d "$OUT/app-v1" src/com/howtodoinjava/app/CountApp.java

echo "== GreetApp with greeter-2.0.0.jar (works)"
java -cp "$OUT/app-v2:$OUT/greeter-2.0.0.jar" com.howtodoinjava.app.GreetApp

echo "== GreetApp with greeter-1.0.0.jar (NoSuchMethodError)"
java -cp "$OUT/app-v2:$OUT/greeter-1.0.0.jar" com.howtodoinjava.app.GreetApp

echo "== CountApp with greeter-2.0.0.jar (return type changed, NoSuchMethodError)"
java -cp "$OUT/app-v1:$OUT/greeter-2.0.0.jar" com.howtodoinjava.app.CountApp

echo "== Which jar did Greeter come from?"
java -verbose:class -cp "$OUT/app-v2:$OUT/greeter-1.0.0.jar" com.howtodoinjava.app.GreetApp 2>/dev/null | grep "greeter.Greeter "

echo "== Methods in each jar"
javap -cp "$OUT/greeter-1.0.0.jar" com.howtodoinjava.greeter.Greeter
javap -cp "$OUT/greeter-2.0.0.jar" com.howtodoinjava.greeter.Greeter
