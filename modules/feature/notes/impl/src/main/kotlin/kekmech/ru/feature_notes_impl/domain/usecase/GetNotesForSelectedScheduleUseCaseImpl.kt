package kekmech.ru.feature_notes_impl.domain.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_notes_api.domain.usecase.GetNotesForSelectedScheduleUseCase
import kekmech.ru.feature_notes_impl.data.repository.NotesRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository

internal class GetNotesForSelectedScheduleUseCaseImpl(
    private val scheduleRepository: ScheduleRepository,
    private val notesRepository: NotesRepository,
) : GetNotesForSelectedScheduleUseCase {

    override suspend operator fun invoke(): List<Note> {
        val selectedSchedule = scheduleRepository.getSelectedSchedule()
        return try {
            // TODO: get rid of SQL for notes
            notesRepository.getNotesBySchedule(selectedSchedule)
        } catch (e: android.database.sqlite.SQLiteDatabaseLockedException) {
            FirebaseCrashlytics.getInstance().recordException(e) {
                key("schedule_name", selectedSchedule.name)
            }
            emptyList()
        } catch (e: Exception) {
            throw e
        }
    }
}
