package kekmech.ru.common_feature_toggles

interface RemoteVariableValueHolder {

    /**
     * Get serialized variable value
     */
    operator fun get(name: String): String?
}
