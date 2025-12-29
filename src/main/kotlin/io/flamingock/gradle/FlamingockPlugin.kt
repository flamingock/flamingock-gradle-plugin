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

import io.flamingock.gradle.internal.DependencyConfigurator
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Flamingock Gradle Plugin.
 *
 * Simplifies Flamingock setup by automatically configuring dependencies
 * and annotation processors.
 *
 * Usage:
 * ```
 * plugins {
 *     id("io.flamingock") version "1.0.0-beta.7"
 * }
 *
 * flamingock {
 *     community()
 *     mongock()     // optional
 *     springboot()  // optional
 *     graalvm()     // optional
 * }
 * ```
 */
class FlamingockPlugin : Plugin<Project> {

    companion object {
        const val EXTENSION_NAME = "flamingock"
        const val PLUGIN_VERSION = "1.0.0-beta.7"
    }

    override fun apply(project: Project) {
        // Apply java plugin if not already applied
        if (!project.plugins.hasPlugin("java")) {
            project.plugins.apply("java")
        }

        // Create and register the extension
        val extension = project.extensions.create(
            EXTENSION_NAME,
            FlamingockExtension::class.java
        )

        // Configure dependencies after project evaluation
        project.afterEvaluate {
            validateConfiguration(extension)
            DependencyConfigurator.configure(project, extension, PLUGIN_VERSION)
        }
    }

    private fun validateConfiguration(extension: FlamingockExtension) {
        if (!extension.isCommunityEnabled) {
            throw GradleException(
                """
                |
                |FLAMINGOCK CONFIGURATION ERROR
                |
                |No Flamingock edition selected.
                |
                |Currently only the Community edition is available.
                |
                |Please configure:
                |
                |flamingock {
                |    community()
                |}
                |
                |Cloud edition support will be available in a future release.
                |
                """.trimMargin()
            )
        }
    }
}
