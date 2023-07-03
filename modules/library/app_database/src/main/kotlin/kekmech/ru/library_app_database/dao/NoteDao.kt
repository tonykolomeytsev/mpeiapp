package kekmech.ru.library_app_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kekmech.ru.library_app_database.entity.NormalNote

@Dao
interface NoteDao {

    @Upsert
    suspend fun updateOrInsert(normalNote: NormalNote)

    @Query("SELECT * FROM note WHERE associatedScheduleName=:scheduleName")
    suspend fun getAllNotesForSchedule(scheduleName: String): List<NormalNote>

    @Delete
    suspend fun delete(normalNote: NormalNote)
}
