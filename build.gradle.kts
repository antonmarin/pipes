
plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("kotlin-module")
}

group = "ru.antonmarin"

repositories {
    mavenCentral()
}

dependencies {
    // logger
    runtimeOnly("ch.qos.logback:logback-classic:1.4.12")

    // mapping
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.2") // map java time api

    //http client
    testImplementation ("org.testcontainers:mockserver:1.19.7")
    testImplementation("org.mock-server:mockserver-client-java:5.15.0")
    //rss
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.0")

    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation ("org.testcontainers:testcontainers:1.19.7")
}

// releasing
tasks.build {
    dependsOn("shadowJar")
}
tasks.shadowJar {
    archiveBaseName.set("application")
    archiveVersion.set("") // Remove app version
    archiveClassifier.set("") // Remove the default '-all' classifier
    manifest {
        // Set entrypoint
        attributes["Main-Class"] = "ru.antonmarin.autoget.MainKt"
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}
