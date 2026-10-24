package kekmech.ru.feature_contributors_impl.data.repository

import kekmech.ru.feature_contributors_api.data.repository.ContributorsRepository
import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kekmech.ru.feature_contributors_impl.data.model.GitHubContributorDto
import kekmech.ru.feature_contributors_impl.data.network.GitHubService
import kekmech.ru.lib_persistent_cache.api.PersistentCache
import kekmech.ru.lib_persistent_cache.api.ofList
import kotlinx.coroutines.flow.Flow

internal class ContributorsRepositoryImpl(
    private val gitHubService: GitHubService,
    persistentCache: PersistentCache,
) : ContributorsRepository {

    private val contributorsCache by persistentCache.ofList<Contributor>()

    private val leadContributor = Contributor(
        login = "ghostoftheeleven",
        name = "Расул Рустамов",
        bio = "Разработчик МЭИ.Хаб • студент МЭИ",
        url = "https://github.com/ghostoftheeleven",
        avatarUrl = "https://avatars.githubusercontent.com/u/98048622?v=4",
    )

    private val coAuthor = Contributor(
        login = "Johnny-Wickky",
        name = "Johnny Wickky",
        bio = "Студент МЭИ, ИБ-энтузиаст",
        url = "https://github.com/Johnny-Wickky",
        avatarUrl = "https://avatars.githubusercontent.com/u/Johnny-Wickky?v=4",
    )

    override fun observeContributors(): Flow<List<Contributor>> =
        contributorsCache.observe()

    override suspend fun fetchContributors(): Result<Unit> =
        runCatching {
            val originalContributors = runCatching {
                gitHubService.getContributors()
                    .sortedByDescending(GitHubContributorDto::total)
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
            }.getOrDefault(emptyList())

            listOf(leadContributor, coAuthor) + originalContributors.filterNot {
                it.login.equals("ghostoftheeleven", ignoreCase = true) ||
                    it.login.equals("Johnny-Wickky", ignoreCase = true)
            }
        }.mapCatching { contributorsCache.put(it) }
}
