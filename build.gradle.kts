plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.0.21"
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}

android {
    namespace = "com.equiposeis"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.equiposeis"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    kotlinOptions {
        jvmTarget = "17"
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
}

dependencies {
    val roomVersion = "2.7.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
}
