package kekmech.ru.domain_schedule

import kekmech.ru.domain_schedule.dto.SelectedSchedule

public interface PreheatSelectedScheduleProvider {

    /**
     * @throws IllegalStateException If schedule isn't selected yet. This case can happen when
     * the application is first launched
     */
    public fun getSelectedScheduleImmediately(): SelectedSchedule
}
