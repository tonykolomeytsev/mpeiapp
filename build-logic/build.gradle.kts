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
    plugins.create("AndroidBaseConventionPlugin") {
        id = "mpeix.android.base"
        displayName = "MpeiX Android Base Plugin"
        description = "Gradle Plugin for setting up any android module"
        implementationClass = "mpeix.plugins.AndroidBaseConventionPlugin"
    }
    plugins.create("AndroidSigningConventionPlugin") {
        id = "mpeix.android.signing"
        displayName = "MpeiX Android Signing Plugin"
        description = "Gradle Plugin for signing release APK and AAB"
        implementationClass = "mpeix.plugins.AndroidSigningConventionPlugin"
    }
    plugins.create("AndroidLibConventionPlugin") {
        id = "mpeix.android.lib"
        displayName = "MpeiX Android Library Plugin"
        description = "Gradle Plugin for setting up android library module"
        implementationClass = "mpeix.plugins.AndroidLibConventionPlugin"
    }
    plugins.create("AndroidComposeConventionPlugin") {
        id = "mpeix.android.compose"
        displayName = "MpeiX Compose Android Library Plugin"
        description =
            "Gradle Plugin for setting up android library module with Compose dependencies"
        implementationClass = "mpeix.plugins.AndroidComposeConventionPlugin"
    }
    plugins.create("KotlinLibConventionPlugin") {
        id = "mpeix.kotlin.lib"
        displayName = "Mpeix Kotlin Plugin"
        description = "Gradle Plugin for setting up pure kotlin library module"
        implementationClass = "mpeix.plugins.KotlinLibConventionPlugin"
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
}
