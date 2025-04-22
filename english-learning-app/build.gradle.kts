plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") version "4.4.2" // You can choose to remove the version if it's already in toml
}

android {
    namespace = "com.example.mobile_vocab_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobile_vocab_project"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    // UI & core
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.core:core:1.9.0") // for NotificationCompat
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // Firebase (use BoM to manage versions)
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-firestore")

    // WorkManager
    implementation("androidx.work:work-runtime:2.10.0")

    // Utilities
    implementation("com.google.guava:guava:31.1-android")
}

apply(plugin = "com.google.gms.google-services")
