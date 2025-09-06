package kekmech.ru.feature_schedule_api

import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule

public interface PreheatSelectedScheduleProvider {

    /**
     * @throws IllegalStateException If schedule isn't selected yet. This case can happen when
     * the application is first launched
     */
    public fun getSelectedScheduleImmediately(): SelectedSchedule
}
