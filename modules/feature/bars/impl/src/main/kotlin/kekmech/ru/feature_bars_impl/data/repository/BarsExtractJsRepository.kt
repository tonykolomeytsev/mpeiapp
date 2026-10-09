package kekmech.ru.feature_bars_impl.data.repository

import android.content.Context
import kekmech.ru.feature_bars_impl.data.datasource.BarsExtractJsDataSource
import kekmech.ru.feature_bars_impl.data.network.BarsService

typealias JsSources = String

internal class BarsExtractJsRepository(
    private val barsExtractJsDataSource: BarsExtractJsDataSource,
    private val barsService: BarsService,
    private val context: Context,
) {

    suspend fun getExtractJs(): Result<JsSources> =
        runCatching { barsService.getExtractJs() }
            .map { it.charStream().buffered().readText() }
            .onSuccess { barsExtractJsDataSource.save(it) }
            .recoverCatching {
                barsExtractJsDataSource.restore() ?: loadFromAssets()
            }

    private fun loadFromAssets(): JsSources {
        return context.assets.open("extract.js").use { stream ->
            stream.bufferedReader().use { it.readText() }
        }
    }
}

