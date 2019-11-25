package kekmech.ru.core.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "notes",
    indices = [Index(
        value = ["id"],
        unique = true
    )]
)
data class NoteNative(
    @ColumnInfo(name = "couple_id")
    var coupleId: Int,

    @ColumnInfo(name = "timestamp")
    var timestamp: String,

    @ColumnInfo(name = "data")
    var data: String,

    @ColumnInfo(name = "tag")
    var tag: String
) {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = -1
}