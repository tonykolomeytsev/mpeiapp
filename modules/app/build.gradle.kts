@file:Suppress("UnstableApiUsage")

plugins {
    id("mpeix.android.application")
    id("mpeix.android.signing")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply(from = "${rootProject.projectDir}/gradle/ci.gradle")

android {
    namespace = "kekmech.ru.mpeiapp"

    defaultConfig {
        applicationId = "kekmech.ru.mpeiapp"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        configureEach {
            resValue(
                "string",
                "google_maps_api_key",
                project.property("mpeiapp_google_maps_api_key").toString()
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        // This is to make it possible to use Ktor (mock_server) on android
        // https://github.com/gnarea/minimal-ktor-server-android/blob/master/app/build.gradle
        resources.excludes += setOf("META-INF/**", "**/attach_hotspot_windows.dll")
    }
}

dependencies {
    implementation("com.github.jetradarmobile:android-snowfall:1.2.0")
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.coordinatorLayout)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.datastore.preferences.android)
    implementation(libs.androidx.lifecycleCommonJava8)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.viewPager2)
    implementation(libs.appyx.core)
    implementation(libs.compose.activity)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.rmr.mapmemory.core)
    implementation(libs.rx.android)
    implementation(libs.rx.java)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logger)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)
    implementation(libs.vivid.elmslie.android)
    implementation(libs.vivid.elmslie.core)

    implementation(project(":feature_app_settings_impl"))
    implementation(project(":feature_bars_impl"))
    implementation(project(":feature_dashboard_impl"))
    implementation(project(":feature_app_update_impl"))
    implementation(project(":feature_map_impl"))
    implementation(project(":feature_notes_impl"))
    implementation(project(":feature_onboarding_impl"))
    implementation(project(":feature_schedule_impl"))
    implementation(project(":feature_search_impl"))

    implementation(project(":feature_app_settings_api"))
    implementation(project(":feature_app_settings_impl"))
    implementation(project(":feature_bars_api"))
    implementation(project(":feature_dashboard_api"))
    implementation(project(":feature_favorite_schedule_api"))
    implementation(project(":feature_favorite_schedule_impl"))
    implementation(project(":feature_app_update_api"))
    implementation(project(":feature_contributors_api"))
    implementation(project(":feature_contributors_impl"))
    implementation(project(":feature_main_screen_api"))
    implementation(project(":feature_map_api"))
    implementation(project(":feature_notes_api"))
    implementation(project(":feature_onboarding_api"))
    implementation(project(":feature_schedule_api"))
    implementation(project(":feature_search_api"))

    implementation(project(":ext_koin"))

    implementation(project(":library_analytics_android"))
    implementation(project(":ext_android"))
    implementation(project(":library_app_database_api"))
    implementation(project(":library_app_database"))
    implementation(project(":library_app_lifecycle"))
    implementation(project(":library_coroutines"))
    implementation(project(":library_app_info"))
    implementation(project(":library_elm"))
    implementation(project(":library_feature_toggles"))
    implementation(project(":library_navigation"))
    implementation(project(":library_network"))

    implementation(project(":ext_kotlin"))
    implementation(project(":ext_shared_preferences"))

    implementation(project(":library_persistent_cache"))

    implementation(project(":ui_theme"))

    implementation(project(":coreui"))
    implementation(project(":icons"))
    implementation(project(":res_strings"))

    debugImplementation(project(":mock_server"))
    debugImplementation(project(":debug_menu"))
    debugImplementation(project(":library_chucker"))

    testImplementation(libs.koin.test)
    testImplementation(libs.kotest.runner)
}
