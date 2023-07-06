package kekmech.ru.feature_bars_impl.data.repository

import kekmech.ru.feature_bars_impl.data.datasource.BarsConfigDataSource
import kekmech.ru.feature_bars_impl.data.network.BarsService
import kekmech.ru.feature_bars_impl.domain.RemoteBarsConfig

internal class BarsConfigRepository(
    private val barsConfigDataSource: BarsConfigDataSource,
    private val barsService: BarsService,
) {

    suspend fun getBarsConfig(): Result<RemoteBarsConfig> =
        runCatching { barsService.getRemoteBarsConfig() }
            .onSuccess { barsConfigDataSource.save(it) }
            .recoverCatching { checkNotNull(barsConfigDataSource.restore()) }
}
