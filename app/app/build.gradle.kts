plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "edu.fit.schedulo.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.fit.schedulo.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Apache Commons Collections
    implementation(libs.collections)

    // Jackson DataBind
    implementation(libs.jackson.databind)
    implementation(libs.jackson.databind.jsr310)

    // Jsoup
    implementation(libs.jsoup)

}