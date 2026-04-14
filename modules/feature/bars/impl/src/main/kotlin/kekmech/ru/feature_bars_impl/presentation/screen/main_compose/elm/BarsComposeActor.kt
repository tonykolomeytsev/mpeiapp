package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent as Event

internal class BarsComposeActor(
//    private val barsHandle: BarsHandle,
    private val appSettingsFeatureLauncher: AppSettingsFeatureLauncher,
    private val router: Router,
//    private val barsRepository: BarsRepository,
) : Actor<Command, Event>() {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun execute(command: Command): Flow<Event> = emptyFlow()
//        when (command) {
//            is Command.SubscribeAuthStatus -> barsRepository.loginStateTrigger
//                .map {
//                    when (barsHandle.auth.init()) {
//                        is InitResult.AlreadyLoggedIn -> AuthStatus.LoggedIn
//                        is InitResult.AccountSelectionRequired,
//                        is InitResult.LoginRequired -> AuthStatus.LoginRequired
//                    }
//                }.mapEvents(
//                    eventMapper = ::SubscribeAuthStatusSuccess,
//                    errorMapper = { e ->
//                        val e = ExceptionWithId(e)
//                        crashlytics.recordException(e.source) {
//                            key("screen", "main")
//                            key("stage", "auth.init")
//                            key("exception_id", e.uuid)
//                        }
//                        CheckAuthStatusFailure(e)
//                    },
//                )
//
//            is Command.GetProfile -> actorFlow {
//                val acc = requireNotNull(barsRepository.getCurrentAccount()) {
//                    "Unexpected state: selected account is null"
//                }
//                acc.toBarsProfile()
//            }.mapEvents(
//                eventMapper = ::GetProfileSuccess,
//                errorMapper = { e ->
//                    val e = ExceptionWithId(e)
//                    crashlytics.recordException(e.source) {
//                        key("screen", "main")
//                        key("stage", "getProfile")
//                        key("exception_id", e.uuid)
//                    }
//                    CheckAuthStatusFailure(e)
//                },
//            )
//
//            is Command.GetMarks -> flow {
//                val cached = barsRepository.getSavedDisciplines()
//                if (cached != null) {
//                    emit(GetMarksSuccess(cached, true))
//                }
//
//                val actual = LibToMarksResponseMapper.map(barsHandle.user.getMarks().disciplines)
//                barsRepository.saveDisciplines(actual.payload)
//                emit(GetMarksSuccess(actual.payload, false))
//            }.mapEvents(
//                eventMapper = { it },
//                errorMapper = { e ->
//                    val e = ExceptionWithId(e)
//                    crashlytics.recordException(e.source) {
//                        key("screen", "main")
//                        key("stage", "getMarks")
//                        key("exception_id", e.uuid)
//                    }
//                    GetMarksFailure(e)
//                }
//            )
//
//            is Command.OpenAppSettings -> actorFlow {
//                withContext(Dispatchers.Main) {
//                    appSettingsFeatureLauncher.launch()
//                }
//            }.mapEvents()
//
//            is Command.OpenLogin -> actorFlow {
//                withContext(Dispatchers.Main) {
//                    router.executeCommand(AddScreenForward { BarsLoginFragment() })
//                }
//            }.mapEvents()
//
//            is Command.OpenDetails -> actorFlow {
//                withContext(Dispatchers.Main) {
//                    router.executeCommand(ShowDialog {
//                        BarsDetailsFragment.newInstance(command.discipline)
//                    })
//                }
//            }.mapEvents()
//        }

//    private fun Account.toBarsProfile(): BarsProfile =
//        BarsProfile(
//            name = name.split("\\s+".toRegex()).take(2).joinToString(" "),
//            group = group,
//        )
}