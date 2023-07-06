package kekmech.ru.feature_bars_impl.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kekmech.ru.ext_kotlin.fromBase64
import kekmech.ru.ext_kotlin.toBase64
import kekmech.ru.feature_bars_impl.data.repository.JsSources
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull

internal class BarsExtractJsDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    private val extractJsEncodedKey = stringPreferencesKey("extractJs.base64encoded")

    suspend fun save(jsSources: JsSources) {
        dataStore.edit { store ->
            store[extractJsEncodedKey] = jsSources.toBase64()
        }
    }

    suspend fun restore(): JsSources? =
        dataStore.data
            .mapNotNull { store ->
                store[extractJsEncodedKey]?.fromBase64()
            }
            .firstOrNull()
}
