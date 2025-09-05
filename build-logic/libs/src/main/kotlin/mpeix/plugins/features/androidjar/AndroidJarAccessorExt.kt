package mpeix.plugins.features.androidjar

import org.gradle.api.Project

internal fun Project.createAndroidJarAccessorExtension() {
    extensions.add(
        /* name = */ "androidJar",
        /* extension = */ checkNotNull(rootProject.extensions.extraProperties[AndroidJarLocationKey]) {
            "The property with 'android.jar' location is missing. Make sure the " +
                    "buildLogic.androidJar.targetSdk(...) is applied in the root project"
        }
    )
}