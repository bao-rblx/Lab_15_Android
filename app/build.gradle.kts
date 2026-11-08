plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vlu.newsreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vlu.newsreader"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Lifecycle (lifecycleScope)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Local JVM unit tests (real org.json impl, since android.jar only has stubs)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20240303")
}
