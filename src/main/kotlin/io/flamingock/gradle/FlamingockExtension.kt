/*
 * Copyright 2024 Flamingock (https://www.flamingock.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.flamingock.gradle

/**
 * Extension for configuring Flamingock in a Gradle project.
 *
 * Usage:
 * ```
 * flamingock {
 *     community()
 *     mongockSupport()
 *     springboot()
 *     graalvm()
 *     templates(SQL, MONGODB)
 * }
 * ```
 */
open class FlamingockExtension {

    internal var isCommunityEnabled: Boolean = false
        private set

    internal var isMongockEnabled: Boolean = false
        private set

    internal var isSpringbootEnabled: Boolean = false
        private set

    internal var isGraalvmEnabled: Boolean = false
        private set

    internal var templates: Set<FlamingockTemplate> = emptySet()
        private set

    /**
     * Enables the Community edition of Flamingock.
     *
     * This is REQUIRED. The plugin will fail if this method is not called.
     *
     * Adds:
     * - `implementation(platform("io.flamingock:flamingock-community-bom"))`
     * - `implementation("io.flamingock:flamingock-community")`
     */
    fun community() {
        isCommunityEnabled = true
    }

    /**
     * Enables Mongock compatibility for migrating from Mongock to Flamingock.
     *
     * Adds:
     * - `implementation("io.flamingock:mongock-support")`
     * - `annotationProcessor("io.flamingock:mongock-support")`
     */
    fun mongock() {
        isMongockEnabled = true
    }

    /**
     * Enables Spring Boot integration.
     *
     * Adds:
     * - `implementation("io.flamingock:flamingock-springboot-integration")`
     *
     * Also switches test support from `flamingock-test-support` to
     * `flamingock-springboot-test-support` (which transitively includes the basic one).
     */
    fun springboot() {
        isSpringbootEnabled = true
    }

    /**
     * Enables GraalVM native image support.
     *
     * Adds:
     * - `implementation("io.flamingock:flamingock-graalvm")`
     */
    fun graalvm() {
        isGraalvmEnabled = true
    }

    /**
     * Adds Java template dependencies.
     *
     * Adds for each template:
     * - `implementation("io.flamingock:<template-artifact>:<template-version>")`
     */
    fun templates(vararg templates: FlamingockTemplate) {
        this.templates = this.templates + templates.toSet()
    }
}
