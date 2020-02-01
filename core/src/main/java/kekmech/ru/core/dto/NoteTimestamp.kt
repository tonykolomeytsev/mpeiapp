package kekmech.ru.core.dto

class NoteTimestamp private constructor(private val ts: String) {

    val week by lazy { ts.substringBefore('d').substringAfter('w').toInt() }

    companion object {
        fun from(coupleNative: CoupleNative, weekOffset: Int): String {
            val coupleWeek = if (weekOffset == 0) Time.today() else Time.today().getDayWithOffset(weekOffset * 7)
            return "w${coupleWeek.weekOfYear}d${coupleNative.day}n${coupleNative.num}h${coupleNative.place.trim().hashCode()}"
        }

        fun from(string: String) = NoteTimestamp(string)
    }
}