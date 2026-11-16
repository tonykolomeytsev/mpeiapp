package mpeix.plugins.features.composeonly

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

private const val TASK_NAME = "verifyComposeOnly"

/**
 * The UI is Compose only. Fails `preBuild` of the module if it brings back the View UI:
 * XML layouts or imports of Fragments, AppCompat, RecyclerView, ViewBinding and other View widgets.
 *
 * Activities are `ComponentActivity`, screens exchange results through Navigation3 `ResultEventBus`.
 */
internal fun Project.setupComposeOnlyCheck() {
    val verify = tasks.register(TASK_NAME, VerifyComposeOnlyTask::class.java) { task ->
        task.group = "verification"
        task.description = "Checks that the module has no XML layouts and no View UI imports"
        task.sources.from(fileTree("src") { it.include("**/*.kt", "**/*.java") })
        task.layouts.from(fileTree("src") { it.include("*/res/layout*/**") })
        task.reportFile.set(layout.buildDirectory.file("reports/$TASK_NAME/ok.txt"))
    }
    tasks.matching { it.name == "preBuild" }.configureEach { it.dependsOn(verify) }
}

@CacheableTask
internal abstract class VerifyComposeOnlyTask : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sources: ConfigurableFileCollection

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val layouts: ConfigurableFileCollection

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    @TaskAction
    fun verify() {
        val problems = layouts.files.map { "XML layout: $it" } +
            sources.files.flatMap { file ->
                file.useLines { lines ->
                    lines.mapIndexedNotNull { index, line ->
                        val import = IMPORT.matchEntire(line.trim())?.groupValues?.get(1)
                            ?: return@mapIndexedNotNull null
                        if (FORBIDDEN_IMPORTS.any { it.matches(import) }) {
                            "View UI import: $file:${index + 1}: $import"
                        } else {
                            null
                        }
                    }.toList()
                }
            }
        if (problems.isNotEmpty()) {
            throw GradleException(
                "The UI is Compose only, write it with Compose instead:\n" +
                    problems.joinToString(separator = "\n") { "  $it" }
            )
        }
        reportFile.get().asFile.writeText("OK")
    }

    private companion object {
        val IMPORT = Regex("""import\s+([\w.]+)(?:\s+as\s+\w+)?""")
        val FORBIDDEN_IMPORTS = listOf(
            Regex("""androidx\.fragment\..+"""),
            Regex("""androidx\.appcompat\..+"""),
            Regex("""androidx\.recyclerview\..+"""),
            Regex("""androidx\.viewpager2?\..+"""),
            Regex("""androidx\.constraintlayout\..+"""),
            Regex("""androidx\.swiperefreshlayout\..+"""),
            Regex("""androidx\.viewbinding\..+"""),
            Regex(""".+\.databinding\..+Binding"""),
            Regex("""android\.view\.LayoutInflater"""),
            Regex("""androidx\.compose\.ui\.platform\.ComposeView"""),
            Regex("""com\.google\.android\.material\..+"""),
            Regex("""com\.facebook\.shimmer\..+"""),
        )
    }
}
