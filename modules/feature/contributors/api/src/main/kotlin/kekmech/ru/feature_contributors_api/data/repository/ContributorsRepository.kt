package kekmech.ru.feature_contributors_api.data.repository

import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kotlinx.coroutines.flow.Flow

public interface ContributorsRepository {

    public fun observeContributors(): Flow<List<Contributor>>

    public suspend fun fetchContributors(): Result<Unit>
}
