package kekmech.ru.common_feature_toggles

import java.util.concurrent.ConcurrentHashMap

internal class RemoteVariableValueHolderImpl(
    private val remoteConfigWrapper: RemoteConfigWrapper,
) : RemoteVariableValueHolder, RewriteRemoteVariableHandle {

    private val rewritten by lazy { ConcurrentHashMap<String, String>() }

    override fun get(name: String): String? =
        rewritten[name]
            ?: remoteConfigWrapper.getUntyped(name)
                .takeIf { it.isNotEmpty() }

    override fun override(name: String, value: String?) {
        if (value == null) {
            rewritten.remove(name)
        } else {
            rewritten[name] = value
        }
    }

    override fun isRewritten(name: String): Boolean =
        rewritten.containsKey(name) &&
                rewritten[name] != remoteConfigWrapper.getUntyped(name)
}
