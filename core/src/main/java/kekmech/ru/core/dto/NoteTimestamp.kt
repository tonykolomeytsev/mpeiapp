package kekmech.ru.core.dto

class NoteTimestamp private constructor() {
    companion object {
        fun from(coupleNative: CoupleNative, weekOffset: Int): String {
            val coupleWeek = if (weekOffset == 0) Time.today() else Time.today().getDayWithOffset(weekOffset * 7)
            return "w${coupleWeek.weekOfYear}d${coupleNative.day}n${coupleNative.num}h${coupleNative.place.trim().hashCode()}"
        }
    }
}