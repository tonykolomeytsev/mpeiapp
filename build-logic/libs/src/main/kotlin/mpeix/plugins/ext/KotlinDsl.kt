package mpeix.plugins.ext

import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.TaskContainer

internal inline fun <reified T : Any> ExtensionContainer.configure(noinline action: T.() -> Unit) =
    configure(T::class.java, action)

internal inline fun <reified T : Any> ExtensionContainer.create(
    name: String,
    vararg constructionArguments: Any
) =
    create(name, T::class.java, *constructionArguments)

internal inline fun <reified T : Task> TaskContainer.configureLazy(noinline action: T.() -> Unit) =
    withType(T::class.java).configureEach(action)