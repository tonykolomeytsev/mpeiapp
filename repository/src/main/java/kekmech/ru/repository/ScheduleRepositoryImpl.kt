package kekmech.ru.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kekmech.ru.core.dto.*
import kekmech.ru.core.gateways.ScheduleCacheGateway
import kekmech.ru.core.repositories.ScheduleRepository
import kekmech.ru.repository.room.AppDatabase
import kekmech.ru.repository.utils.SessionParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*

class ScheduleRepositoryImpl constructor(
    private val scheduleCacheGateway: ScheduleCacheGateway,
    private val appdb: AppDatabase
) : ScheduleRepository {

    override var isNeedToUpdateFeed = MutableLiveData<Boolean>().apply { value = false }

    override val scheduleId: Int get() = scheduleCacheGateway.scheduleId

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

    private val groupNumber = MutableLiveData<String>().apply { value = "" }
    override fun getGroupNum(): LiveData<String> {
        GlobalScope.launch(Dispatchers.Main) {
            groupNumber.value = withContext(Dispatchers.IO) {
                scheduleCacheGateway.getGroupNum().toUpperCase(Locale.getDefault())
            }
        }
        return groupNumber
    }

    override fun getAllSchedules() = scheduleCacheGateway.getAllSchedules()

    override fun setCurrentScheduleId(id: Int) = scheduleCacheGateway.setCurrentScheduleId(id)

    override fun loadSessionFromRemote(): AcademicSession {
        val groupName = scheduleCacheGateway.getGroupNum().toUpperCase(Locale.getDefault())
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!


        val href = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupName)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(false)
            .post()
            .select("a[href]")
            .first()
            .attr("href")

        val timetable = Jsoup.connect(href)
            .post()
        val __EVENTVALIDATION = timetable.select("input[name=__EVENTVALIDATION]").attr("value")
        val __VIEWSTATE = timetable.select("input[name=__VIEWSTATE]").attr("value")
        val __EVENTARGUMENT = "0"
        val __VIEWSTATEGENERATOR = timetable.select("input[name=__VIEWSTATEGENERATOR]").attr("value")
        val __EVENTTARGET = timetable.select("div[class=mpei-tt-outer-wrap]").select("a").attr("href").let {
            ".*'(.*)'.*'.*'.*".toRegex().find(it)?.groups?.get(1)?.value ?: ""
        }
        val result = Jsoup.connect(href)
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

    override fun updateScheduleFromRemote(groupName: String) {

    }

    override fun isSchedulesEmpty(): Boolean {
        return appdb.scheduleDao().getAnySchedule() == null
    }

    override fun removeSchedule(schedule: ScheduleNative) {
        appdb.scheduleDao().delete(schedule)
    }
}