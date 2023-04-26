plugins {
    id("mpeix.kotlin")
}

dependencies {
    compileOnly(androidJar)
    compileOnlyAar(libs.androidx.fragment)
    compileOnlyAar(libs.timber)

    implementation(libs.google.gson)
    implementation(libs.rx.java)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.rxJava3)

    implementation(project(":common_annotations"))
    implementation(project(":common_cache"))
    implementation(project(":common_di"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_shared_preferences"))
}
