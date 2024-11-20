plugins {
    alias(libs.plugins.doobyplus.android.application)
    alias(libs.plugins.doobyplus.hilt)
    alias(libs.plugins.doobyplus.compose)
    alias(libs.plugins.doobyplus.firebase)
    alias(libs.plugins.doobyplus.room)
}

android {
    namespace = "com.positiveHelicopter.doobyplus"

    defaultConfig {
        applicationId = "com.positiveHelicopter.doobyplus"
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.network.okhttp)
    implementation(libs.coil.kt.gif)
    implementation(libs.custom.tab.browser)
    implementation(libs.proto.preference.datastore)
    implementation(libs.work.runtime)
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    implementation(libs.jsoup)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}