plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.8.21-1.0.11")
    implementation("com.android.tools.build:gradle:8.0.0")
    implementation(gradleApi())
}

gradlePlugin {
    plugins.create("mpeixBaseAndroidModulePlugin") {
        id = "mpeix.android.base"
        displayName = "MpeiX Android Base Plugin"
        description = "Gradle Plugin for setting up any android module"
        implementationClass = "mpeix.plugins.convention.BaseAndroidModulePlugin"
    }
    plugins.create("mpeixAndroidModulePlugin") {
        id = "mpeix.android"
        displayName = "MpeiX Android Library Plugin"
        description = "Gradle Plugin for setting up android library module"
        implementationClass = "mpeix.plugins.convention.AndroidModulePlugin"
    }
    plugins.create("mpeixComposeAndroidModulePlugin") {
        id = "mpeix.android.compose"
        displayName = "MpeiX Compose Android Library Plugin"
        description =
            "Gradle Plugin for setting up android library module with Compose dependencies"
        implementationClass = "mpeix.plugins.convention.ComposeAndroidModulePlugin"
    }
    plugins.create("mpeixKotlinModulePlugin") {
        id = "mpeix.kotlin"
        displayName = "Mpeix Kotlin Plugin"
        description = "Gradle Plugin for setting up pure kotlin library module"
        implementationClass = "mpeix.plugins.convention.KotlinModulePlugin"
    }
    plugins.create("mpeixAndroidJarFinderPlugin") {
        id = "mpeix.android-jar-finder"
        displayName = "Android Jar Finder Plugin"
        description = "The Gradle Plugin that finds android.jar in project environment"
        implementationClass = "mpeix.plugins.dependencies.AndroidJarFinderPlugin"
    }
    plugins.create("mpeixAar2JarPlugin") {
        id = "mpeix.aar2jar"
        displayName = "Aar2Jar Plugin"
        description = "The Gradle Plugin that transforms AAR dependencies to JAR"
        implementationClass = "mpeix.plugins.dependencies.Aar2JarPlugin"
    }
    plugins.create("mpeixKspSupportPlugin") {
        id = "mpeix.ksp"
        displayName = "Kotlin Symbol Processing Support Plugin"
        description = "The Gradle Plugin that adds KSP support to the module"
        implementationClass = "mpeix.plugins.dependencies.KspSupportPlugin"
    }
}
