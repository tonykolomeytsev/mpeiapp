package kekmech.ru.core.dto

import io.realm.RealmObject

/**
 * Один раз при первом за семестр скачивании расписания чекаем
 * какая сейчас неделя на календаре, запоминаем её.
 * Также чекаем номер недели на сайте универа. По разности двух этих недель можно
 * далее определять четность недели.
 */
open class WeekInfo(
    var calendarWeek: Int? = null,
    var universityWeek: Int? = null
) : RealmObject()