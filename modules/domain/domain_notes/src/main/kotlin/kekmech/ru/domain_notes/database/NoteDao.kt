package kekmech.ru.domain_notes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_notes.database.entities.NormalNote

@Dao
interface NoteDao {

    @Upsert
    suspend fun updateOrInsert(normalNote: NormalNote)

    @Query("SELECT * FROM note WHERE associatedScheduleName=:scheduleName")
    suspend fun getAllNotesForSchedule(scheduleName: String): List<NormalNote>

    @Delete
    suspend fun delete(normalNote: NormalNote)
}
