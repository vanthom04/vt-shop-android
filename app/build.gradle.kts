plugins {
    id("com.android.application")
}

android {
    namespace = "com.vanthom04.vtshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vanthom04.vtshop"
        minSdk = 20
        targetSdk = 33
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // AH Bottom Navigation View
    implementation("com.aurelhubert:ahbottomnavigation:2.3.4")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // CircleIndicator
    implementation("me.relex:circleindicator:2.1.6")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // okHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // volley
    implementation("com.android.volley:volley:1.2.1")

    // retrofit
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // socket.io
    implementation ("io.socket:socket.io-client:2.0.0")
}