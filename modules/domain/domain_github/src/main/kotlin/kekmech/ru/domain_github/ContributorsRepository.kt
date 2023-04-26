package kekmech.ru.domain_github

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.domain_github.dto.GitHubContributor
import kekmech.ru.domain_github.dto.GitHubUser
import java.time.Duration

class ContributorsRepository(
    persistentCache: PersistentCache,
    private val gitHubService: GitHubService,
) {

    private val contributorsCache = persistentCache
        .of(
            key = ContributorsCacheKey,
            valueClass = ContributorsCacheWrapper::class.java,
            lifetime = Duration.ofDays(1),
        )

    fun observeContributors(): Observable<List<GitHubUser>> =
        contributorsCache.observe()
            .map { it.items }

    fun fetchContributors(): Completable =
        gitHubService.getContributors()
            .flattenAsObservable { it.sortedByDescending(GitHubContributor::total) }
            .concatMapSingle { gitHubService.getUser(it.author.login) }
            .map { it.copy(bio = it.bio?.trim()) }
            .toList()
            .doOnSuccess { contributorsCache.set(ContributorsCacheWrapper(it)) }
            .ignoreElement()

    private data class ContributorsCacheWrapper(
        val items: List<GitHubUser>,
    )

    private companion object {

        const val ContributorsCacheKey = "ContributorsCacheKey"
    }
}