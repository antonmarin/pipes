// https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")/* version "1.9.22"*/
}

repositories {
    mavenCentral()
}

dependencies {
    // logger
    api("org.slf4j:slf4j-api:[2.0,3.0)!!2.0.9")

    // use jupiter engine https://junit.org/junit5/docs/current/user-guide/#running-tests-build-gradle-engines-configure
    testImplementation("org.junit.jupiter:junit-jupiter:[5.9, 6.0)!!5.9.3")
    testImplementation("io.mockk:mockk:[1.13,2.0)")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
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
