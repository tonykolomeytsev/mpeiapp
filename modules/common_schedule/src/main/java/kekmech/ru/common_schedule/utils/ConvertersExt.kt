package kekmech.ru.common_schedule.utils

import kekmech.ru.common_android.moscowLocalTime
import kekmech.ru.common_kotlin.addIf
import kekmech.ru.common_schedule.items.LunchItem
import kekmech.ru.common_schedule.items.NotePreview
import kekmech.ru.common_schedule.items.WindowItem
import kekmech.ru.domain_schedule.dto.Classes
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
            add(NotePreview(notePreviewContent, linkedClasses = e))
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
            addIf(LunchItem) { index == indexOfLastSecondClasses }
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
            addIf(
                WindowItem(prevClasses.time.end, currClasses.time.start)
            ) { currClasses.number - prevClasses.number > 1 }
            add(currClasses)
        }
    } else {
        addAll(raw)
    }
}

/**
 * Indicate progress for those classes who need it, return the same list
 * IT IS IN-PLACE FUNCTION!
 */
@Suppress("NestedBlockDepth")
fun List<Any>.withProgressPreview(
    now: LocalTime = moscowLocalTime()
): List<Any> {
    var isProgressIndicated = false
    val iterator = iterator()
    while (!isProgressIndicated && iterator.hasNext()) {
        when (val item = iterator.next()) {
            is Classes -> {
                val time = item.time
                if (now in time.start..time.end) {
                    val classesDuration = time.start.until(time.end, ChronoUnit.SECONDS)
                    val elapsedTime = time.start.until(now, ChronoUnit.SECONDS)
                    if (classesDuration > 0) {
                        item.progress = elapsedTime.toFloat() / classesDuration.toFloat()
                    }
                    isProgressIndicated = true
                }
            }
            else -> Unit
        }
    }
    return this
}