package kekmech.ru.core.dto

import androidx.room.*
import com.google.gson.annotations.SerializedName
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
    var scheduleId: Int,

    @ColumnInfo(name = "timestamp")
    var timestamp: String,

    @ColumnInfo(name = "data")
    var data: String,

    @ColumnInfo(name = "tag")
    var tag: String
) {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0

    data class Note(
        @SerializedName("t")
        var text: String = "",
        @SerializedName("w")
        var weekNum: Int = 0,
        @SerializedName("d")
        var dayNum: Int = 0,
        @SerializedName("c")
        var coupleNum: Int = 0
    )
}