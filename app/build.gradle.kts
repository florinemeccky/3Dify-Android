plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.a3dify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.a3dify"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
    // Core Android
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.core:core:1.15.0")

    // Material Design 3
    implementation("com.google.android.material:material:1.12.0")

    // Fragment
    implementation("androidx.fragment:fragment:1.8.6")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // Firebase BOM — manages all Firebase version compatibility automatically
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Firebase Auth only — free, no billing required
    implementation("com.google.firebase:firebase-auth")
}