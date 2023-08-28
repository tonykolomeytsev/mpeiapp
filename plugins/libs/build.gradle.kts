plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("com.android.tools.build:gradle:7.4.0")
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins.create("mpeixBaseAndroidModulePlugin") {
        id = "mpeix.android.base"
        displayName = "MpeiX Android Base Plugin"
        description = "Gradle Plugin for setting up any android module"
        implementationClass = "mpeix.plugins.BaseAndroidModulePlugin"
    }
    plugins.create("mpeixAndroidModulePlugin") {
        id = "mpeix.android"
        displayName = "MpeiX Android Library Plugin"
        description = "Gradle Plugin for setting up android library module"
        implementationClass = "mpeix.plugins.AndroidModulePlugin"
    }
    plugins.create("mpeixComposeAndroidModulePlugin") {
        id = "mpeix.android.compose"
        displayName = "MpeiX Compose Android Library Plugin"
        description =
            "Gradle Plugin for setting up android library module with Compose dependencies"
        implementationClass = "mpeix.plugins.ComposeAndroidModulePlugin"
    }
    plugins.create("mpeixKotlinModulePlugin") {
        id = "mpeix.kotlin"
        displayName = "Mpeix Kotlin Plugin"
        description = "Gradle Plugin for setting up pure kotlin library module"
        implementationClass = "mpeix.plugins.KotlinModulePlugin"
    }
    plugins.create("AndroidSigningConventionPlugin") {
        id = "mpeix.android.signing"
        displayName = "MpeiX Android Signing Plugin"
        description = "Gradle Plugin for signing release APK and AAB"
        implementationClass = "mpeix.plugins.AndroidSigningConventionPlugin"
    }
}