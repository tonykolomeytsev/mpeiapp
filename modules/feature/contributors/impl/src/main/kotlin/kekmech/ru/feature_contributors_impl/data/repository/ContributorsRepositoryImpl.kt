package kekmech.ru.feature_contributors_impl.data.repository

import kekmech.ru.feature_contributors_api.data.repository.ContributorsRepository
import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kekmech.ru.feature_contributors_impl.data.dto.GitHubContributor
import kekmech.ru.feature_contributors_impl.data.network.GitHubService
import kekmech.ru.library_persistent_cache.api.PersistentCache
import kekmech.ru.library_persistent_cache.api.ofList
import kotlinx.coroutines.flow.Flow

internal class ContributorsRepositoryImpl(
    private val gitHubService: GitHubService,
    persistentCache: PersistentCache,
) : ContributorsRepository {

    private val contributorsCache by persistentCache.ofList<Contributor>()

    override fun observeContributors(): Flow<List<Contributor>> =
        contributorsCache.observe()

    override suspend fun fetchContributors(): Result<Unit> =
        runCatching {
            gitHubService.getContributors()
                .sortedByDescending(GitHubContributor::total)
                .map { gitHubService.getUser(it.author.login) }
                .map {
                    Contributor(
                        login = it.login,
                        name = it.name,
                        bio = it.bio?.trim(),
                        url = it.gitHubPageUrl,
                        avatarUrl = it.avatarUrl,
                    )
                }
        }.mapCatching { contributorsCache.put(it) }
}
