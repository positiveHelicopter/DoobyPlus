package com.positiveHelicopter.doobyplus

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
//import org.gradle.api.provider.Provider
//import org.gradle.kotlin.dsl.assign
//import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
//import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
            add("implementation", libs.findLibrary("androidx-activity-compose").get())
            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-compose-viewmodel").get())
            add("implementation", libs.findLibrary("androidx-compose-hilt-navigation").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }

//    extensions.configure<ComposeCompilerGradlePluginExtension> {
//        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
//        fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
//            rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
//        }.map { it.dir(dir) }
//
//        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
//            .relativeToRootProject("compose-metrics")
//            .let(metricsDestination::set)
//
//        project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
//            .relativeToRootProject("compose-reports")
//            .let(reportsDestination::set)
//
//        stabilityConfigurationFile = rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
//
//        enableStrongSkippingMode = true
//    }
}