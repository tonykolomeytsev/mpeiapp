object Dependencies {
    object Versions {
        const val kotlin = "1.3.40"
        const val androidGradle = "3.4.2"
        const val supportLibrary = "28.0.0"
        const val dagger = "2.16"
        const val lifecycle = "1.1.0"
        const val constraintLayout = "1.1.3"

        const val cicerone = "3.0.0"

        const val jUnit = "4.12"
        const val espressoCore = "3.0.2"
        const val testRunner = "1.0.2"
    }

    object BuildPlugins {
        const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
        const val googleServices = "com.google.gms:google-services:4.0.1"
    }

    object Android {
        const val buildToolsVersion = "27.0.3"
        const val minSdkVersion = 21
        const val targetSdkVersion = 28
        const val compileSdkVersion = 28
        const val applicationId = "my.company.app"
        const val versionCode = 1
        const val versionName = "1.0.0"
    }

    object Libs {
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

        const val appCompat = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
        const val design = "com.android.support:design:${Versions.supportLibrary}"
        const val supportAnnotations = "com.android.support:support-annotations:${Versions.supportLibrary}"
        const val cardView = "com.android.support:cardview-v7:${Versions.supportLibrary}"

        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
        const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"

        const val cicerone = "ru.terrakok.cicerone:cicerone:${Versions.cicerone}"

        const val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
        const val lifecycleCompiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"

        const val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    }

    object TestLibs {
        const val jUnit = "junit:junit:${Versions.jUnit}"
        const val testRunner = "com.android.support.test:runner:${Versions.testRunner}"
        const val espressoCore = "com.android.support.test.espresso:espresso-core:${Versions.espressoCore}"
    }

}