// https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))

    // logger
    api("org.slf4j:slf4j-api:[2.0,3.0)!!2.0.9")

    // use jupiter engine https://junit.org/junit5/docs/current/user-guide/#running-tests-build-gradle-engines-configure
    testImplementation("org.junit.jupiter:junit-jupiter:[5.9, 6.0)!!5.9.3")
    testImplementation("io.mockk:mockk:[1.13,2.0)")
}

kotlin {
    jvmToolchain(21) // update in Dockerfile too
    compilerOptions {
        allWarningsAsErrors = true
    }
}

tasks.withType<Test> {
    // use JUnit5
    useJUnitPlatform()
    // enable logging exceptions
    testLogging {
        events(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}
