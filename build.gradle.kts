// TODO: move to plugin
buildscript {
    val versions = object {
        val kotlin = "1.7.0"
        val recyclerview = "1.2.1"
        val constraintlayout = "2.1.0"
        val coordinatorlayout = "1.1.0"
        val viewPager2 = "1.0.0"
        val appcompat = "1.1.0"
        val koin = "3.1.2"
        val material = "1.4.0"
        val cardview = "1.0.0"
        val swiperefreshlayout = "1.1.0"
        val fragmentktx = "1.3.6"
        val retrofit = "2.9.0"
        val okhttp = "4.9.0"
    }


    extra.apply {
        set("kotlin", "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${versions.kotlin}")
        set("kotlinStdLib", "org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlin}")
        set("timber", "com.jakewharton.timber:timber:5.0.1")
        set("desugaring", "com.android.tools:desugar_jdk_libs:1.1.5")
        set("picasso", "com.squareup.picasso:picasso:2.8")
        set("coil", "io.coil-kt:coil:1.3.2")

        val androidx = object {
            val constraintlayout =
                "androidx.constraintlayout:constraintlayout:${versions.constraintlayout}"
            val coordinatorlayout =
                "androidx.coordinatorlayout:coordinatorlayout:${versions.coordinatorlayout}"
            val appcompat = "androidx.appcompat:appcompat:${versions.appcompat}"
            val recyclerview = "androidx.recyclerview:recyclerview:${versions.recyclerview}"
            val cardview = "androidx.cardview:cardview:${versions.cardview}"
            val swiperefreshlayout =
                "androidx.swiperefreshlayout:swiperefreshlayout:${versions.swiperefreshlayout}"
            val viewPager2 = "androidx.viewpager2:viewpager2:${versions.viewPager2}"
            val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.6"
            val coreKtx = "androidx.core:core-ktx:1.3.2"
            val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:2.3.1"
            val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        }
        set("androidx", androidx)

        val test = object {
            val runner = "androidx.test:runner:1.2.0"
            val espresso = "androidx.test.espresso:espresso-core:3.2.0"
        }
        set("test", test)

        val google = object {
            val material = "com.google.android.material:material:${versions.material}"
            val gson = "com.google.code.gson:gson:2.8.8"
            val maps = "com.google.android.gms:play-services-maps:17.0.1"
        }
        set("google", google)

        val koin = object {
            val core = "io.insert-koin:koin-core:${versions.koin}"
            val android = "io.insert-koin:koin-android:${versions.koin}"
        }
        set("koin", koin)

        val firebase = object {
            val analytics = "com.google.firebase:firebase-analytics:19.0.1"
            val config = "com.google.firebase:firebase-config:21.0.1"
            val crashlytics = "com.google.firebase:firebase-crashlytics:18.2.1"
        }
        set("firebase", firebase)

        val rx = object {
            val java = "io.reactivex.rxjava3:rxjava:3.1.1"
            val android = "io.reactivex.rxjava3:rxandroid:3.0.0"
        }
        set("rx", rx)

        val squareup = object {
            val retrofit = "com.squareup.retrofit2:retrofit:${versions.retrofit}"
            val retrofit_rxJava2 = "com.squareup.retrofit2:adapter-rxjava3:${versions.retrofit}"
            val retrofit_gson = "com.squareup.retrofit2:converter-gson:${versions.retrofit}"
            val okhttp = "com.squareup.okhttp3:okhttp:${versions.okhttp}"
            val okhttpLogger = "com.squareup.okhttp3:logging-interceptor:${versions.okhttp}"
        }
        set("squareup", squareup)

        val facebook = object {
            val shimmer = "com.facebook.shimmer:shimmer:0.5.0"
        }
        set("facebook", facebook)

        val kotest = object {
            val runner = "io.kotest:kotest-runner-junit5:4.4.1"
            val assertions = "io.kotest:kotest-assertions-core:4.4.1"
            val property = "io.kotest:kotest-property:4.4.2"
        }
        set("kotest", kotest)

        val tinkoff = object {
            val pagerindicator = "ru.tinkoff.scrollingpagerindicator:scrollingpagerindicator:1.2.1"
        }
        set("tinkoff", tinkoff)

        val vividMoney = object {
            val elmslieCore = "com.github.vivid-money.elmslie:elmslie-core:1.1.4"
            val elmslieAndroid = "com.github.vivid-money.elmslie:elmslie-android:1.1.4"
            val elmslieStoreHolder = "com.github.vivid-money.elmslie:elmslie-store-persisting:1.1.4"
        }
        set("vividMoney", vividMoney)

        set("appVersionName", "1.11.0")
        set("appVersionCode", 43)
        set("gradleDir", "$rootDir/gradle")
    }
}

plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    kotlin("android") version "1.7.0" apply false
    id("com.google.gms.google-services") version "4.3.13" apply false
    id("com.google.firebase.crashlytics") version "2.9.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.20.0"

    id("mpeix.android") apply false
    id("mpeix.kotlin") apply false
}

tasks.withType<Delete>() {
    delete(rootProject.buildDir)
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }
    detekt {
        config = rootProject.files("detekt.yaml")
    }
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
    }
}
