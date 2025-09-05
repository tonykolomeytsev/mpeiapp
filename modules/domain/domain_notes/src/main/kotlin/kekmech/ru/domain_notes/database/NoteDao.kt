package kekmech.ru.domain_notes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_notes.database.entities.NormalNote

@Dao
public interface NoteDao {

    @Upsert
    public fun updateOrInsert(normalNote: NormalNote): Completable

    @Query("SELECT * FROM note WHERE associatedScheduleName=:scheduleName")
    public fun getAllNotesForSchedule(scheduleName: String): Single<List<NormalNote>>

    @Delete
    public fun delete(normalNote: NormalNote): Completable
}
