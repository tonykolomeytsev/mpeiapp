package kekmech.ru.feature_bars_impl.data.repository

import android.content.Context
import kekmech.ru.feature_bars_impl.data.datasource.BarsConfigDataSource
import kekmech.ru.feature_bars_impl.data.mapper.RemoteBarsConfigMapper
import kekmech.ru.feature_bars_impl.data.model.RemoteBarsConfigDto
import kekmech.ru.feature_bars_impl.data.network.BarsService
import kekmech.ru.feature_bars_impl.domain.RemoteBarsConfig
import kotlinx.serialization.json.Json

internal class BarsConfigRepository(
    private val barsConfigDataSource: BarsConfigDataSource,
    private val barsService: BarsService,
    private val context: Context,
) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getBarsConfig(): Result<RemoteBarsConfig> =
        runCatching { barsService.getRemoteBarsConfig() }
            .map { RemoteBarsConfigMapper.dtoToDomain(it) }
            .onSuccess { barsConfigDataSource.save(it) }
            .recoverCatching {
                barsConfigDataSource.restore() ?: loadFromAssets()
            }

    private fun loadFromAssets(): RemoteBarsConfig {
        return context.assets.open("config.json").use { stream ->
            val content = stream.bufferedReader().use { it.readText() }
            val dto = json.decodeFromString<RemoteBarsConfigDto>(content)
            RemoteBarsConfigMapper.dtoToDomain(dto)
        }
    }
}

