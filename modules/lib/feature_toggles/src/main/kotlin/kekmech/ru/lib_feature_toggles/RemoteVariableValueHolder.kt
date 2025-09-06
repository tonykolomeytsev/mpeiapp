package kekmech.ru.lib_feature_toggles

public class RemoteVariableValueHolder internal constructor(
    private val remoteConfigWrapper: RemoteConfigWrapper,
    private val middleware: Middleware?,
) {

    /**
     * Get serialized variable value
     */
    public fun get(name: String): String? {
        return middleware?.get(name)
            ?: remoteConfigWrapper.getUntyped(name)
                .takeIf { it.isNotEmpty() }
    }

    public interface Middleware {

        public fun get(name: String): String?
    }
}
