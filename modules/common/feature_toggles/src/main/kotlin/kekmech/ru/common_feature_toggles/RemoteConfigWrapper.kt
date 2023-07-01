package kekmech.ru.common_feature_toggles

interface RemoteConfigWrapper {

    @Deprecated("Use `getUntyped` instead", replaceWith = ReplaceWith("getUntyped"))
    operator fun get(featureToggleKey: String): Boolean

    fun getUntyped(name: String): String
}
