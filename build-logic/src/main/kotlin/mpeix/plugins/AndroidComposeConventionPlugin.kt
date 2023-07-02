package mpeix.plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import mpeix.plugins.setup.Plugins
import mpeix.plugins.setup.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType

@Suppress("unused")
internal class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            assert(pluginManager.hasPlugin(Plugins.MpeixAndroidLibrary) ||
                    pluginManager.hasPlugin(Plugins.MpeixAndroidExtension)) {
                "Plugin `${Plugins.MpeixAndroidCompose}` can only be used " +
                        "together with plugins `${Plugins.MpeixAndroidLibrary}` " +
                        "and `${Plugins.MpeixAndroidExtension}`"
            }
            extensions.findByType<LibraryExtension>()?.apply {
                configureAndroidCompose(this, fullDependencySet = true)
            } ?: extensions.findByType<ApplicationExtension>()?.apply {
                configureAndroidCompose(this, fullDependencySet = true)
            }
        }
    }
}
