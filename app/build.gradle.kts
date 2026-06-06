plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.guide.smartcity"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.guide.smartcity"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.activity:activity:1.13.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.13.0")
    // Retrofit for network calls
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
// Glide for loading images from URL

    // Import the Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))

    // Add the library for Firebase Authentication (for Login/Sign-up)
    implementation("com.google.firebase:firebase-auth")

    // Add the library for Cloud Firestore (to save Jalandhar places)
    implementation("com.google.firebase:firebase-firestore")

}