package kekmech.ru.repository.room

import android.arch.persistence.room.*
import kekmech.ru.core.dto.ScheduleNative

@Dao
interface ScheduleDao {
    @Query("select * from schedules")
    fun getAll(): List<ScheduleNative>

    @Query("select * from schedules where id = :id")
    fun getById(id: Int): ScheduleNative?

    @Query("select * from schedules where user_id = :userId")
    fun getAllByUserId(userId: Int): List<ScheduleNative>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scheduleNative: ScheduleNative)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(scheduleNative: ScheduleNative)

    @Delete
    fun delete(scheduleNative: ScheduleNative)
}