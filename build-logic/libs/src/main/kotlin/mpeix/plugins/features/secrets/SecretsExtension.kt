package mpeix.plugins.features.secrets

import mpeix.plugins.ext.isCiBuild
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class SecretsExtension @Inject constructor(
    private val objects: ObjectFactory,
) {

    private val secrets = mutableMapOf<String, String>()

    fun register(id: String, action: Action<SecretDeclarationContext>) {
        val ctx = objects.newInstance(SecretDeclarationContext::class.java)
        action.execute(ctx)
        secrets[id] = if (isCiBuild) {
            ctx.ci.orNull ?: error("No CI value provided for secret with id `$id`")
        } else {
            ctx.local.orNull ?: error("No local value provided for secret with id `$id`")
        }
    }

    fun get(id: String): String {
        return secrets[id] ?: error("Secret with id `$id` have not registered")
    }

    abstract class SecretDeclarationContext {
        abstract val local: Property<String>
        abstract val ci: Property<String>
    }
}