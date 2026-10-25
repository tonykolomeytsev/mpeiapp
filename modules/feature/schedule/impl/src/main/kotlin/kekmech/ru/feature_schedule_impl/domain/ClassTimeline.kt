package kekmech.ru.feature_schedule_impl.domain

import kekmech.ru.feature_schedule_api.domain.model.Classes
import java.time.LocalTime

/**
 * What is going on in one day at a given moment: the class that is on right now, the one that is next,
 * and when the picture changes. Shared by the home screen widgets and the lock screen notification.
 */
internal data class ClassTimeline(
    /** The class that has started and has not finished yet */
    val current: Classes?,
    /** The first class that has not started yet */
    val next: Classes?,
    /** Classes that have not finished yet, in the order of the day (includes [current] and [next]) */
    val remaining: List<Classes>,
    /** Next moment when [current] / [next] change, null when nothing changes until the end of the day */
    val changesAt: LocalTime?,
    /** There were classes today, and all of them are over */
    val isOver: Boolean,
    val isEmpty: Boolean,
) {
    /** The class to put in the center of a small widget: the current one, or the next one */
    val focus: Classes? get() = current ?: next

    companion object {

        fun of(classes: List<Classes>, now: LocalTime): ClassTimeline {
            val sorted = classes.sortedBy { it.time.start }
            val remaining = sorted.filter { it.time.end > now }
            val current = remaining.firstOrNull { it.time.start <= now }
            val next = remaining.firstOrNull { it.time.start > now }
            val changesAt = remaining
                .flatMap { listOf(it.time.start, it.time.end) }
                .filter { it > now }
                .minOrNull()
            return ClassTimeline(
                current = current,
                next = next,
                remaining = remaining,
                changesAt = changesAt,
                isOver = sorted.isNotEmpty() && remaining.isEmpty(),
                isEmpty = sorted.isEmpty(),
            )
        }
    }
}
