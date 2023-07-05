package kekmech.ru.library_schedule.utils

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.ext_kotlin.moscowLocalTime
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.library_schedule.items.LunchItem
import kekmech.ru.library_schedule.items.NotePreview
import kekmech.ru.library_schedule.items.WindowItem
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

private const val CLASSES_BEFORE_LUNCH_NUMBER = 2
private const val CLASSES_AFTER_LUNCH_NUMBER = 3

/**
 * Insert notes preview items after classes items and return new list
 */
fun List<Any>.withNotePreview(): List<Any> = mutableListOf<Any>().apply {
    val raw = this@withNotePreview
    for (e in raw) {
        add(e)
        val classes = e as? Classes ?: continue
        classes.attachedNotePreview?.let { notePreviewContent ->
            add(NotePreview(notePreviewContent, linkedClasses = e, classes.progress))
        }
    }
}

/**
 * Insert lunch item between second and third classes (if exists) and return new list
 */
fun List<Any>.withLunch(): List<Any> = mutableListOf<Any>().apply {
    val raw = this@withLunch
    val hasSecondAndThirdClasses =
        raw.any { it is Classes && it.number == CLASSES_BEFORE_LUNCH_NUMBER } &&
                raw.any { it is Classes && it.number == CLASSES_AFTER_LUNCH_NUMBER }
    if (hasSecondAndThirdClasses) {
        val indexOfLastSecondClasses = raw.indexOfLast { it is Classes && it.number == 2 }
        raw.forEachIndexed { index, e ->
            add(e)
            if (index == indexOfLastSecondClasses) {
                add(LunchItem)
            }
        }
    } else {
        addAll(raw)
    }
}

/**
 * Insert window item between classes with numbers N and N + x, (x >= 2) and return new list
 */
fun List<Classes>.withWindows(): List<Any> = mutableListOf<Any>().apply {
    val raw = this@withWindows
    if (raw.size > 1) {
        add(raw.first())
        for (i in 1 until raw.size) {
            val currClasses = raw[i]
            val prevClasses = raw[i - 1]

            if (currClasses.number - prevClasses.number > 1) {
                add(WindowItem(prevClasses.time.end, currClasses.time.start))
            }
            add(currClasses)
        }
    } else {
        addAll(raw)
    }
}

/**
 * Indicate progress for those classes who need it, return new list
 *
 * @param nowDate current date at the time of processing
 * @param nowTime current time of processing
 */
@Suppress("NestedBlockDepth")
fun List<Any>.withProgressPreview(
    nowTime: LocalTime = moscowLocalTime(),
    nowDate: LocalDate = moscowLocalDate(),
): List<Any> {
    if (nowDate != moscowLocalDate()) return this
    return map { item ->
        if (item is Classes) {
            val time = item.time
            if (nowTime in time.start..time.end) {
                val classesDuration = time.start.until(time.end, ChronoUnit.SECONDS)
                val elapsedTime = time.start.until(nowTime, ChronoUnit.SECONDS)
                if (classesDuration > 0) {
                    item.copy(progress = elapsedTime.toFloat() / classesDuration.toFloat())
                } else item
            } else item
        } else item
    }
}
