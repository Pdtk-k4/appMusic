plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
    signingConfigs {
        create("my_config") {
            storeFile =
                file("D:\\Users\\DungDrum\\Documents\\AndroidStudioProjects\\DahitaMusic\\KeyStore.jks")
            storePassword = "123456@Ab"
            keyAlias = "Dahita"
            keyPassword = "123456@Ab"
        }
    }
    namespace = "com.example.dahitamusic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dahitamusic"
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
            signingConfig = signingConfigs.getByName("my_config")
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
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-messaging")

    implementation ("com.squareup.picasso:picasso:2.8")

    implementation ("me.relex:circleindicator:2.1.6")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.google.android.exoplayer:exoplayer:2.18.1")

    implementation ("androidx.palette:palette:1.0.0")
}