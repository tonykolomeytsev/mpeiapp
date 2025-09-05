package mpeix.plugins.features.unittesting

import mpeix.plugins.ext.configureLazy
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

internal fun Project.setupUnitTests() {
    tasks.configureLazy<Test> {
        useJUnitPlatform()
        jvmArgs("-Xshare:off")
    }
}