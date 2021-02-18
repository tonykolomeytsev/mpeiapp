package kekmech.ru.common_webview.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.common_webview.presentation.WebViewEvent.News
import kekmech.ru.common_webview.presentation.WebViewEvent.Wish

internal class WebViewReducer : BaseReducer<WebViewState, WebViewEvent, WebViewEffect, WebViewAction> {

    override fun reduce(
        event: WebViewEvent,
        state: WebViewState
    ): Result<WebViewState, WebViewEffect, WebViewAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    @Suppress("MagicNumber")
    private fun reduceWish(
        event: Wish,
        state: WebViewState
    ): Result<WebViewState, WebViewEffect, WebViewAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            effect = WebViewEffect.LoadUrl(state.url)
        )
        is Wish.Action.SecretsGranted -> TODO()
        is Wish.Click.OnBack -> Result(
            state = state,
            effect = WebViewEffect.CloseScreen
        )
        is Wish.Click.OnLink -> TODO()
        is Wish.Action.LoadingProgressChanged -> Result(
            state = state.copy(isLoading = event.progress < 100)
        )
    }

    @Suppress("UnusedPrivateMember")
    private fun reduceNews(
        event: News,
        state: WebViewState
    ): Result<WebViewState, WebViewEffect, WebViewAction> = when (event) {
        is News.OnSecretsTransferFailure -> TODO()
        is News.OnSecretsTransferSuccessfully -> TODO()
    }
}