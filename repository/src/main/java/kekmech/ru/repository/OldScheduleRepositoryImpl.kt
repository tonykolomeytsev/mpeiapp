package kekmech.ru.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.gson.GsonBuilder
import kekmech.ru.core.dto.*
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.repositories.OldScheduleRepository
import kekmech.ru.repository.gateways.State
import kekmech.ru.repository.gateways.StateFactory
import kekmech.ru.repository.room.AppDatabase
import kekmech.ru.repository.utils.HtmlToScheduleParser
import kekmech.ru.repository.utils.ParserCouple
import kekmech.ru.repository.utils.ParserSchedule
import kekmech.ru.repository.utils.SessionParser
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.util.*

class OldScheduleRepositoryImpl constructor(
    private val scheduleCacheGateway: ScheduleCacheGateway,
    private val appdb: AppDatabase,
    private val context: Context
) : OldScheduleRepository {

    private val sharedPreferences = context.getSharedPreferences("mpeix", Context.MODE_PRIVATE)
    private val gson = GsonBuilder().create()

    private val stateFactory = StateFactory(sharedPreferences, gson)
    private val sessionSchedule: State<AcademicSession> = stateFactory.build("session_schedule")

    override var isNeedToUpdateFeed = MutableLiveData<Boolean>().apply { value = false }

    override val scheduleId: Int get() = scheduleCacheGateway.scheduleId

    override val scheduleLiveData = MutableLiveData<Schedule>()
        get() {
            if (field.value == null) GlobalScope.launch(Dispatchers.IO) {
                val value = getSchedule(true)
                if (value != null) withContext(Dispatchers.Main) { field.value = value }
            }
            return field
        }

    override fun getSchedule(refresh: Boolean): Schedule? {
        return scheduleCacheGateway.getSchedule()
    }

    override fun getOffsetCouples(offset: Int, refresh: Boolean): List<CoupleNative> {
        val today = Time.today()
        val offsetDay = today.getDayWithOffset(offset)
        if (today.dayOfWeek + offset <= 7) {
            val couples = scheduleCacheGateway.getCouples(today.dayOfWeek + offset, offsetDay.parity == Time.Parity.ODD)
            when {
                couples == null -> return emptyList()
                couples.isEmpty() -> return listOf(CoupleNative.dayOff)
                else -> return couples
            }
        } else {
            val necessaryDayOfWeek = (today.dayOfWeek + offset) % 7
            val couples = scheduleCacheGateway.getCouples(necessaryDayOfWeek, offsetDay.parity == Time.Parity.ODD)
            when {
                couples == null -> return emptyList()
                couples.isEmpty() -> return listOf(CoupleNative.dayOff)
                else -> return couples
            }
        }
    }

    override fun saveSchedule(schedule: Schedule) {
        scheduleCacheGateway.newSchedule(schedule)
    }

    private val groupNumber = Transformations.map(scheduleLiveData) { it?.group?.toUpperCase(Locale.getDefault()) ?: "" }
    override fun getGroupNum(): LiveData<String> = groupNumber

    override fun getAllSchedules() = scheduleCacheGateway.getAllSchedules()

    override fun setCurrentScheduleId(id: Int) {
        scheduleCacheGateway.setCurrentScheduleId(id)
        GlobalScope.launch(Dispatchers.IO) {
            val value = getSchedule(true)
            if (value != null) withContext(Dispatchers.Main) { scheduleLiveData.value = value }
        }
    }

    override fun loadSessionLiveData(): LiveData<AcademicSession> {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                sessionSchedule %= loadSessionFromRemote()
            } catch (e: Exception) { e.printStackTrace() }
        }
        return sessionSchedule.liveData
    }

    override fun loadSessionFromRemote(): AcademicSession {
        val groupName = scheduleCacheGateway.getGroupNum().toUpperCase(Locale.getDefault())
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!


        val timetable = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupName)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(true)
            .post()

        val __EVENTVALIDATION = timetable.select("input[name=__EVENTVALIDATION]").attr("value")
        val __VIEWSTATE = timetable.select("input[name=__VIEWSTATE]").attr("value")
        val __EVENTARGUMENT = "0"
        val __VIEWSTATEGENERATOR = timetable.select("input[name=__VIEWSTATEGENERATOR]").attr("value")
        val __EVENTTARGET = timetable.select("div[class=mpei-tt-outer-wrap]").select("a").attr("href").let {
            ".*'(.*)'.*'.*'.*".toRegex().find(it)?.groups?.get(1)?.value ?: ""
        }
        val result = Jsoup.connect(timetable.location())
            .data("__EVENTVALIDATION", __EVENTVALIDATION)
            .data("__VIEWSTATE", __VIEWSTATE)
            .data("__EVENTARGUMENT", __EVENTARGUMENT)
            .data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
            .data("__EVENTTARGET", __EVENTTARGET)
            .followRedirects(true)
            .post()
        val scheduleTable = result.select("div[class=mpei-tt-grid-wrap]")
        return SessionParser().parse(scheduleTable)
    }

    override suspend fun loadScheduleFromRemote(groupName: String): Schedule {
        // загружаем страничку и вбиваем номер группы в форму
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!

        // вычисляем первый понедельник семестра и второй понедельник семестра
        val firstMonday = Time.firstSemesterDay().gotoMonday()
        val secondMonday = firstMonday.getDayWithOffset(7)

        val currentWeekPage = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupName)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(true)
            .post()
        // скрапим первую страничку
        val firstWeekHtml = currentWeekPage
            .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
            .html()
        val nextWeekHref = currentWeekPage
            .select("span[class*=mpei-galaktika-lessons-grid-nav]")
            .select("a")
            .last()
            .attr("href")

        val firstWeekSchedule = async { HtmlToScheduleParser().parse(firstWeekHtml) }
        val secondWeekShedule = async {
            // скрапим вторую страничку
            val secondWeekHtml = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/table.aspx$nextWeekHref")
                .get()
                .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
                .html()
            HtmlToScheduleParser().parse(secondWeekHtml)
        }

        // объединяем результаты парсинга
        val joinedCouples = mutableListOf<ParserCouple>()
        joinedCouples.addAll(firstWeekSchedule.await().couples.onEach { couple -> couple.week = 1 })
        joinedCouples.addAll(secondWeekShedule.await().couples.onEach { couple -> couple.week = 2 })
        val finalParserSchedule = ParserSchedule(
            joinedCouples
        )

        return Schedule(
            0,
            groupName.toUpperCase(Locale.getDefault()),
            Time.today().weekOfYear,
            Time.today().weekOfSemester, // deprecated
            finalParserSchedule.couples.map {
                CoupleNative(
                    0,
                    it.name,
                    it.teacher,
                    it.place,
                    it.timeStart,
                    it.timeEnd,
                    it.type,
                    it.num,
                    it.day,
                    it.week
                )
            },
            "sch_v2"
        )
    }

    override fun updateScheduleByGroupNum(schedule: Schedule) {
        val similarSchedule = appdb.scheduleDao().getByGroupNum(schedule.group)
        if (similarSchedule == null) {
            scheduleCacheGateway.newSchedule(schedule)

            Log.d("ScheduleRepository", "Schedule created")
        } else {
            // тут надо очистить все пары и добавить их заново, а у schedule поменять calendarWeek на свежий
            val scheduleId = similarSchedule.id
            appdb.coupleDao().deleteByScheduleId(scheduleId) // очищаем все пары
            // устанавливаем свежую дату
            similarSchedule.calendarWeek = schedule.calendarWeek
            similarSchedule.universityWeek = schedule.universityWeek
            // достаём новые пары
            val newCouples = schedule.coupleList.map { it.apply { this.scheduleId = scheduleId } }
            newCouples.forEach(appdb.coupleDao()::insert) // пишем новые пары в базу

            Log.d("ScheduleRepository", "Schedule updated")
        }
        GlobalScope.launch(Dispatchers.Main) { scheduleLiveData.value = schedule }
    }

    private fun<T> async(action: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = action)

    override fun isSchedulesEmpty(): Boolean {
        return appdb.scheduleDao().getAnySchedule() == null
    }

    override fun removeSchedule(schedule: ScheduleNative) {
        appdb.coupleDao().deleteByScheduleId(schedule.id)
        appdb.scheduleDao().delete(schedule)
    }
}