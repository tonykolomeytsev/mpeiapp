plugins {
    id("mpeix.kotlin.lib")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.androidx.fragment)
    compileOnlyAar(libs.timber)

    implementation(libs.google.gson)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.rmr.mapmemory.core)
    implementation(libs.rmr.mapmemory.coroutines)
    implementation(libs.squareup.retrofit)
    implementation(libs.androidx.datastore.preferences.core)

    implementation(project(":common_annotations"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_network"))
    implementation(project(":ext_shared_preferences"))
}
