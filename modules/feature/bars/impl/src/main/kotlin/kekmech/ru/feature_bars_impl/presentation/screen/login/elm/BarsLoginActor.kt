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
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.GetAccountsFailure
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.GetAccountsSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.LoginWithPasswordFailure
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.LoginWithPasswordSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.RequestTwoFactorCodeFailure
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.RequestTwoFactorCodeSuccess
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal.Submit2faCodeFailure
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
import kotlinx.coroutines.withTimeoutOrNull
import money.vivid.elmslie.core.store.Actor
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent as Event

private const val TWO_FACTOR_DEBOUNCE_SEC = 30

/**
 * bars.mpei.ru is unstable and sometimes accepts a TCP connection but never responds.
 * lib_bars has no call timeout of its own, so without this guard the login screen
 * would show an endless loading state.
 */
private const val BARS_REQUEST_TIMEOUT_MS = 30_000L

internal class BarsLoginActor(
    private val barsHandle: BarsHandle,
    private val router: Router,
    private val barsRepository: BarsRepository,
) : Actor<Command, Event>() {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()
    private var activeRequestCodeDeferred: kotlinx.coroutines.CompletableDeferred<Unit>? = null

    override fun execute(command: Command): Flow<Event> =
        when (command) {
            is Command.CheckAuthStatus -> actorFlow {
                timber.log.Timber.d("BarsLoginActor: Starting auth.init()")
                val initResult = withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                    barsHandle.auth.init()
                } ?: error("BARS request timeout: auth.init")
                timber.log.Timber.d("BarsLoginActor: Init result: ${initResult.javaClass.simpleName}")
                when (initResult) {
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
                timber.log.Timber.d("BarsLoginActor: Starting login with username=${command.login}")
                val loginResult = withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                    timber.log.Timber.d("BarsLoginActor: Calling barsHandle.auth.login()")
                    barsHandle.auth.login(
                        data = LoginData(
                            username = command.login,
                            password = command.password,
                            remember = false,
                        )
                    )
                } ?: error("BARS request timeout: auth.login")
                timber.log.Timber.d("BarsLoginActor: Login result: ${loginResult.javaClass.simpleName}")
                when (loginResult) {
                    is LoginResult.WrongCredentials -> LoginStatus.WrongCredentials
                    is LoginResult.Success -> LoginStatus.AccountSelectionRequired
                    is LoginResult.TwoFactorRequired -> {
                        val session = kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder.currentSession
                        val defaultProvider = session?.defaultProvider ?: loginResult.defaultProvider.toDomain()
                        val providers = (
                            session?.availableProviders
                                ?: (loginResult.availableProviders.map { it.toDomain() } + listOf(defaultProvider))
                            )
                            .distinct()
                            .sortedByDescending { it == defaultProvider }

                        LoginStatus.TwoFactorRequired(
                            defaultProvider = defaultProvider,
                            providers = providers,
                            serverMessage = session?.lastServerMessage,
                        )
                    }
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
                    LoginWithPasswordFailure(e)
                },
            )

            is Command.RequestTwoFactorCode -> actorFlow {
                val deferred = kotlinx.coroutines.CompletableDeferred<Unit>()
                activeRequestCodeDeferred = deferred
                val isTotp = command.provider == CodeProvider.TOTP
                kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder.setActiveProvider(command.provider)
                try {
                    val requestResult = withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                        barsHandle.auth.requestTwoFactorCode(command.provider.toLib())
                    } ?: error("BARS request timeout: auth.requestTwoFactorCode")
                    when (requestResult) {
                        RequestTwoFactorResult.Error -> error("Unable to send code")
                        RequestTwoFactorResult.Success -> Unit
                    }
                    if (isTotp) {
                        kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder.markTotpInitialized()
                    }
                    deferred.complete(Unit)
                    kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder.currentSession?.lastServerMessage
                } catch (e: Throwable) {
                    deferred.completeExceptionally(e)
                    throw e
                }
            }.mapEvents(
                eventMapper = { serverMsg ->
                    RequestTwoFactorCodeSuccess(
                        provider = command.provider,
                        debounceSec = TWO_FACTOR_DEBOUNCE_SEC,
                        serverMessage = serverMsg,
                    )
                },
                errorMapper = { e ->
                    val e = ExceptionWithId(e)
                    crashlytics.recordException(e.source) {
                        key("screen", "2fa")
                        key("stage", "auth.requestTwoFactorCode")
                        key("exception_id", e.uuid)
                    }
                    RequestTwoFactorCodeFailure(e)
                },
            )

            is Command.SubscribeTwoFactorCodeTimer -> flow {
                for (i in TWO_FACTOR_DEBOUNCE_SEC downTo 0) {
                    emit(i)
                    delay(1000)
                }
            }.mapEvents(::SubscribeTwoFactorCodeTimerSuccess)

            is Command.Submit2faCode -> actorFlow {
                runCatching { activeRequestCodeDeferred?.await() }
                val session = kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder.currentSession
                if (session?.isTotp == true && !session.isTotpInitialized) {
                    val initResult = withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                        barsHandle.auth.requestTwoFactorCode(TwoFactorProvider.TG)
                    }
                    if (initResult == RequestTwoFactorResult.Success) {
                        kekmech.ru.feature_bars_impl.data.network.BarsTwoFactorSessionHolder.markTotpInitialized()
                    }
                }
                val result = withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                    barsHandle.auth.submitTwoFactorCode(
                        data = TwoFactorData(
                            username = command.login,
                            code = command.code,
                            remember = false,
                        ),
                    )
                } ?: error("BARS request timeout: auth.submitTwoFactorCode")
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
                    Submit2faCodeFailure(e)
                },
            )

            is Command.GetAccounts -> actorFlow {
                val listResult = withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                    barsHandle.auth.getStudentList()
                } ?: error("BARS request timeout: auth.getStudentList")
                val accounts = when (val result = listResult) {
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
                    GetAccountsFailure(e)
                },
            )

            is Command.SubmitAccountId -> actorFlow {
                withTimeoutOrNull(BARS_REQUEST_TIMEOUT_MS) {
                    barsHandle.auth.selectStudentById(command.id)
                } ?: error("BARS request timeout: auth.selectStudentById")
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
            TwoFactorProvider.TG -> CodeProvider.TOTP
        }

    private fun CodeProvider.toLib(): TwoFactorProvider =
        when (this) {
            CodeProvider.MAX -> TwoFactorProvider.MAX
            CodeProvider.VK -> TwoFactorProvider.VK
            CodeProvider.TG -> TwoFactorProvider.TG
            // Map TOTP and EMAIL to TG (slot 1) - BarsTwoFactorCompatInterceptor rewrites tid=1 to tid=5 on the wire
            CodeProvider.TOTP -> TwoFactorProvider.TG
            CodeProvider.EMAIL -> TwoFactorProvider.TG
        }

    private fun StudentEntry.toDomain(): Account =
        Account(
            id = id,
            name = name,
            group = group,
            status = status,
        )
}