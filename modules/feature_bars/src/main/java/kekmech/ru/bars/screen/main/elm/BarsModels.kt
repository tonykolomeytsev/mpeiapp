package kekmech.ru.bars.screen.main.elm

import kekmech.ru.domain_bars.dto.RemoteBarsConfig

data class BarsState(
    val isLoggedIn: Boolean? = null,
    val config: RemoteBarsConfig? = null
)

sealed class BarsEvent {
    sealed class Wish : BarsEvent() {
        object Init : Wish()

        object Action {
            object Update : Wish()
        }
    }

    sealed class News : BarsEvent() {
        data class GetRemoteBarsConfigSuccess(val remoteBarsConfig: RemoteBarsConfig) : News()
        data class GetRemoteBarsConfigFailure(val throwable: Throwable) : News()
    }
}

sealed class BarsEffect {
    data class LoadPage(val url: String) : BarsEffect()
    data class InvokeJs(val js: String) : BarsEffect()
}

sealed class BarsAction {
    object GetRemoteBarsConfig : BarsAction()
}