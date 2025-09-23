plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.burgerapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.burgerapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
//    implementation("androidx.navigation:navigation-compose-android:2.9.4")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation(libs.hilt.android)
    implementation(libs.firebase.crashlytics.buildtools) // Hilt runtime
    kapt(libs.dagger.hilt.android.compiler) // Use the full alias defined in libs.versions.toml
    implementation(libs.androidx.hilt.navigation.compose.v110alpha01)

    implementation(libs.firebase.analytics)
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    // Firebase Authentication (no version number needed, BoM manages it)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)

    // Google Sign-In
    //implementation("com.google.android.gms:play-services-auth:20.7.0") // stable version

    // Navigation
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)

    // Other dependencies remain unchanged
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}