package kekmech.ru.feature_schedule.main.helpers

import kekmech.ru.feature_schedule.main.item.DayItem
import java.time.LocalDate

object DayItemStateHelper {

    var selectedDay: DayItem = DayItem(LocalDate.now())
    private var clearSelectionSubscriber: () -> Unit = {}

    fun subscribeToClearSelection(subscriber: () -> Unit) {
        clearSelectionSubscriber = subscriber
    }

    fun clearOldSelectionAndRun(model: DayItem, action: () -> Unit) {
        clearSelectionSubscriber()
        clearSelectionSubscriber = {}
        this.selectedDay = model
        action()
    }
}