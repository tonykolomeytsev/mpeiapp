package kekmech.ru.domain_bars.data

import kekmech.ru.domain_bars.BarsService
import kekmech.ru.domain_bars.dto.RemoteBarsConfig

interface BarsConfigRepository {

    suspend fun getBarsConfig(): Result<RemoteBarsConfig>
}

internal class BarsConfigRepositoryImpl(
    private val barsConfigDataSource: BarsConfigDataSource,
    private val barsService: BarsService,
) : BarsConfigRepository {

    override suspend fun getBarsConfig(): Result<RemoteBarsConfig> =
        runCatching { barsService.getRemoteBarsConfig() }
            .onSuccess { barsConfigDataSource.save(it) }
            .recoverCatching { checkNotNull(barsConfigDataSource.restore()) }
}
