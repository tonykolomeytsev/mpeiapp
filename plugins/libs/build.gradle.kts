plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("com.android.tools.build:gradle:7.2.1")
    implementation(gradleApi())
}

gradlePlugin {
    plugins.create("mpeixAndroidModulePlugin") {
        id = "mpeix.android"
        displayName = "MpeiX Android Plugin"
        description = "Gradle Plugin for setting up android library module"
        implementationClass = "mpeix.plugins.AndroidModulePlugin"
    }
    plugins.create("mpeixKotlinModulePlugin") {
        id = "mpeix.kotlin"
        displayName = "Mpeix Kotlin Plugin"
        description = "Gradle Plugin for setting up pure kotlin library module"
        implementationClass = "mpeix.plugins.KotlinModulePlugin"
    }
}