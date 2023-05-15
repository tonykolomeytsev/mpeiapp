package kekmech.ru.domain_bars.data

import kekmech.ru.domain_bars.BarsService

typealias JsSources = String

interface BarsExtractJsRepository {

    suspend fun getExtractJs(): Result<JsSources>
}

internal class BarsExtractJsRepositoryImpl(
    private val barsExtractJsDataSource: BarsExtractJsDataSource,
    private val barsService: BarsService,
) : BarsExtractJsRepository {

    override suspend fun getExtractJs(): Result<JsSources> =
        runCatching { barsService.getExtractJs() }
            .map { it.charStream().buffered().readText() }
            .onSuccess { barsExtractJsDataSource.save(it) }
            .recoverCatching { checkNotNull(barsExtractJsDataSource.restore()) }
}
