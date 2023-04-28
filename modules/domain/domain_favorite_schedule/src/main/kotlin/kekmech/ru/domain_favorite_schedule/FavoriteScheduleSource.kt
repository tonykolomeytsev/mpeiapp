package kekmech.ru.domain_favorite_schedule

import kekmech.ru.common_app_database.AppDatabase
import kekmech.ru.common_app_database.dto.Record
import kekmech.ru.common_kotlin.fromBase64
import kekmech.ru.common_kotlin.toBase64
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType

class FavoriteScheduleSource(
    private val db: AppDatabase,
) {

    fun getAll() = db.fetch("select * from favorite_schedules;").map(::map)

    fun add(favoriteSchedule: FavoriteSchedule) {
        favoriteSchedule.apply {
            db.fetch(
                """
                insert into favorite_schedules(grp_num, description, ord) 
                values ('${name.toBase64()}', '${description.toBase64()}', $order);
            """
            )
        }
    }

    fun remove(favoriteSchedule: FavoriteSchedule) {
        favoriteSchedule.apply {
            val base64Name = name.toBase64()
            db.fetch(
                """
                delete from favorite_schedules 
                where grp_num like '$base64Name';
            """
            )
        }
    }

    fun addAll(fs: List<FavoriteSchedule>) {
        fs.forEach(::add)
    }

    fun deleteAll() {
        db.fetch("delete from favorite_schedules;")
    }

    private fun map(record: Record): FavoriteSchedule {
        val name = record.get<String>("grp_num")?.fromBase64().orEmpty()
        return FavoriteSchedule(
            name = record.get<String>("grp_num")?.fromBase64().orEmpty(),
            type = name.deriveType(),
            description = record.get<String>("description")?.fromBase64().orEmpty(),
            order = record.get("ord") ?: 0
        )
    }

    @Deprecated("It is just incapsulated tech dept")
    private fun String.deriveType(): ScheduleType = // TODO: add scheduleType to SQL model
        if (matches(GROUP_NUMBER_PATTERN)) ScheduleType.GROUP else ScheduleType.PERSON

    private companion object {

        val GROUP_NUMBER_PATTERN = "[а-яА-Я]+-[а-яА-Я0-9]+-[0-9]+".toRegex()
    }
}
