package kekmech.ru.mpeiapp.testing

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

/**
 * # MockBackend
 *
 * What the in-app mock backend returns, see `modules/mock_server`.
 *
 * The mock builds the weeks around today: the current week is the even one, the next week is odd.
 * Keep the assertions to what is the same on any day the tests run.
 */
object MockBackend {

    /**
     * The mock returns the same schedule for any group, this one is what it calls itself.
     * The search finds it too: the search results are real groups, see `assets/schedule/search.json`
     */
    const val GROUP = "Сэ-12-21"

    /**
     * A real teacher from the search results. The search by "Куликова" also finds
     * "Куликова Татьяна Александровна"
     */
    const val TEACHER = "Куликова Екатерина Александровна"

    /** Week of the semester of the current week, the next ones are counted from it */
    const val CURRENT_WEEK_OF_SEMESTER = 12

    /**
     * Whether the schedule opens the next week instead of the current one:
     * it does so on Saturday and Sunday, see `ScheduleReducer`
     */
    fun scheduleOpensNextWeek(): Boolean =
        LocalDate.now(ZoneId.of("Europe/Moscow")).dayOfWeek >= DayOfWeek.SATURDAY
}
