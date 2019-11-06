package kekmech.ru.repository.room

import androidx.room.*
import kekmech.ru.core.dto.NoteNative

@Dao
interface NoteDao {
    @Query("select * from notes")
    fun getAll(): List<NoteNative>

    @Query("select * from notes where id = :id")
    fun getById(id: Int): NoteNative?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noteNative: NoteNative)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(noteNative: NoteNative)

    @Delete
    fun delete(noteNative: NoteNative)
}