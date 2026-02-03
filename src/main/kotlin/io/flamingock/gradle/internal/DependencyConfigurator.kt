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
package io.flamingock.gradle.internal

import io.flamingock.gradle.FlamingockExtension
import org.gradle.api.Project
import org.gradle.api.logging.Logger

/**
 * Configures Flamingock dependencies based on the extension settings.
 */
internal object DependencyConfigurator {

    fun configure(project: Project, extension: FlamingockExtension, version: String, logger: Logger) {
        val group = FlamingockConstants.GROUP
        val dependencies = project.dependencies

        // Log the effective edition
        when {
            extension.isMinimalEnabled -> {
                logger.info("Flamingock: Minimal edition enabled (processor only)")
            }
            extension.isCloudEnabled -> {
                val qualifier = if (extension.selectedEdition == null) "(default)" else "(explicit)"
                logger.info("Flamingock: Cloud edition enabled $qualifier")
            }
            else -> {
                logger.info("Flamingock: Community edition enabled")
            }
        }

        // Always add the annotation processor
        dependencies.add(
            "annotationProcessor",
            "$group:flamingock-processor:$version"
        )
        logger.info("Flamingock: Added annotation processor (flamingock-processor)")

        // Cloud edition dependencies
        if (extension.isCloudEnabled) {
            // Add BOM for version management
            dependencies.add(
                "implementation",
                dependencies.platform("$group:flamingock-cloud-bom:$version")
            )
            // Add core cloud library (version managed by BOM)
            dependencies.add(
                "implementation",
                "$group:flamingock-cloud"
            )
        }

        // Community edition dependencies
        if (extension.isCommunityEnabled) {
            // Add BOM for version management
            dependencies.add(
                "implementation",
                dependencies.platform("$group:flamingock-community-bom:$version")
            )
            // Add core community library (version managed by BOM)
            dependencies.add(
                "implementation",
                "$group:flamingock-community"
            )
        }

        // Mongock support
        if (extension.isMongockEnabled) {
            dependencies.add(
                "implementation",
                "$group:mongock-support"
            )
            dependencies.add(
                "annotationProcessor",
                "$group:mongock-support:$version"
            )
            logger.info("Flamingock: Mongock support enabled")
        }

        // Spring Boot integration
        if (extension.isSpringbootEnabled) {
            dependencies.add(
                "implementation",
                "$group:flamingock-springboot-integration"
            )
            dependencies.add(
                "testImplementation",
                "$group:flamingock-springboot-test-support"
            )
            logger.info("Flamingock: Spring Boot integration enabled")
        }

        // GraalVM support
        if (extension.isGraalvmEnabled) {
            dependencies.add(
                "implementation",
                "$group:flamingock-graalvm"
            )
            logger.info("Flamingock: GraalVM native image support enabled")
        }
    }
}
