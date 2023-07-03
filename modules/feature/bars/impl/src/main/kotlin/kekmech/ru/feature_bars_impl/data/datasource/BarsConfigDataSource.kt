package kekmech.ru.feature_bars_impl.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kekmech.ru.feature_bars_impl.domain.RemoteBarsConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull

internal class BarsConfigDataSource(
    private val dataStore: DataStore<Preferences>,
) {

    private val loginUrlKey = stringPreferencesKey("barsConfig.loginUrl")
    private val logoutUrlKey = stringPreferencesKey("barsConfig.logoutUrl")
    private val marksListUrlKey = stringPreferencesKey("barsConfig.marksListUrl")
    private val studentListUrlKey = stringPreferencesKey("barsConfig.studentListUrl")

    suspend fun save(barsConfig: RemoteBarsConfig) {
        dataStore.edit { store ->
            store[loginUrlKey] = barsConfig.loginUrl
            store[logoutUrlKey] = barsConfig.logoutUrl
            store[marksListUrlKey] = barsConfig.marksListUrl
            store[studentListUrlKey] = barsConfig.studentListUrl
        }
    }

    suspend fun restore(): RemoteBarsConfig? =
        dataStore.data
            .mapNotNull { store ->
                RemoteBarsConfig(
                    loginUrl = store[loginUrlKey] ?: return@mapNotNull null,
                    studentListUrl = store[studentListUrlKey] ?: return@mapNotNull null,
                    marksListUrl = store[marksListUrlKey] ?: return@mapNotNull null,
                    logoutUrl = store[logoutUrlKey] ?: return@mapNotNull null,
                )
            }
            .firstOrNull()
}
