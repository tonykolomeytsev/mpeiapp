plugins {
    id("mpeix.android.lib")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)

    implementation(project(":common_android"))
    implementation(project(":common_kotlin"))
    implementation(project(":common_network"))

    implementation(project(":domain_schedule"))
    implementation(project(":domain_schedule_models"))
}
