package kekmech.ru.domain_notes

import kekmech.ru.common_android.fromBase64
import kekmech.ru.common_android.toBase64
import kekmech.ru.common_app_database.AppDatabase
import kekmech.ru.common_app_database.dto.Record
import kekmech.ru.domain_notes.dto.Note
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotesSourceImpl(
    private val db: AppDatabase
) : NotesSource {
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun getAll(groupName: String) =
        db.fetch("select * from notes where grp_name='$groupName';").map(::map)

    /**
     * Puts note to db, or update existing note if note.id != -1
     */
    override fun put(groupName: String, note: Note) {
        val content = note.content.toBase64()
        val dateTime = note.dateTime.format(dateTimeFormatter)
        val classesName = note.classesName
        if (note.id == -1) {
            db.fetch("""
                insert into notes(content, datetime, cls_name, grp_name)
                values ('$content', '$dateTime', '$classesName', '$groupName');
                """
            )
        } else {
            db.fetch("""
                update notes 
                set content='$content',
                    datetime='$dateTime',
                    cls_name='$classesName',
                    grp_name='$groupName'
                where _id=${note.id};
            """)
        }
    }

    override fun delete(note: Note) {
        if (note.id == -1) return
        db.fetch("delete from notes where _id=${note.id};")
    }

    private fun map(record: Record): Note = Note(
        id = record.get<Int>("_id") ?: error("Note Id must not be null!"),
        content = record.get<String>("content")?.fromBase64().orEmpty(),
        dateTime = record.get<String>("datetime")?.let { LocalDateTime.parse(it, dateTimeFormatter) } ?: LocalDateTime.MIN,
        classesName = record.get<String>("cls_name").orEmpty()
    )
}