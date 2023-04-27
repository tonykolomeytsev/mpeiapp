@file:Suppress("UnstableApiUsage")

pluginManagement {

    /**
     * The pluginManagement {repositories {...}} block configures the
     * repositories Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies. Gradle pre-configures support for remote
     * repositories such as JCenter, Maven Central, and Ivy. You can also use
     * local repositories or define your own remote repositories. The code below
     * defines the Gradle Plugin Portal, Google's Maven repository,
     * and the Maven Central Repository as the repositories Gradle should use to look for its dependencies.
     */

    includeBuild("plugins")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

dependencyResolutionManagement {

    /**
     * The dependencyResolutionManagement { repositories {...}}
     * block is where you configure the repositories and dependencies used by
     * all modules in your project, such as libraries that you are using to
     * create your application. However, you should configure module-specific
     * dependencies in each module-level build.gradle file. For new projects,
     * Android Studio includes Google's Maven repository and the
     * Maven Central Repository by
     * default, but it does not configure any dependencies (unless you select a
     * template that requires some).
     */

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

rootProject.name = "mpeix"

File(rootProject.projectDir, "modules").walk()
    .filter { it.isBuildGradleScript() }
    .filter { it != rootProject.buildFile }
    .mapNotNull { it.parentFile }
    .count { moduleDir ->
        val moduleName = ":${moduleDir.name}"
        include(moduleName)
        project(moduleName).projectDir = moduleDir
        true
    }
    .also { println("Added $it subprojects") }

fun File.isBuildGradleScript(): Boolean =
    isFile && name == "build.gradle.kts"
