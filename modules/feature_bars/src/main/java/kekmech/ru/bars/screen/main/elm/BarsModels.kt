package kekmech.ru.bars.screen.main.elm

import kekmech.ru.domain_bars.dto.RemoteBarsConfig

data class BarsState(
    val isLoggedIn: Boolean? = null,
    val config: RemoteBarsConfig? = null
)

sealed class BarsEvent {
    sealed class Wish : BarsEvent() {
        object Init : Wish()
    }

    sealed class News : BarsEvent() {

    }
}

sealed class BarsEffect {

}

sealed class BarsAction {

}