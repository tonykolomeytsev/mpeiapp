plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnlyAar(libs.androidx.fragment)

    implementation(libs.google.gson)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
}