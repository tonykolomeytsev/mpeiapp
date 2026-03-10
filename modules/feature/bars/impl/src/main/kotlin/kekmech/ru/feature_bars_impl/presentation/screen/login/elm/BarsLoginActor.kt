package kekmech.ru.feature_bars_impl.presentation.screen.login.elm

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import com.kekmech.lib_bars.BarsHandle
import com.kekmech.lib_bars.auth.GetStudentListResult
import com.kekmech.lib_bars.auth.InitResult
import com.kekmech.lib_bars.auth.LoginData
import com.kekmech.lib_bars.auth.LoginResult
import com.kekmech.lib_bars.auth.RequestTwoFactorResult
import com.kekmech.lib_bars.auth.StudentEntry
import com.kekmech.lib_bars.auth.SubmitTwoFactorResult
import com.kekmech.lib_bars.auth.TwoFactorData
import com.kekmech.lib_bars.auth.TwoFactorProvider
import kekmech.ru.feature_bars_impl.data.repository.BarsRepository
import kekmech.ru.feature_bars_impl.domain.ExceptionWithId
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.CheckAuthStatusFailure
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.CheckAuthStatusSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.GetAccountsSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.LoginWithPasswordSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.RequestTwoFactorCodeSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.Submit2faCodeSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.SubscribeTwoFactorCodeTimerSuccess
import kekmech.ru.lib_elm.actorFlow
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.lib_navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent as Event

private const val TWO_FACTOR_DEBOUNCE_SEC = 30

internal class BarsLoginActor(
    private val barsHandle: BarsHandle,
    private val router: Router,
    private val barsRepository: BarsRepository,
) : Actor<Command, Event>() {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.CheckAuthStatus -> actorFlow {
                when (barsHandle.auth.init()) {
                    is InitResult.LoginRequired -> AuthStatus.LoginRequired
                    is InitResult.AccountSelectionRequired -> AuthStatus.AccountSelectionRequired
                    is InitResult.AlreadyLoggedIn -> AuthStatus.LoggedIn
                }
            }.mapEvents(
                eventMapper = ::CheckAuthStatusSuccess,
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "login")
                        key("stage", "auth.init")
                        key("exception_id", e.uuid)
                    }
                    CheckAuthStatusFailure(e)
                },
            )

            is Command.LoginWithPassword -> actorFlow {
                val loginResult = barsHandle.auth.login(
                    data = LoginData(
                        username = command.login,
                        password = command.password,
                        remember = false,
                    )
                )
                when (loginResult) {
                    is LoginResult.WrongCredentials -> LoginStatus.WrongCredentials
                    is LoginResult.Success -> LoginStatus.AccountSelectionRequired
                    is LoginResult.TwoFactorRequired -> LoginStatus.TwoFactorRequired(
                        defaultProvider = loginResult.defaultProvider.toDomain(),
                        providers = loginResult.availableProviders.map { it.toDomain() },
                    )
                }
            }.mapEvents(
                eventMapper = ::LoginWithPasswordSuccess,
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "login")
                        key("stage", "auth.login")
                        key("exception_id", e.uuid)
                    }
                    CheckAuthStatusFailure(e)
                },
            )

            is Command.RequestTwoFactorCode -> actorFlow {
                when (barsHandle.auth.requestTwoFactorCode(command.provider.toLib())) {
                    RequestTwoFactorResult.Error -> error("Unable to send code")
                    RequestTwoFactorResult.Success -> Unit
                }
            }.mapEvents(
                eventMapper = {
                    RequestTwoFactorCodeSuccess(
                        provider = command.provider,
                        debounceSec = TWO_FACTOR_DEBOUNCE_SEC,
                    )
                },
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "2fa")
                        key("stage", "auth.requestTwoFactorCode")
                        key("exception_id", e.uuid)
                    }
                    CheckAuthStatusFailure(e)
                },
            )

            is Command.SubscribeTwoFactorCodeTimer -> flow {
                for (i in TWO_FACTOR_DEBOUNCE_SEC downTo 0) {
                    emit(i)
                    delay(1000)
                }
            }.mapEvents(::SubscribeTwoFactorCodeTimerSuccess)

            is Command.Submit2faCode -> actorFlow {
                val result = barsHandle.auth.submitTwoFactorCode(
                    data = TwoFactorData(
                        username = command.login,
                        code = command.code,
                        remember = false,
                    ),
                )
                when (result) {
                    is SubmitTwoFactorResult.AccountSelectionRequired,
                    is SubmitTwoFactorResult.Success -> TwoFactorCodeStatus.AccountSelectionRequired

                    is SubmitTwoFactorResult.IncorrectCode -> TwoFactorCodeStatus.InvalidCode
                    is SubmitTwoFactorResult.Error -> error("Error while submitting code")
                }
            }.mapEvents(
                eventMapper = ::Submit2faCodeSuccess,
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "2fa")
                        key("stage", "auth.submitTwoFactorCode")
                        key("exception_id", e.uuid)
                    }
                    CheckAuthStatusFailure(e)
                },
            )

            is Command.GetAccounts -> actorFlow {
                val accounts = when (val result = barsHandle.auth.getStudentList()) {
                    is GetStudentListResult.Success -> result.list.map { it.toDomain() }
                    is GetStudentListResult.Error -> error("Error while getting accounts")
                }
                barsRepository.saveAccounts(accounts)
                accounts
            }.mapEvents(
                eventMapper = ::GetAccountsSuccess,
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "accounts")
                        key("stage", "auth.getStudentList")
                        key("exception_id", e.uuid)
                    }
                    CheckAuthStatusFailure(e)
                },
            )

            is Command.SubmitAccountId -> actorFlow {
                barsHandle.auth.selectStudentById(command.id)
                barsRepository.saveCurrentUserId(command.id)
                barsRepository.loginStateTrigger.emit(Unit)
                withContext(Dispatchers.Main) {
                    router.executeCommand(PopBackStack())
                }
            }.mapEvents(
                // infallible in this version on lib_bars
            )

            is Command.Exit -> actorFlow {
                withContext(Dispatchers.Main) {
                    router.executeCommand(PopBackStack())
                }
            }.mapEvents()
        }

    private fun TwoFactorProvider.toDomain(): CodeProvider =
        when (this) {
            TwoFactorProvider.MAX -> CodeProvider.MAX
            TwoFactorProvider.VK -> CodeProvider.VK
            TwoFactorProvider.TG -> CodeProvider.TG
        }

    private fun CodeProvider.toLib(): TwoFactorProvider =
        when (this) {
            CodeProvider.MAX -> TwoFactorProvider.MAX
            CodeProvider.VK -> TwoFactorProvider.VK
            CodeProvider.TG -> TwoFactorProvider.TG
        }

    private fun StudentEntry.toDomain(): Account =
        Account(
            id = id,
            name = name,
            group = group,
            status = status,
        )
}