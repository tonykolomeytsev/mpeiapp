package kekmech.ru.feature_schedule.main.helpers

class DayItemStateHelper {

    private var clearSelectionSubscriber: () -> Unit = {}

    fun subscribeToClearSelection(subscriber: () -> Unit) {
        clearSelectionSubscriber = subscriber
    }

    fun clearOldSelectionAndRun(action: () -> Unit) {
        clearSelectionSubscriber()
        clearSelectionSubscriber = {}
        action()
    }
}