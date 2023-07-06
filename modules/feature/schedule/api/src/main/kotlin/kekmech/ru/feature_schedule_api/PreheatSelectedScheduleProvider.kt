package kekmech.ru.feature_schedule_api

import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule

interface PreheatSelectedScheduleProvider {

    /**
     * @throws IllegalStateException If schedule isn't selected yet. This case can happen when
     * the application is first launched
     */
    fun getSelectedScheduleImmediately(): SelectedSchedule
}
