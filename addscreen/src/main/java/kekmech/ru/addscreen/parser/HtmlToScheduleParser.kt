package kekmech.ru.addscreen.parser

import android.annotation.SuppressLint
import kekmech.ru.core.dto.Time
import java.text.SimpleDateFormat
import java.util.*

class HtmlToScheduleParser {
    private val coupleBuilder = CoupleBuilder()
    private val scheduleBuilder = ScheduleBuilder()

    fun parse(html: String): ParserSchedule {
        val table = SCHEDULE_TABLE
            .find(html)
            .let { it?.groups?.toList() ?: emptyList() }
            .find { it?.value?.startsWith("<table") ?: false }
            ?.value ?: throw IllegalArgumentException("Unable to find <table> with 'mpei-galaktika-lessons-grid-tbl' class")
        "<tr>.*?</tr>".toRegex()
            .findAll(table)
            .iterator()
            .forEach {
                parseTr(it)
            }
        return scheduleBuilder.build()
    }

    private fun parseTr(matchResult: MatchResult) {
        val group = matchResult.groups[0]!!.value
        when {
            group.matches(WEEK_INFO) -> WEEK_INFO
                .findGroupsIn(group)[1]
                .value
                .let { pushWeekInfo(it) }
            group.matches(DAY_INFO) -> DAY_INFO
                .findGroupsIn(group)
                .first { it.value.matches(DAY_INFO_DATE) }
                .value
                .let { pushDayInfo(it) }
            group.matches(COUPLE_INFO) -> pushCoupleInfo(COUPLE_INFO.findGroupsIn(group))
        }
    }

    private fun pushCoupleInfo(findGroupsIn: List<MatchGroup>) {
        var timeInfo = ""
        var metaInfo = ""
        // тут вообще всего два элемента должно быть всегда
        // в одном из них лежит время пары, в другом подробности
        findGroupsIn.forEach {
            if (it.value.matches(COUPLE_INFO_TIME))
                timeInfo = it.value
            else if (!it.value.matches(COUPLE_INFO))
                metaInfo = it.value
        }
        //println("COUPLE INFO: $timeInfo $metaInfo")
        createCouple(timeInfo, metaInfo)
    }

    private fun createCouple(timeInfo: String, metaInfo: String) {
        val groups = COUPLE_INFO_TIME
            .findGroupsIn(timeInfo)
        coupleBuilder.timeStart = groups[1].value
        coupleBuilder.timeEnd = groups[2].value

        coupleBuilder.name = COUPLE_NAME.findGroupsIn(metaInfo)[1].value
        coupleBuilder.place = COUPLE_PLACE.findGroupsIn(metaInfo)[1].value
        coupleBuilder.teacher = COUPLE_TEACHER.findGroupsIn(metaInfo)[1].value
        val type = COUPLE_TYPE.findGroupsIn(metaInfo)[1].value.toUpperCase()
        coupleBuilder.type = when {
            type.contains("ЛЕК") -> ParserCouple.LECTURE
            type.contains("ЛАБ") -> ParserCouple.LAB
            type.contains("ПРАК") -> ParserCouple.PRACTICE
            type.contains("КУРС") -> ParserCouple.COURSE
            else -> ParserCouple.UNKNOWN
        }
        coupleBuilder.num = when {
            coupleBuilder.timeStart.contains("9:20") -> 1
            coupleBuilder.timeStart == "11:10" -> 2
            coupleBuilder.timeStart == "13:45" -> 3
            coupleBuilder.timeStart == "15:35" -> 4
            coupleBuilder.timeStart.contains("17") -> 5
            else -> -1
        }
        scheduleBuilder.putCouple(coupleBuilder.build())
    }

    // найден заголовок недели
    @SuppressLint("DefaultLocale")
    private fun pushWeekInfo(weekInfo: String) {
        //println("WEEK INFO: $weekInfo")
        coupleBuilder.week = when (coupleBuilder.week) {
            -1 -> 1
            1 -> 2
            else -> throw IllegalArgumentException("Something went wrong")
        }

        if (scheduleBuilder.firstCoupleDay == null) {
            val parser: (String) -> Date = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())::parse
            val nextWeek = "start=(\\d+.\\d+.\\d+)".toRegex()
                .findGroupsIn(weekInfo)
                .last { it.value.matches("\\d+.\\d+.\\d+".toRegex()) }
                .let { parser(it.value) }
                .let { Time(it) }
            val currentWeek = nextWeek.getDayWithOffset(-7)
            scheduleBuilder.firstCoupleDay = currentWeek.calendar
            println("FIRST COUPLE DAY: ${currentWeek.dayOfMonth}.${currentWeek.month + 1}.${currentWeek.year}")
        }
    }

    // найден заголовок дня
    @SuppressLint("DefaultLocale")
    private fun pushDayInfo(dayInfo: String) {
        //println("DAY_INFO: $dayInfo")
        coupleBuilder.day = getDayNumByName(dayInfo.toUpperCase())
    }

    private fun getMonthNumByName(month: String): Int = when {
        month.contains("СЕН") -> Calendar.SEPTEMBER
        month.contains("ОКТ") -> Calendar.OCTOBER
        month.contains("ДЕК") -> Calendar.DECEMBER
        month.contains("ЯНВ") -> Calendar.JANUARY
        month.contains("ФЕВ") -> Calendar.FEBRUARY
        month.contains("МАР") -> Calendar.MARCH
        month.contains("АПР") -> Calendar.APRIL
        month.contains("МАЙ") -> Calendar.MAY
        month.contains("ИЮН") -> Calendar.JUNE
        month.contains("ИЮЛ") -> Calendar.JULY
        month.contains("АВГ") -> Calendar.AUGUST
        else -> throw IllegalArgumentException("Something went wrong with month")
    }

    private fun getDayNumByName(day: String): Int = when {
        day.contains("ПН") ->   Calendar.MONDAY
        day.contains("ПОН") ->  Calendar.MONDAY
        day.contains("ВТ") ->   Calendar.TUESDAY
        day.contains("СР") ->   Calendar.WEDNESDAY
        day.contains("ЧТ") ->   Calendar.THURSDAY
        day.contains("ЧЕТ") ->  Calendar.THURSDAY
        day.contains("ПТ") ->   Calendar.FRIDAY
        day.contains("ПЯТ") ->  Calendar.FRIDAY
        day.contains("СБ") ->   Calendar.SATURDAY
        day.contains("СУБ") ->  Calendar.SATURDAY
        day.contains("ВС") ->   Calendar.SUNDAY
        day.contains("ВОСК") -> Calendar.SUNDAY
        else -> throw IllegalArgumentException("Something went wrong with days of week")
    }

    private fun Regex.findGroupsIn(group: String) = this
        .find(group)
        .let { it?.groups?.toMutableList() }!!
        .filterNotNull()

    companion object {
        val SCHEDULE_TABLE = "<table.+?mpei-galaktika-lessons-grid-tbl.*?</table>".toRegex()
        val WEEK_INFO = ".*<td.+?mpei-galaktika-lessons-grid-week.*?>(.+?)</td>.*".toRegex()
        val WEEK_INFO_DATE = ".*?(\\d+)(.*?)(\\d+).*?(\\d+)(.*?)(\\d+).*?".toRegex()
        val WEEK_INFO_DATE_DAY = ".*?(\\d{1,2})[^0-9]*.*?".toRegex()
        val WEEK_INFO_DATE_MONTH = "[^0-9]+".toRegex()
        val WEEK_INFO_DATE_YEAR = "\\d{4}".toRegex()
        val DAY_INFO = ".*<td.+?mpei-galaktika-lessons-grid-date.*?>(.+?)</td>.*".toRegex()
        val DAY_INFO_DATE = "([^>]+){2}\\s(\\d+).*?([^<]+)".toRegex()
        val COUPLE_INFO = ".*<td.+?mpei-galaktika-lessons-grid-time.*?>(.+?)</td>.*<td.+?mpei-galaktika-lessons-grid-day.*?>(.+?)</td>.*".toRegex()
        val COUPLE_INFO_TIME = "(\\d+:\\d+).*?(\\d+:\\d+)".toRegex()
        val COUPLE_NAME = ".*?<span.*?name.*?>(.*?)</span>.*".toRegex()
        val COUPLE_TYPE = ".*?<span.*?type.*?>(.*?)</span>.*".toRegex()
        val COUPLE_PLACE = ".*?<span.*?room.*?>(.*?)</span>.*".toRegex()
        val COUPLE_TEACHER = ".*?<span.*?pers.*?>(.*?)</span>.*".toRegex()
    }

    class CoupleBuilder {
        fun build() = ParserCouple(name, teacher, place, timeStart, timeEnd, type, num, day, week)

        var name: String = ""        // название предмета
        var teacher: String = ""     // ФИО препода
        var place: String = ""       // место проведения
        var timeStart: String = ""   // время начала
        var timeEnd: String = ""     // время конца
        var type: String = ""        // тип занятия
        var num: Int = 0             // номер пары
        var day: Int = 0             // день проведения
        var week: Int = -1           // ODD\EVEN\BOTH
    }

    class ScheduleBuilder {
        private var couples = mutableListOf<ParserCouple>()

        fun build() = ParserSchedule(couples, Calendar.getInstance())

        var firstCoupleDay: Calendar? = null

        fun putCouple(couple: ParserCouple) = couples.add(couple)
    }
}