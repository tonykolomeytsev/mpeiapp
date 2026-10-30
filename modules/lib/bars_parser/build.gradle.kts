// The parser of the HTML pages of the BARS student portal (bars.mpei.ru). Pure Kotlin, no Android and no network:
// it takes the page text and returns data classes, so the same code runs on Android, on iOS and in JVM unit tests.
plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ksoup)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
