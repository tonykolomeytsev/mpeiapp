package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import io.github.tonykolomeytsev.mpeix.sdk.Profile
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_bars_impl.data.mapper.SdkToAssessedDisciplineMapper
import kekmech.ru.feature_bars_impl.data.repository.BarsProfileIdRepository
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.navigation.BarsLoginScreenKey
import kekmech.ru.feature_bars_impl.presentation.screen.details.BarsDetailsFragment
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Internal.CheckAuthStatusFailure
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Internal.GetMarksFailure
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Internal.GetMarksSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Internal.GetProfileSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent.Internal.SubscribeAuthStatusSuccess
import kekmech.ru.lib_elm.actorFlow
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ShowDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsComposeEvent as Event

@OptIn(ExperimentalCoroutinesApi::class)
internal class BarsComposeActor(
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

            // Profile and marks follow the current profile, so a new login needs no new subscription
            is Command.GetProfile -> barsProfileIdRepository.currentProfileId
                .filterNotNull()
                .flatMapLatest { sdk.bars.profile.profile(it) }
                .mapNotNull { it.value?.toBarsProfile() }
                .mapEvents(
                    eventMapper = ::GetProfileSuccess,
                    errorMapper = { CheckAuthStatusFailure(it.recorded("getProfile")) },
                )

            is Command.GetMarks -> barsProfileIdRepository.currentProfileId
                .filterNotNull()
                .flatMapLatest { sdk.bars.study.academicPerformance(it) }
                .mapNotNull { resource ->
                    val performance = resource.value ?: return@mapNotNull null
                    GetMarksSuccess(SdkToAssessedDisciplineMapper.map(performance.current.disciplines))
                }
                .mapEvents(
                    eventMapper = { it },
                    errorMapper = { GetMarksFailure(it.recorded("getMarks")) },
                )

            // Profile and marks are one page, so one refresh updates both
            is Command.Refresh -> actorFlow {
                barsProfileIdRepository.currentProfileId.value?.let { sdk.bars.study.refresh(it) }
            }.mapEvents()

            is Command.OpenAppSettings -> actorFlow {
                withContext(Dispatchers.Main) {
                    appSettingsFeatureLauncher.launch()
                }
            }.mapEvents()

            is Command.OpenLogin -> actorFlow {
                withContext(Dispatchers.Main) {
                    router.executeCommand(AddScreenForward(BarsLoginScreenKey))
                }
            }.mapEvents()

            is Command.OpenDetails -> actorFlow {
                withContext(Dispatchers.Main) {
                    router.executeCommand(ShowDialog {
                        BarsDetailsFragment.newInstance(command.discipline)
                    })
                }
            }.mapEvents()
        }

    private fun Throwable.recorded(stage: String): ExceptionWithId {
        val e = ExceptionWithId(this)
        crashlytics.recordException(e.source) {
            key("screen", "main")
            key("stage", stage)
            key("exception_id", e.uuid)
        }
        return e
    }

    private fun Profile.toBarsProfile(): BarsProfile =
        BarsProfile(
            name = fullName.split("\\s+".toRegex()).take(2).joinToString(" "),
            group = group.orEmpty(),
        )
}
