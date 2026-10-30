// The network side of the BARS student portal for the code that is shared between Android and iOS: the sign-in with
// the second factor, the cookies of the session and the pages that :lib_bars_parser understands.
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":lib_bars_parser"))
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
