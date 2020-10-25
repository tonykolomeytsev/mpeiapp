package kekmech.ru.common_webview.presentation

import kekmech.ru.common_mvi.BaseFeature

internal object WebViewFeatureFactory {

    fun create(url: String): WebViewFeature = BaseFeature(
        initialState = WebViewState(url = url),
        reducer = WebViewReducer(),
        actor = WebViewActor()
    ).start()
}