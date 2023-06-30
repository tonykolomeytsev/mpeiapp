package kekmech.ru.mpeiapp.demo.screens.elmslie.elm

import kekmech.ru.common_elm.Resource
import vivid.money.elmslie.core.store.Store

typealias ElmDemoStore = Store<ElmDemoEvent, ElmDemoEffect, ElmDemoState>

data class ElmDemoState(
    val randomArgument: Int,
    val resource: Resource<String> = Resource.Loading,
)

sealed interface ElmDemoEvent {

    sealed interface Ui : ElmDemoEvent {

        object Init : Ui

        sealed interface Click : Ui {

            object TextButton : Click
        }
    }

    sealed interface Internal : ElmDemoEvent {

        data class LoadNewResourceSuccess(val string: String) : Internal
        data class LoadNewResourceFailure(val error: Throwable) : Internal
    }
}

sealed interface ElmDemoCommand {

    object LoadNewResource : ElmDemoCommand
}

sealed interface ElmDemoEffect
