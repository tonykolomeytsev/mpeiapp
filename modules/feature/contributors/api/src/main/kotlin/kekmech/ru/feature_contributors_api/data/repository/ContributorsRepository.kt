package kekmech.ru.feature_contributors_api.data.repository

import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kotlinx.coroutines.flow.Flow

interface ContributorsRepository {

    fun observeContributors(): Flow<List<Contributor>>

    suspend fun fetchContributors(): Result<Unit>
}
