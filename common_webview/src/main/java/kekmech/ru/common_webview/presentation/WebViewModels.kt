package kekmech.ru.common_webview.presentation

import kekmech.ru.common_mvi.Feature

typealias WebViewFeature = Feature<WebViewState, WebViewEvent, WebViewEffect>

data class WebViewState(
    val url: String,
    val isLoading: Boolean = true
)

sealed class WebViewAction {
    data class TransferSecrets(val secrets: String) : WebViewAction()
}

sealed class WebViewEffect {
    object CloseScreen : WebViewEffect()
    data class LoadUrl(val url: String) : WebViewEffect()
}

sealed class WebViewEvent {

    sealed class Wish : WebViewEvent() {
        object Init : Wish()

        object Click {
            object OnBack : Wish()
            data class OnLink(val url: String) : Wish()
        }

        object Action {
            data class SecretsGranted(val secrets: String) : Wish()
            data class LoadingProgressChanged(val progress: Int) : Wish()
        }
    }

    sealed class News {
        object OnSecretsTransferSuccessfully : News()
        data class OnSecretsTransferFailure(val throwable: Throwable) : News()
    }
}