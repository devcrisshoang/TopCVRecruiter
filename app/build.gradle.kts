plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.topcvrecruiter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.topcvrecruiter"
        minSdk = 29
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.cast.framework)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.circleimageview)
    //implementation(kotlin("script-runtime"))
    implementation ("com.google.android.gms:play-services-maps:18.0.0")
    // Thêm vào build.gradle
    implementation (libs.glide)
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    // Google Play Services
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:latest.release")

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.okhttp)
    implementation (libs.adapter.rxjava3)
    implementation (libs.rxandroid)
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

}