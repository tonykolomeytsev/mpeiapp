import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
    implementation(libs.android.gradle.plugin)
    implementation(libs.detekt.gradle.plugin)
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins.create("AndroidSigningConventionPlugin") {
        id = "mpeix.android.signing"
        displayName = "MpeiX Android Signing Plugin"
        description = "Gradle Plugin for signing release APK and AAB"
        implementationClass = "mpeix.plugins.AndroidSigningConventionPlugin"
    }
    plugins.create("AndroidComposeConventionPlugin") {
        id = "mpeix.android.compose"
        displayName = "MpeiX Compose Android Library Plugin"
        description =
            "Gradle Plugin for setting up android library module with Compose dependencies"
        implementationClass = "mpeix.plugins.AndroidComposeConventionPlugin"
    }
    plugins.create("KotlinAar2JarConventionPlugin") {
        id = "mpeix.kotlin.aar2jar"
        displayName = "Aar2Jar Plugin"
        description = "The Gradle Plugin that transforms AAR dependencies to JAR"
        implementationClass = "mpeix.plugins.KotlinAar2JarConventionPlugin"
    }
    plugins.create("AndroidJarFinderConventionPlugin") {
        id = "mpeix.android-jar-finder"
        displayName = "Android Jar Finder Plugin"
        description = "The Gradle Plugin that finds android.jar in project environment"
        implementationClass = "mpeix.plugins.AndroidJarFinderConventionPlugin"
    }
    plugins.create("KspConventionPlugin") {
        id = "mpeix.ksp"
        displayName = "Kotlin Symbol Processing Support Plugin"
        description = "The Gradle Plugin that adds KSP support to the module"
        implementationClass = "mpeix.plugins.KspConventionPlugin"
    }
    plugins.create("DetektConventionPlugin") {
        id = "mpeix.detekt"
        displayName = "Detekt configuration Plugin"
        description = "The Gradle Plugin that configures Detekt in subproject"
        implementationClass = "mpeix.plugins.DetektConventionPlugin"
    }
    plugins.create("FeatureApiConventionPlugin") {
        id = "mpeix.feature.api"
        displayName = "MpeiX Feature Api Convention"
        description = "The Gradle Plugin that configures Feature Api module"
        implementationClass = "mpeix.plugins.FeatureApiConventionPlugin"
    }
    plugins.create("FeatureImplementationConventionPlugin") {
        id = "mpeix.feature.impl"
        displayName = "MpeiX Feature Implementation Convention"
        description = "The Gradle Plugin that configures Feature Implementation module"
        implementationClass = "mpeix.plugins.FeatureImplementationConventionPlugin"
    }
    plugins.create("KotlinExtensionConventionPlugin") {
        id = "mpeix.kotlin.extension"
        displayName = "MpeiX Kotlin Extension Convention"
        description = "The Gradle Plugin that configures extensions module for jvm libraries"
        implementationClass = "mpeix.plugins.KotlinExtensionConventionPlugin"
    }
    plugins.create("KotlinLibraryConventionPlugin") {
        id = "mpeix.kotlin.library"
        displayName = "MpeiX Kotlin Library Convention"
        description = "The Gradle Plugin that configures Kotlin Library module"
        implementationClass = "mpeix.plugins.KotlinLibraryConventionPlugin"
    }
    plugins.create("AndroidExtensionConventionPlugin") {
        id = "mpeix.android.extension"
        displayName = "MpeiX Android Extension Convention"
        description = "The Gradle Plugin that configures extensions module for android libraries"
        implementationClass = "mpeix.plugins.AndroidExtensionConventionPlugin"
    }
    plugins.create("AndroidLibraryConventionPlugin") {
        id = "mpeix.android.library"
        displayName = "MpeiX Android Library Convention"
        description = "The Gradle Plugin that configures Android Library module"
        implementationClass = "mpeix.plugins.AndroidLibraryConventionPlugin"
    }
    plugins.create("AndroidApplicationConventionPlugin") {
        id = "mpeix.android.application"
        displayName = "MpeiX Android Application Convention"
        description = "The Gradle Plugin that configures Android application"
        implementationClass = "mpeix.plugins.AndroidApplicationConventionPlugin"
    }
    plugins.create("AndroidUiConventionPlugin") {
        id = "mpeix.android.ui"
        displayName = "MpeiX Android Ui Convention"
        description = "The Gradle Plugin that configures Android Library with UI Components module"
        implementationClass = "mpeix.plugins.AndroidUiConventionPlugin"
    }
    plugins.create("AndroidResourcesConventionPlugin") {
        id = "mpeix.android.resources"
        displayName = "MpeiX Android Resources Convention"
        description = "The Gradle Plugin that configures Android module with resources only"
        implementationClass = "mpeix.plugins.AndroidResourcesConventionPlugin"
    }
    plugins.create("KotlinJvmParcelizePlugin") {
        id = "mpeix.kotlin.parcelize"
        displayName = "Parcelize Support Plugin"
        description = "The plugin allows to use `@Parcelize` annotation in non-android modules"
        implementationClass = "mpeix.plugins.KotlinJvmParcelizePlugin"
    }
}
