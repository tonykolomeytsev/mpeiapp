package kekmech.ru.core.dto

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import kotlin.coroutines.suspendCoroutine

data class Schedule(
    var id: Int = 0,
    var calendarWeek: Int? = null,
    var universityWeek: Int? = null,
    var coupleList: List<CoupleNative>? = null,
    val name: String? = null
)