plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "io.flamingock"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

gradlePlugin {
    website.set("https://www.flamingock.io/")
    vcsUrl.set("https://github.com/flamingock/flamingock-gradle-plugin")

    plugins {
        create("flamingock") {
            id = "io.flamingock"
            implementationClass = "io.flamingock.gradle.FlamingockPlugin"
            displayName = "Flamingock Gradle Plugin"
            description = "Gradle plugin to configure Flamingock with zero boilerplate"
            tags.set(
                listOf(
                    "flamingock",
                    "change-as-code",
                    "external-systems",
                    "application-evolution",
                    "gradle-plugin"
                )
            )
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set("Flamingock Gradle Plugin")
                description.set("Gradle plugin to configure Flamingock with zero boilerplate")
                url.set("https://www.flamingock.io/")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
            }
        }
    }
}
