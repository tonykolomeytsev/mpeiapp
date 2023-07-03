package kekmech.ru.feature_bars_impl.data.repository

import kekmech.ru.feature_bars_impl.data.datasource.BarsExtractJsDataSource
import kekmech.ru.feature_bars_impl.data.network.BarsService

typealias JsSources = String

internal class BarsExtractJsRepository(
    private val barsExtractJsDataSource: BarsExtractJsDataSource,
    private val barsService: BarsService,
) {

    suspend fun getExtractJs(): Result<JsSources> =
        runCatching { barsService.getExtractJs() }
            .map { it.charStream().buffered().readText() }
            .onSuccess { barsExtractJsDataSource.save(it) }
            .recoverCatching { checkNotNull(barsExtractJsDataSource.restore()) }
}
