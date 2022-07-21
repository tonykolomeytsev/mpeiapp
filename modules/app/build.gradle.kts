plugins {
    id("com.android.application")
    id("mpeix.android.base")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {

    defaultConfig {
        applicationId = "kekmech.ru.mpeiapp"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
        }
        configureEach {
            resValue("string", "google_maps_api_key", project.property("mpeiapp_google_maps_api_key").toString())
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugaring)
    implementation(libs.androidx.lifecycleCommonJava8)

    // Koin for Kotlin Android
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.timber)

    implementation(libs.vivid.elmslie.core)
    implementation(libs.vivid.elmslie.android)

    // Android widgets
    implementation(libs.androidx.viewPager2)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.google.material)
    implementation(libs.google.maps)
    implementation("com.github.jetradarmobile:android-snowfall:1.2.0")

    // Firebase
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)

    implementation(libs.rx.java)
    implementation(libs.rx.android)

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logger)

    implementation(project(":feature_onboarding"))
    implementation(project(":feature_dashboard"))
    implementation(project(":feature_schedule"))
    implementation(project(":feature_app_settings"))
    implementation(project(":feature_map"))
    implementation(project(":feature_force_update"))
    implementation(project(":feature_bars"))
    implementation(project(":feature_notes"))
    implementation(project(":feature_search"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_app_settings"))
    implementation(project(":domain_force_update"))
    implementation(project(":domain_bars"))
    implementation(project(":domain_search"))
    implementation(project(":domain_main_screen"))
    implementation(project(":domain_onboarding"))
    implementation(project(":domain_map"))

    implementation(project(":coreui"))
    implementation(project(":common_navigation"))
    implementation(project(":common_android"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_mvi"))
//    implementation project(":common_webview")
    implementation(project(":common_network"))
    implementation(project(":common_cache"))
    implementation(project(":common_app_database"))
    implementation(project(":common_analytics"))
    implementation(project(":common_feature_toggles"))
    implementation(project(":common_shared_preferences"))
//    implementation project(":common_images")
}
