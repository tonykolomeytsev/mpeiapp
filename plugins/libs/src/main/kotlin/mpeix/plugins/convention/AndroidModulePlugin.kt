package mpeix.plugins.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AndroidModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply {
            apply("com.android.library")
            apply("mpeix.android.base")
        }
        target.extensions.configure(LibraryExtension::class.java) { extension ->
            // configure namespace of the package (instead of AndroidManifest.xml)
            extension.namespace = "kekmech.ru.${target.name}"
        }
    }
}