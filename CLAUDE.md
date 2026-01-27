# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

> **IMPORTANT: This plugin is intentionally minimal**
>
> Do NOT implement:
> - Multiple drivers
> - Template logic
> - Advanced DSL
> - Auto-detection
> - Runtime features
>
> Focus only on:
> - `cloud()` (default edition)
> - `community()`
> - `mongock()`
> - `springboot()`
> - `graalvm()`
> - Dependency wiring

## Project Overview

**flamingock-gradle-plugin** is a Gradle plugin that simplifies the setup and configuration of Flamingock in user projects. It manages all required dependencies, annotation processors, and build configurations automatically.

- **Plugin ID**: `io.flamingock`
- **Current Version**: `1.0.0`
- **Group ID**: `io.flamingock`
- **Target Gradle**: 7.4+
- **Language**: Kotlin

### What This Plugin Does

- Applies `java` plugin if not present
- Adds `flamingock-processor` annotation processor (always)
- Defaults to Cloud edition if no edition is explicitly selected
- Adds Flamingock BOM and core library based on the selected edition
- Adds optional modules based on DSL configuration
- Validates mutual exclusion of editions eagerly (fails fast)

### What This Plugin Does NOT Do

- Run migrations at build time
- Auto-detect frameworks
- Configure database drivers
- Handle templates

## User-Facing DSL

```kotlin
plugins {
    id("io.flamingock") version "1.0.0"
}

flamingock {
    // Cloud is the default - no call needed, or explicit:
    cloud()
    // OR for Community edition:
    community()

    mongock()     // Optional - Mongock migration support
    springboot()  // Optional - Spring Boot integration
    graalvm()     // Optional - GraalVM native image support
}
```

### Edition Selection

| User's DSL | Effective Edition |
|---|---|
| `flamingock { }` (empty or no block) | Cloud (default) |
| `flamingock { cloud() }` | Cloud (explicit) |
| `flamingock { community() }` | Community |
| `flamingock { community(); cloud() }` | **ERROR** (mutually exclusive) |

## Dependency Wiring

| Method                | Configuration         | Artifact                                             |
|-----------------------|-----------------------|------------------------------------------------------|
| Always                | `annotationProcessor` | `io.flamingock:flamingock-processor`                 |
| `cloud()` (default)   | `implementation`      | `platform("io.flamingock:flamingock-cloud-bom")`     |
| `cloud()` (default)   | `implementation`      | `io.flamingock:flamingock-cloud`                     |
| `community()`         | `implementation`      | `platform("io.flamingock:flamingock-community-bom")` |
| `community()`         | `implementation`      | `io.flamingock:flamingock-community`                 |
| `mongock()`           | `implementation`      | `io.flamingock:mongock-support`                      |
| `mongock()`           | `annotationProcessor` | `io.flamingock:mongock-support`                      |
| `springboot()`        | `implementation`      | `io.flamingock:flamingock-springboot-integration`    |
| `springboot()`        | `testImplementation`  | `io.flamingock:flamingock-springboot-test-support`   |
| `graalvm()`           | `implementation`      | `io.flamingock:flamingock-graalvm`                   |

## Project Structure

```
flamingock-gradle-plugin/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── src/main/kotlin/io/flamingock/gradle/
│   ├── FlamingockPlugin.kt           # Main plugin class
│   ├── FlamingockExtension.kt        # DSL extension
│   └── internal/
│       └── DependencyConfigurator.kt # Dependency wiring
└── src/main/resources/META-INF/gradle-plugins/
    └── io.flamingock.properties      # Plugin descriptor
```

## Build Commands

```bash
# Build the plugin
./gradlew build

# Run tests
./gradlew test

# Publish to local Maven repository
./gradlew publishToMavenLocal

# Clean build
./gradlew clean build
```

## Validation

Calling both `community()` and `cloud()` fails immediately with:

```
FLAMINGOCK CONFIGURATION ERROR

Cannot enable both Community and Cloud editions.

The editions are mutually exclusive. Please choose one:

flamingock {
    community()   // Community edition
}

or

flamingock {
    cloud()       // Cloud edition (default)
}
```

## Design Rules

**DO:**
- Use `FlamingockExtension` to store DSL state (edition enum + boolean flags)
- Use `afterEvaluate` to apply configuration
- Use `project.dependencies.add(...)` for dependency wiring
- Apply `java` plugin if not present
- Fail fast with clear error messages on mutual exclusion
- Keep logic simple and explicit

**DO NOT:**
- Auto-detect frameworks
- Use reflection
- Generate code
- Add optional/hidden behavior
- Guess user intent

## License

Apache License 2.0 (same as Flamingock parent project)

All source files must include the Flamingock license header.
