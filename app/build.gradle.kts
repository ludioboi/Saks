plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.saks"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.saks"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.socket.io.client)
    implementation(libs.navigation.ui)
    implementation(libs.coordinatorlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.ssp.android)
    implementation (libs.sdp.android)
    implementation (libs.socket.io.client)
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.recyclerview.selection)
    implementation(libs.journeyapps.zxing.android.embedded)
    implementation(libs.core)
    implementation(libs.okhttp)
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}