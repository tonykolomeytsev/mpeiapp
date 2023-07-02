package kekmech.ru.library_feature_toggles

interface RemoteConfigWrapper {

    fun getUntyped(name: String): String

    fun getAll(): Map<String, String>
}
