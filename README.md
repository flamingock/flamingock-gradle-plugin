# Flamingock Gradle Plugin

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/io.flamingock)](https://plugins.gradle.org/plugin/io.flamingock)

**Zero-boilerplate Flamingock configuration for Gradle projects.**

This plugin simplifies Flamingock setup by automatically adding the required dependencies and annotation processors to your project.

> **Flamingock** is a Change-as-Code platform for audited, synchronized evolution of distributed systems.
> Learn more at [flamingock.io](https://www.flamingock.io/) | [Documentation](https://docs.flamingock.io/) | [Main Project](https://github.com/flamingock/flamingock-java)

---

## Requirements

- **Gradle** 7.4+
- **Java** 8+

---

## Installation

Add the plugin to your `build.gradle.kts`:

```kotlin
plugins {
    id("io.flamingock") version "1.0.0-beta.7"
}

flamingock {
    community()
}
```

Or in `build.gradle` (Groovy):

```groovy
plugins {
    id 'io.flamingock' version '1.0.0-beta.7'
}

flamingock {
    community()
}
```

---

## Configuration

```kotlin
flamingock {
    community()    // Required - enables Community edition
    mongock()      // Optional - Mongock migration support
    springboot()   // Optional - Spring Boot integration
    graalvm()      // Optional - GraalVM native image support
}
```

### Configuration Options

| Method | Required | Description |
|--------|----------|-------------|
| `community()` | Yes | Enables the Flamingock Community edition |
| `mongock()` | No | Adds support for migrating from Mongock |
| `springboot()` | No | Adds Spring Boot integration and test support |
| `graalvm()` | No | Adds GraalVM native image support |

---

## What Gets Added

The plugin automatically adds dependencies based on your configuration:

### Always Added
```kotlin
annotationProcessor("io.flamingock:flamingock-processor:1.0.0-beta.7")
```

### `community()`
```kotlin
implementation(platform("io.flamingock:flamingock-community-bom:1.0.0-beta.7"))
implementation("io.flamingock:flamingock-community")
```

### `mongock()`
```kotlin
implementation("io.flamingock:mongock-support")
annotationProcessor("io.flamingock:mongock-support")
```

### `springboot()`
```kotlin
implementation("io.flamingock:flamingock-springboot-integration")
testImplementation("io.flamingock:flamingock-springboot-test-support")
```

### `graalvm()`
```kotlin
implementation("io.flamingock:flamingock-graalvm")
```

---

## Example

A typical Spring Boot project with MongoDB:

```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.flamingock") version "1.0.0-beta.7"
}

flamingock {
    community()
    springboot()
}

dependencies {
    // Your MongoDB driver
    implementation("org.mongodb:mongodb-driver-sync:5.0.0")

    // Your Flamingock audit store (choose one)
    implementation("io.flamingock:flamingock-auditstore-mongodb-sync")
}
```

Then write your changes:

```java
@ChangeUnit(id = "create-users-index", order = "001", author = "dev")
public class CreateUsersIndex {

    @Execution
    public void execute(MongoDatabase database) {
        database.getCollection("users")
            .createIndex(Indexes.ascending("email"));
    }

    @RollbackExecution
    public void rollback(MongoDatabase database) {
        database.getCollection("users")
            .dropIndex(Indexes.ascending("email"));
    }
}
```

---

## Links

- [Flamingock Website](https://www.flamingock.io/)
- [Documentation](https://docs.flamingock.io/)
- [Main Project (flamingock-java)](https://github.com/flamingock/flamingock-java)
- [Report Issues](https://github.com/flamingock/flamingock-gradle-plugin/issues)

---

## License

```
Copyright 2024 Flamingock

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
