package kekmech.ru.common_feature_toggles

class RemoteVariableValueHolder internal constructor(
    private val remoteConfigWrapper: RemoteConfigWrapper,
    private val middleware: Middleware?,
) {

    /**
     * Get serialized variable value
     */
    fun get(name: String): String? =
        middleware?.get(name)
            ?: remoteConfigWrapper.getUntyped(name)
                .takeIf { it.isNotEmpty() }

    interface Middleware {

        fun get(name: String): String?
    }
}
