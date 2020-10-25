package kekmech.ru.common_webview.presentation

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

internal class WebViewActor : Actor<WebViewAction, WebViewEvent> {

    override fun execute(action: WebViewAction): Observable<WebViewEvent> = when (action) {
        is WebViewAction.TransferSecrets -> TODO()
    }
}