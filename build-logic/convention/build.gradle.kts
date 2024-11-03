import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.positiveHelicopter.doobyplus.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "doobyplus.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("hilt") {
            id = "doobyplus.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("compose") {
            id = "doobyplus.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("firebase") {
            id = "doobyplus.firebase"
            implementationClass = "FireBaseConventionPlugin"
        }
        register("room") {
            id = "doobyplus.room"
            implementationClass = "RoomConventionPlugin"
        }
    }
}