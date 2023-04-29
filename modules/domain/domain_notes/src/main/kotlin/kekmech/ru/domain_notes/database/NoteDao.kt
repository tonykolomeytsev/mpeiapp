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
    fun updateOrInsert(normalNote: NormalNote): Completable

    @Query("SELECT * FROM note WHERE associatedScheduleName=:scheduleName")
    fun getAllNotesForSchedule(scheduleName: String): Single<List<NormalNote>>

    @Delete
    fun delete(normalNote: NormalNote): Completable
}
