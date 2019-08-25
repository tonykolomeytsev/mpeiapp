package kekmech.ru.repository.room

import android.arch.persistence.room.*
import kekmech.ru.core.dto.CoupleNative

@Dao
interface CoupleDao {
    @Query("select * from couples")
    fun getAll(): List<CoupleNative>

    @Query("select * from couples where id = :id")
    fun getById(id: Int): CoupleNative

    @Query("select * from couples where schedule_id = :scheduleId")
    fun getAllByScheduleId(scheduleId: Int): List<CoupleNative>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coupleNative: CoupleNative)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(coupleNative: CoupleNative)

    @Delete
    fun delete(coupleNative: CoupleNative)
}