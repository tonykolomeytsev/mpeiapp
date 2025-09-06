package kekmech.ru.lib_app_database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kekmech.ru.lib_app_database.entity.NormalNote

@Dao
public interface NoteDao {

    @Upsert
    public suspend fun updateOrInsert(normalNote: NormalNote)

    @Query("SELECT * FROM note WHERE associatedScheduleName=:scheduleName")
    public suspend fun getAllNotesForSchedule(scheduleName: String): List<NormalNote>

    @Delete
    public suspend fun delete(normalNote: NormalNote)
}
