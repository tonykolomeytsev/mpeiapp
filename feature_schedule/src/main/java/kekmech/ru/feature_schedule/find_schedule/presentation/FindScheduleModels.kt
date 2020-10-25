package kekmech.ru.feature_schedule.find_schedule.presentation

import kekmech.ru.common_mvi.Feature

internal typealias FindScheduleFeature = Feature<FindScheduleState, FindScheduleEvent, FindScheduleEffect>

internal data class FindScheduleState(
    val continueTo: String,
    val isLoading: Boolean = false,
    val isContinueButtonEnabled: Boolean = false
)

internal sealed class FindScheduleEvent {

    sealed class Wish : FindScheduleEvent() {

        object Init : Wish()

        object Click {
            data class Continue(val groupName: String) : Wish()
        }

        object Action {
            data class GroupNumberChanged(val groupName: String) : Wish()
        }
    }

    sealed class News : FindScheduleEvent() {
        data class GroupLoadedSuccessfully(val groupName: String) : News()
        data class GroupLoadingError(val throwable: Throwable) : News()
    }
}

internal sealed class FindScheduleEffect {
    object ShowError : FindScheduleEffect()
    object ShowSomethingWentWrongError : FindScheduleEffect()
    data class NavigateNextFragment(val continueTo: String) : FindScheduleEffect()
}

internal sealed class FindScheduleAction {
    data class FindGroup(val groupName: String) : FindScheduleAction()
    data class SelectGroup(val groupName: String) : FindScheduleAction()
}