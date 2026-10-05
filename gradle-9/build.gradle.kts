plugins {
    application
}

group = "com.howtodoinjava"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")   // required in Gradle 9
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)                // compile and test with JDK 25
    }
}

application {
    mainClass = "com.howtodoinjava.gradle9.FruitApp"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// Gradle 9: Project.exec() is removed, so we run the command through a provider
val gitHash = providers.exec {
    commandLine("git", "rev-parse", "--short", "HEAD")
    isIgnoreExitValue = true                                     // no failure outside a Git repo
}.standardOutput.asText.map { it.trim().ifEmpty { "unknown" } }

tasks.register("printGitHash") {
    val hash = gitHash
    doLast {
        println("Git commit: " + hash.get())
    }
}
