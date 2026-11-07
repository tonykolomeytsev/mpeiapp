package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import io.github.tonykolomeytsev.mpeix.sdk.Completeness
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.Profile
import io.github.tonykolomeytsev.mpeix.sdk.RefreshState
import io.github.tonykolomeytsev.mpeix.sdk.SdkError
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.navigation.BarsAccountsScreenKey
import kekmech.ru.feature_bars_impl.navigation.BarsDetailsScreenKey
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.ShowingFromCacheReason
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal.CacheReasonsChanged
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal.CheckAuthStatusFailure
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal.ObserveMarksFailure
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal.ObserveMarksSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal.ObserveProfileSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal.SubscribeAuthStatusSuccess
import kekmech.ru.lib_elm.actorFlow
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import money.vivid.elmslie.core.store.Actor
import io.github.tonykolomeytsev.mpeix.sdk.Resource as SdkResource
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent as Event

@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsActor(
    private val sdk: MpeixSdk,
    private val appSettingsFeatureLauncher: AppSettingsFeatureLauncher,
    private val router: Router,
    private val barsProfileIdRepository: BarsProfileIdRepository,
) : Actor<Command, Event>() {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.SubscribeAuthStatus -> barsProfileIdRepository.currentProfileId
                .map { if (it != null) AuthStatus.LoggedIn else AuthStatus.LoginRequired }
                .mapEvents(
                    eventMapper = ::SubscribeAuthStatusSuccess,
                    errorMapper = { CheckAuthStatusFailure(it.recorded("auth.init")) },
                )

            is Command.ObserveProfile -> barsProfileIdRepository.currentProfileId
                .filterNotNull()
                .flatMapLatest { sdk.bars.profile.profile(it) }
                .mapNotNull { it.value?.toBarsProfile() }
                .mapEvents(
                    eventMapper = ::ObserveProfileSuccess,
                    errorMapper = { CheckAuthStatusFailure(it.recorded("observeProfile")) },
                )

            is Command.ObserveMarks -> barsProfileIdRepository.currentProfileId
                .filterNotNull()
                .flatMapLatest { sdk.bars.study.academicPerformance(it) }
                .mapNotNull { resource ->
                    val performance = resource.value ?: return@mapNotNull null
                    ObserveMarksSuccess(
                        disciplines = performance.current.disciplines,
                        semesterName = performance.current.info.title,
                    )
                }
                .mapEvents(
                    eventMapper = { it },
                    errorMapper = { ObserveMarksFailure(it.recorded("observeMarks")) },
                )

            // A starting refresh clears the previous failure in the SDK, so emissions during a refresh
            // are skipped: the banner keeps the reasons of the last finished refresh instead of blinking
            is Command.ObserveCacheReasons -> barsProfileIdRepository.currentProfileId
                .filterNotNull()
                .flatMapLatest { id ->
                    combine(
                        sdk.bars.profile.profile(id),
                        sdk.bars.study.academicPerformance(id),
                    ) { profile, marks ->
                        listOf(profile, marks)
                    }
                }
                .filter { resources -> resources.none { it.refresh is RefreshState.InProgress } }
                .map { resources -> resources.flatMapTo(mutableSetOf()) { it.cacheReasons() } }
                .distinctUntilChanged()
                .mapEvents(
                    eventMapper = ::CacheReasonsChanged,
                    errorMapper = { CheckAuthStatusFailure(it.recorded("observeCacheReasons")) },
                )

            // Concurrent refreshes of the same profile share one request in the SDK
            is Command.Refresh -> actorFlow {
                val id = barsProfileIdRepository.currentProfileId.value ?: return@actorFlow
                coroutineScope {
                    launch { sdk.bars.profile.refresh(id) }
                    launch { sdk.bars.study.refresh(id) }
                }
            }.mapEvents(
                eventMapper = { Internal.RefreshSuccess },
            )

            is Command.OpenAppSettings -> actorFlow {
                withContext(Dispatchers.Main) {
                    appSettingsFeatureLauncher.launch()
                }
            }.mapEvents()

            is Command.OpenAccounts -> actorFlow {
                withContext(Dispatchers.Main) {
                    router.executeCommand(AddScreenForward(BarsAccountsScreenKey))
                }
            }.mapEvents()

            is Command.OpenDetails -> actorFlow {
                withContext(Dispatchers.Main) {
                    router.executeCommand(AddScreenForward(BarsDetailsScreenKey(command.index)))
                }
            }.mapEvents()
        }

    private fun Throwable.recorded(stage: String): Throwable {
        crashlytics.recordException(this) {
            key("screen", "main")
            key("stage", stage)
        }
        return this
    }

    /** Why this resource shows the cached value. Nothing to tell when there is no cached value yet. */
    private fun SdkResource<*>.cacheReasons(): Set<ShowingFromCacheReason> = buildSet {
        if (value == null) return@buildSet
        if (completeness == Completeness.Partial) add(ShowingFromCacheReason.ProcessingFailed)
        val error = (refresh as? RefreshState.Failed)?.error ?: return@buildSet
        add(
            when (error) {
                is SdkError.NoConnection,
                is SdkError.Timeout,
                is SdkError.SiteUnavailable -> ShowingFromCacheReason.NetworkError

                is SdkError.SessionExpired,
                is SdkError.SignedOut -> ShowingFromCacheReason.SessionExpired

                else -> ShowingFromCacheReason.ProcessingFailed
            }
        )
    }

    private fun Profile.toBarsProfile(): BarsProfile =
        BarsProfile(
            // "Иван Иванов" for the top bar, the full name has the patronymic too
            name = listOfNotNull(firstName, lastName).joinToString(" ")
                .ifEmpty { fullName.split("\\s+".toRegex()).take(2).joinToString(" ") },
            group = group.orEmpty(),
        )
}
