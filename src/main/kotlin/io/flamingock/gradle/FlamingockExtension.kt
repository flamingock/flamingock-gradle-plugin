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

import org.gradle.api.GradleException

/**
 * Extension for configuring Flamingock in a Gradle project.
 *
 * Usage:
 * ```
 * flamingock {
 *     cloud()       // default - Cloud edition (explicit)
 *     community()   // Community edition
 *     mongock()     // optional
 *     springboot()  // optional
 *     graalvm()     // optional
 * }
 * ```
 */
open class FlamingockExtension {

    enum class Edition {
        COMMUNITY,
        CLOUD
    }

    internal var selectedEdition: Edition? = null
        private set

    internal val effectiveEdition: Edition
        get() = selectedEdition ?: Edition.CLOUD

    internal val isCommunityEnabled: Boolean
        get() = effectiveEdition == Edition.COMMUNITY

    internal val isCloudEnabled: Boolean
        get() = effectiveEdition == Edition.CLOUD

    internal var isMongockEnabled: Boolean = false
        private set

    internal var isSpringbootEnabled: Boolean = false
        private set

    internal var isGraalvmEnabled: Boolean = false
        private set

    /**
     * Enables the Community edition of Flamingock.
     *
     * Mutually exclusive with [cloud].
     *
     * Adds:
     * - `implementation(platform("io.flamingock:flamingock-community-bom"))`
     * - `implementation("io.flamingock:flamingock-community")`
     */
    fun community() {
        if (selectedEdition == Edition.CLOUD) {
            throw GradleException(
                """
                |
                |FLAMINGOCK CONFIGURATION ERROR
                |
                |Cannot enable both Community and Cloud editions.
                |
                |The editions are mutually exclusive. Please choose one:
                |
                |flamingock {
                |    cloud()    // Cloud edition
                |}
                |
                |or
                |
                |flamingock {
                |    community()    // Community edition (default)
                |}
                |
                """.trimMargin()
            )
        }
        selectedEdition = Edition.COMMUNITY
    }

    /**
     * Enables the Cloud edition of Flamingock.
     *
     * This is the default edition — if neither [cloud] nor [community] is called,
     * Cloud is selected automatically.
     *
     * Mutually exclusive with [community].
     *
     * Adds:
     * - `implementation(platform("io.flamingock:flamingock-cloud-bom"))`
     * - `implementation("io.flamingock:flamingock-cloud")`
     */
    fun cloud() {
        if (selectedEdition == Edition.COMMUNITY) {
            throw GradleException(
                """
                |
                |FLAMINGOCK CONFIGURATION ERROR
                |
                |Cannot enable both Community and Cloud editions.
                |
                |The editions are mutually exclusive. Please choose one:
                |
                |flamingock {
                |    cloud()    // Cloud edition
                |}
                |
                |or
                |
                |flamingock {
                |    community()    // Community edition (default)
                |}
                |
                """.trimMargin()
            )
        }
        selectedEdition = Edition.CLOUD
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
     * - `testImplementation("io.flamingock:flamingock-springboot-test-support")`
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
}
