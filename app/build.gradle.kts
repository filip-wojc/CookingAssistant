plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}


android {
    namespace = "com.cookingassistant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cookingassistant"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            // Set JVM arguments here for unit tests
            it.jvmArgs(
                "--add-opens", "java.base/java.time=ALL-UNNAMED",
                "--add-opens", "java.base/java.lang=ALL-UNNAMED"
            )
        }
    }
}



dependencies {

    implementation(libs.retrofit) // api into interface
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation (libs.androidx.security.crypto) // token encryption
    implementation(libs.converter.gson) // For JSON parsing with Gson
    implementation(libs.okhttp) // networking
    implementation(libs.logging.interceptor) // Optional: for logging HTTP requests
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.espresso.core)
    implementation("com.squareup.moshi:moshi:1.12.0") // DateTimeAdapter
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    val nav_version = "2.8.2"
    implementation("androidx.compose.runtime:runtime:1.7.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.3")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.7.3")
    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit) // Testing
    testImplementation(libs.mockk.mockk) // Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}