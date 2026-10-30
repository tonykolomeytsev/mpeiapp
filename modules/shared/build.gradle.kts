import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

// The iOS umbrella: the code of the feature modules that is shared with Android is collected here and exported
// to the iOS app (iosApp/) as the static framework "Shared". Android does not depend on this module.
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
    }
}
