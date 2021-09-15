package kekmech.ru.bars.screen.main.elm

import android.webkit.CookieManager
import kekmech.ru.bars.screen.main.elm.BarsEvent.News
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
import kekmech.ru.bars.screen.main.util.hasAuthCookie
import kekmech.ru.bars.screen.main.util.removeAuthCookie
import kekmech.ru.domain_bars.dto.RemoteBarsConfig
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

internal class BarsReducer : ScreenDslReducer<
        BarsEvent, Wish, News,
        BarsState,
        BarsEffect,
        BarsAction>(Wish::class, News::class) {

    override fun Result.internal(event: News): Any =
        when (event) {
            is News.GetRemoteBarsConfigSuccess -> {
                state {
                    copy(
                        config = event.remoteBarsConfig,
                        extractJs = event.extractJs,
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects { +loadPageEffect { latestLoadedUrl ?: config?.loginUrl } }
            }
            is News.GetRemoteBarsConfigFailure -> state {
                copy(
                    isAfterErrorLoadingConfig = true,
                    isLoading = false
                )
            }
            is News.ObserveBarsSuccess -> {
                state {
                    copy(
                        userInfo = event.userBars,
                        isLoading = false,
                        isReturnBannerVisible = isBrowserVisible
                    )
                }
            }
            is News.GetLatestLoadedUrlSuccess -> state {
                copy(latestLoadedUrl = event.latestLoadedUrl)
            }
        }

    override fun Result.ui(event: Wish): Any =
        when (event) {
            is Wish.Init -> {
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
                    +BarsAction.GetLatestLoadedUrl
                    +BarsAction.GetRemoteBarsConfig
                    +BarsAction.ObserveBars
                }
            }
            is Wish.Action.PageStarted -> state {
                copy(
                    webViewUiState = webViewUiState.copy(isLoading = true)
                )
            }
            is Wish.Action.PageLoadingError -> {
                state {
                    copy(
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects { +BarsEffect.ShowCommonError }
            }
            is Wish.Action.PageFinished -> handlePageFinished(event)
            is Wish.Action.Update -> {
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

            is Wish.Click.ShowBrowser -> state { copy(isBrowserVisible = true) }
            is Wish.Click.HideBrowser -> state {
                copy(
                    isBrowserVisible = false,
                    isReturnBannerVisible = false,
                )
            }
            is Wish.Click.SwipeToRefresh -> {
                state {
                    copy(
                        isLoading = true,
                        isAfterErrorLoadingConfig = false,
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                commands {
                    if (state.config == null) {
                        +BarsAction.GetRemoteBarsConfig
                    }
                }
                effects { +loadPageEffect { latestLoadedUrl ?: config?.loginUrl } }
            }
            is Wish.Click.Settings -> effects { +BarsEffect.OpenSettings }
            is Wish.Click.Login -> {
                state {
                    copy(
                        isBrowserVisible = true,
                        webViewUiState = webViewUiState.copy(isLoading = true)
                    )
                }
                effects { +loadPageEffect { config?.loginUrl } }
            }
            is Wish.Click.NotAllowedUrl -> effects { +BarsEffect.OpenExternalBrowser(event.url) }

            is Wish.Extract.StudentName -> commands { +BarsAction.PushStudentName(event.name) }
            is Wish.Extract.StudentGroup -> commands { +BarsAction.PushStudentGroup(event.group) }
            is Wish.Extract.MetaData -> Unit // TODO in future versions
            is Wish.Extract.Rating -> commands { +BarsAction.PushStudentRating(event.ratingJson) }
            is Wish.Extract.Semesters -> Unit // TODO in future versions
            is Wish.Extract.Marks -> commands { +BarsAction.PushMarks(event.marksJson) }
        }

    private fun Result.handlePageFinished(
        event: Wish.Action.PageFinished,
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
                commands { +BarsAction.SetLatestLoadedUrl(event.url) }
            }
            hasAuthCookie -> state {
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
                commands { +BarsAction.SetLatestLoadedUrl(null) }
            }
        }
    }

    private fun invokeExtractJsEffect(state: BarsState) =
        state.extractJs?.let(BarsEffect::InvokeJs)

    private fun Result.loadPageEffect(urlSelector: BarsState.() -> String?) =
        state.urlSelector()?.let(BarsEffect::LoadPage)

    private fun isMarksListUrl(url: String, config: RemoteBarsConfig?) =
        if (config != null) url.startsWith(config.marksListUrl, ignoreCase = true) else false
}