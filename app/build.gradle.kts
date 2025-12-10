plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt")
}

android {
    namespace = "com.example.myfintech"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myfintech"
        minSdk = 30
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
// Keep these standard dependencies from your Version Catalog
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- LIBRARIES ADDED USING SIMPLE DOUBLE QUOTES ---

    // For ViewModel integration with Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // For Navigation with Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // For the standard Material Design icons (like the Menu icon)
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    // For calculating window size classes for responsive UI
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.play.services.mlkit.text.recognition)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")

    // ML Kit Text Recognition
    implementation("com.google.mlkit:text-recognition:16.0.0")

    // --- Test and Debug Dependencies ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}