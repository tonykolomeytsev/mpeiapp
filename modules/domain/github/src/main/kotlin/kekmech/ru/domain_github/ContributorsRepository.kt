package kekmech.ru.domain_github

import kekmech.ru.common_persistent_cache_api.PersistentCache
import kekmech.ru.common_persistent_cache_api.ofList
import kekmech.ru.domain_github.dto.GitHubContributor
import kekmech.ru.domain_github.dto.GitHubUser
import kotlinx.coroutines.flow.Flow

class ContributorsRepository(
    private val gitHubService: GitHubService,
    persistentCache: PersistentCache,
) {

    private val contributorsCache by persistentCache.ofList<GitHubUser>()

    fun observeContributors(): Flow<List<GitHubUser>> =
        contributorsCache.observe()

    suspend fun fetchContributors(): Result<Unit> =
        runCatching {
            gitHubService.getContributors()
                .sortedByDescending(GitHubContributor::total)
                .map { gitHubService.getUser(it.author.login) }
                .map { it.copy(bio = it.bio?.trim()) }
        }.mapCatching { contributorsCache.put(it) }
}
