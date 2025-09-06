package mpeix.plugins.features.android

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import mpeix.plugins.ext.configure
import mpeix.plugins.features.globalconfig.getAndroidProjectConfiguration
import mpeix.plugins.features.unittesting.setupUnitTests
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

context(
    project: Project,
    extension: CommonExtension<*, *, *, *, *, *>,
)
internal fun configureKotlinAndroid(
    explicitApi: Boolean,
) {
    project.setupUnitTests()
    val config = project.getAndroidProjectConfiguration()
    with(extension) {
        compileSdk = config.targetSdk.get()
        buildToolsVersion = config.buildToolsVersion.get()

        defaultConfig {
            minSdk = config.minSdk.get()
        }

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
            unitTests.isReturnDefaultValues = true
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            targetCompatibility = JavaVersion.VERSION_17
            sourceCompatibility = JavaVersion.VERSION_17
        }
    }
    project.extensions.configure<KotlinAndroidProjectExtension> {
        if (explicitApi) explicitApi()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xannotation-target-all")
        }
    }
    project.dependencies.add(
        "coreLibraryDesugaring",
        "com.android.tools:desugar_jdk_libs:${config.coreLibraryDesugaringVersion.get()}"
    )
}

internal fun Project.configureAndroidNamespace(
    extension: CommonExtension<*, *, *, *, *, *>,
) {
    // у нас есть модули с именами типа instaledApp, pushNotification
    // А имена пакетов требуют lowercase()
    extension.namespace = "kekmech.ru.${name.lowercase().replace('-', '.')}"
}

internal fun LibraryAndroidComponentsExtension.enableOnlyDebugBuildType() {
    // Все модули, кроме бинарей, должны иметь один билд тайп.
    // Делается это для приведения проекта к Bazel-like структуре и серьезной оптимизации билд кэша.
    beforeVariants(selector = selector().all()) { builder ->
        builder.enable = builder.buildType == "debug"
    }
}

internal fun LibraryAndroidComponentsExtension.disableAndroidTestTasks() {
    // Отключаем таски связанные с androidTest в библиотечных модулях, потому что
    // они никак не участвуют в сборке автотестов app/tvapp бинарей, но при этом
    // замедляют сборку в студии во время запуска `Build -> Rebuild Project`
    beforeVariants(selector = selector().all()) { builder ->
        builder.enableAndroidTest = false
    }
}