package kekmech.ru.debug_menu.presentation.screens.feature_toggles

import android.content.Context
import kekmech.ru.library_feature_toggles.RemoteConfigWrapper
import kekmech.ru.library_feature_toggles.RemoteVariableValueHolder
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.concurrent.ConcurrentHashMap

internal class FeatureTogglesOverwriteMiddleware(
    private val remoteConfigWrapper: RemoteConfigWrapper,
    context: Context,
) : RemoteVariableValueHolder.Middleware {

    private val overwritten by lazy { restoreFromCache() }
    private val cacheFile = context.cacheDir.resolve("feature-toggles.bin")

    override fun get(name: String): String =
        overwritten[name]
            ?: remoteConfigWrapper.getUntyped(name)

    fun overwrite(name: String, value: String?) {
        if (value == null) {
            overwritten.remove(name)
        } else {
            overwritten[name] = value
        }
        saveToCache()
    }

    fun isOverwritten(name: String): Boolean =
        overwritten.containsKey(name) &&
                remoteConfigWrapper.getAll().let {
                    it.containsKey(name) && it[name] != overwritten[name]
                }

    fun reset() {
        overwritten.clear()
        saveToCache()
    }

    private fun saveToCache() {
        ObjectOutputStream(cacheFile.outputStream()).use {
            it.writeObject(overwritten)
        }
    }

    private fun restoreFromCache(): ConcurrentHashMap<String, String> {
        return runCatching {
            ObjectInputStream(cacheFile.inputStream()).use {
                @Suppress("UNCHECKED_CAST")
                it.readObject() as ConcurrentHashMap<String, String>
            }
        }.getOrElse { ConcurrentHashMap() }
    }
}
