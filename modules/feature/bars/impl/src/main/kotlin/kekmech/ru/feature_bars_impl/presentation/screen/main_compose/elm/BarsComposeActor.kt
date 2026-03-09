package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import com.kekmech.lib_bars.BarsHandle
import com.kekmech.lib_bars.auth.InitResult
import com.kekmech.lib_bars.user.Discipline
import kekmech.ru.ext_kotlin.capitalizeSafe
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_bars_impl.data.mapper.LibToMarksResponseMapper
import kekmech.ru.feature_bars_impl.data.repository.BarsRepository
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.presentation.screen.details.BarsDetailsFragment
import kekmech.ru.feature_bars_impl.presentation.screen.login.BarsLoginFragment
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.Account
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal.CheckAuthStatusFailure
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal.GetMarksFailure
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal.GetMarksSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal.GetProfileSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal.SubscribeAuthStatusSuccess
import kekmech.ru.lib_elm.actorFlow
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.Router
import kekmech.ru.lib_navigation.ShowDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent as Event

internal class BarsComposeActor(
    private val barsHandle: BarsHandle,
    private val appSettingsFeatureLauncher: AppSettingsFeatureLauncher,
    private val router: Router,
    private val barsRepository: BarsRepository,
) : Actor<Command, Event>() {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.SubscribeAuthStatus -> barsRepository.loginStateTrigger
                .map {
                    when (barsHandle.auth.init()) {
                        is InitResult.AlreadyLoggedIn -> AuthStatus.LoggedIn
                        is InitResult.AccountSelectionRequired,
                        is InitResult.LoginRequired -> AuthStatus.LoginRequired
                    }
                }.mapEvents(
                    eventMapper = ::SubscribeAuthStatusSuccess,
                    errorMapper = { e ->
                        val e = ExceptionWithId(e)
                        crashlytics.recordException(e.source) {
                            key("screen", "main")
                            key("stage", "auth.init")
                            key("exception_id", e.uuid)
                        }
                        CheckAuthStatusFailure(e)
                    },
                )

            is Command.GetProfile -> actorFlow {
                val acc = requireNotNull(barsRepository.getCurrentAccount()) {
                    "Unexpected state: selected account is null"
                }
                acc.toBarsProfile()
            }.mapEvents(
                eventMapper = ::GetProfileSuccess,
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "main")
                        key("stage", "getProfile")
                        key("exception_id", e.uuid)
                    }
                    CheckAuthStatusFailure(e)
                },
            )

            is Command.GetMarks -> flow {
                val cached = barsRepository.getSavedDisciplines()
                if (cached != null) {
                    emit(GetMarksSuccess(cached, true))
                }

                val actual = LibToMarksResponseMapper.map(barsHandle.user.getMarks().disciplines)
                barsRepository.saveDisciplines(actual.payload)
                emit(GetMarksSuccess(actual.payload, false))
            }.mapEvents(
                eventMapper = { it },
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "main")
                        key("stage", "getMarks")
                        key("exception_id", e.uuid)
                    }
                    GetMarksFailure(e)
                }
            )

            is Command.OpenAppSettings -> actorFlow {
                withContext(Dispatchers.Main) {
                    appSettingsFeatureLauncher.launch()
                }
            }.mapEvents()

            is Command.OpenLogin -> actorFlow {
                withContext(Dispatchers.Main) {
                    router.executeCommand(AddScreenForward { BarsLoginFragment() })
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

    private fun Account.toBarsProfile(): BarsProfile =
        BarsProfile(
            name = name.split("\\s+".toRegex()).take(2).joinToString(" "),
            group = group,
        )
}