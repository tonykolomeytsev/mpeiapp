package kekmech.ru.feature_bars_impl.presentation.screen.main.elm

import android.webkit.CookieManager
import kekmech.ru.feature_bars_impl.domain.RemoteBarsConfig
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main.util.hasAuthCookie
import kekmech.ru.feature_bars_impl.presentation.screen.main.util.removeAuthCookie
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent as Event
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState as State

internal class BarsReducer :
    ScreenDslReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class
    ) {

    override fun Result.internal(event: Internal): Any =
        when (event) {
            is Internal.GetRemoteBarsConfigSuccess -> {
                state {
                    copy(
                        config = event.remoteBarsConfig,
                        extractJs = event.extractJs,
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects { +loadPageEffect { latestLoadedUrl ?: config?.loginUrl } }
            }
            is Internal.GetRemoteBarsConfigFailure -> state {
                copy(
                    isAfterErrorLoadingConfig = true,
                    isLoading = false
                )
            }
            is Internal.ObserveBarsSuccess -> {
                state {
                    copy(
                        userInfo = event.userBars,
                        isLoading = false,
                        isReturnBannerVisible = isBrowserVisible
                    )
                }
            }
            is Internal.GetLatestLoadedUrlSuccess -> state {
                copy(latestLoadedUrl = event.latestLoadedUrl)
            }
        }

    override fun Result.ui(event: Ui): Any =
        when (event) {
            is Ui.Init -> {
                state {
                    val actualFlowState =
                        if (CookieManager.getInstance().hasAuthCookie()) {
                            FlowState.LOGGED_IN
                        } else {
                            FlowState.NOT_LOGGED_IN
                        }
                    copy(flowState = actualFlowState)
                }
                commands {
                    +Command.GetLatestLoadedUrl
                    +Command.GetRemoteBarsConfig
                    +Command.ObserveBars
                }
            }
            is Ui.Action.PageStarted -> state {
                copy(
                    webViewUiState = webViewUiState.copy(isLoading = true)
                )
            }
            is Ui.Action.PageLoadingError -> {
                state {
                    copy(
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects { +Effect.ShowCommonError }
            }
            is Ui.Action.PageFinished -> handlePageFinished(event)
            is Ui.Action.Update -> {
                state {
                    copy(
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects {
                    if (state.config != null) {
                        +loadPageEffect { latestLoadedUrl ?: config?.loginUrl }
                    }
                }
            }
            is Ui.Action.ScrollToTop ->
                when {
                    state.isBrowserVisible -> state {
                        copy(
                            isBrowserVisible = false,
                            isReturnBannerVisible = false,
                        )
                    }
                    else -> effects { +Effect.ScrollToTop }
                }

            is Ui.Click.ShowBrowser -> state { copy(isBrowserVisible = true) }
            is Ui.Click.HideBrowser -> state {
                copy(
                    isBrowserVisible = false,
                    isReturnBannerVisible = false,
                )
            }
            is Ui.Click.SwipeToRefresh -> {
                state {
                    copy(
                        isLoading = true,
                        isAfterErrorLoadingConfig = false,
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                commands {
                    if (state.config == null) {
                        +Command.GetRemoteBarsConfig
                    }
                }
                effects { +loadPageEffect { latestLoadedUrl ?: config?.loginUrl } }
            }
            is Ui.Click.Settings -> effects { +Effect.OpenSettings }
            is Ui.Click.Login -> {
                state {
                    copy(
                        isBrowserVisible = true,
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects { +loadPageEffect { config?.loginUrl } }
            }
            is Ui.Click.NotAllowedUrl -> effects { +Effect.OpenExternalBrowser(event.url) }

            is Ui.Extract.StudentName -> commands { +Command.PushStudentName(event.name) }
            is Ui.Extract.StudentGroup -> commands { +Command.PushStudentGroup(event.group) }
            is Ui.Extract.MetaData -> Unit // TODO in future versions
            is Ui.Extract.Rating -> commands { +Command.PushStudentRating(event.ratingJson) }
            is Ui.Extract.Semesters -> Unit // TODO in future versions
            is Ui.Extract.Marks -> commands { +Command.PushMarks(event.marksJson) }
        }

    private fun Result.handlePageFinished(
        event: Ui.Action.PageFinished,
    ): Any {
        state.config ?: return Unit
        val hasAuthCookie = CookieManager.getInstance().hasAuthCookie()
        return when {
            isMarksListUrl(event.url, state.config) && hasAuthCookie -> {
                state {
                    copy(
                        flowState = FlowState.LOGGED_IN,
                        isLoading = false,
                        latestLoadedUrl = event.url,
                        webViewUiState = WebViewUiState(
                            url = event.url,
                            pageTitle = event.pageTitle,
                            isLoading = false
                        )
                    )
                }
                effects { +invokeExtractJsEffect(state) }
                commands { +Command.SetLatestLoadedUrl(event.url) }
            }
            hasAuthCookie && !event.url.contains("ReturnURL", ignoreCase = true) -> state {
                copy(
                    flowState = FlowState.LOGGED_IN,
                    isLoading = false,
                    isReturnBannerVisible = false,
                    webViewUiState = WebViewUiState(
                        url = event.url,
                        pageTitle = event.pageTitle,
                        isLoading = false
                    )
                )
            }
            else -> {
                state {
                    CookieManager.getInstance().removeAuthCookie()
                    copy(
                        userInfo = null,
                        flowState = FlowState.NOT_LOGGED_IN,
                        isLoading = false,
                        isReturnBannerVisible = false,
                        latestLoadedUrl = null,
                        webViewUiState = WebViewUiState(
                            url = event.url,
                            pageTitle = event.pageTitle,
                            isLoading = false
                        )
                    )
                }
                commands { +Command.SetLatestLoadedUrl(null) }
            }
        }
    }

    private fun invokeExtractJsEffect(state: State) =
        state.extractJs?.let(Effect::InvokeJs)

    private fun Result.loadPageEffect(urlSelector: State.() -> String?) =
        state.urlSelector()?.let(Effect::LoadPage)

    private fun isMarksListUrl(url: String, config: RemoteBarsConfig?) =
        if (config != null) url.startsWith(config.marksListUrl, ignoreCase = true) else false
}
