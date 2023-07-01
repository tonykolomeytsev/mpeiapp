package kekmech.ru.common_feature_toggles

import java.util.concurrent.ConcurrentHashMap

internal class RemoteVariableValueHolderImpl(
    private val remoteConfigWrapper: RemoteConfigWrapper,
) : RemoteVariableValueHolder {

    private val rewrittenValues by lazy { ConcurrentHashMap<String, String>() }

    override fun get(name: String): String? =
        rewrittenValues[name]
            ?: remoteConfigWrapper.getUntyped(name)
                .takeIf { it.isNotEmpty() }

    override fun override(name: String, value: String?) {
        if (value == null) {
            rewrittenValues.remove(name)
        } else {
            rewrittenValues[name] = value
        }
    }
}
