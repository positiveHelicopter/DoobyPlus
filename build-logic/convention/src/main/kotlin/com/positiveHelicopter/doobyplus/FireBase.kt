package com.positiveHelicopter.doobyplus

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFirebase(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        dependencies {
            val bom = libs.findLibrary("firebase-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("firebase-cloud-messaging").get())
        }
    }
}