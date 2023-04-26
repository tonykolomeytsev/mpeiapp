import org.gradle.api.Project

val Project.androidJar
    get() = rootProject.extensions.extraProperties["androidJar"]
        ?: throw RuntimeException("The `androidJar` property is missing from the root project. " +
                "Make sure the `mpeix.android-jar-finder` plugin is applied in the `app` module")
