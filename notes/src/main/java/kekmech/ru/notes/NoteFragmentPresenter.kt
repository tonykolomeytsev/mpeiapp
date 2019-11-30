package kekmech.ru.notes

import android.annotation.SuppressLint
import android.content.Context
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.core.dto.Time
import kekmech.ru.coreui.Resources.getStringArray
import kekmech.ru.notes.model.NoteFragmentModel
import kekmech.ru.notes.view.NoteFragmentView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class NoteFragmentPresenter @Inject constructor(
    private val model: NoteFragmentModel,
    private val router: Router,
    private val context: Context
): Presenter<NoteFragmentView>() {

    private var realWeek: Int? = null
    private val locker = Any()

    override fun onResume(view: NoteFragmentView) {
        super.onResume(view)
        view.onBackNavClick = router::popBackStack
        view.onTextEdit = ::onTextEdit
        val couple = model.transactedCouple
        if (couple == null) {
            router.popBackStack()
            return
        } else {
            realWeek = couple.realWeek
            if (couple.noteId == -1) {
                view.setStatus(
                    couple.name,
                    couple.timestampReadable(),
                    ""
                )
            } else {
                view.setStatus(
                    couple.name,
                    couple.timestampReadable(),
                    ""
                )
                GlobalScope.launch(Main) {
                    val coupleContent = withContext(IO) { model.getNoteContentById(couple.noteId) }
                    view.setContent(coupleContent)
                }
            }
        }
    }

    private fun onTextEdit(string: String) {
        GlobalScope.launch(IO) {
            synchronized(locker) {
                val couple = model.transactedCouple ?: return@launch
                val day = couple.day
                val num = couple.num
                val note = NoteNative.Note(
                    string,
                    realWeek!!,
                    day,
                    num
                )
                model.saveNote(note)
            }
        }
    }

    /**
     * Форматирование к виду "День недели"
     * @param context - контекст через который можно получить доступ к strings.xml
     * @param stringArrayId - массив с названиями дней недели (ВСК, ПН, ВТ, СР ...)
     * @return [String] - соответствующее номеру названия дня недели
     * *Calendar.SUNDAY == 1 (недели начинаются с воскресенья, нумерация дней с единицы)
     */
    private fun Int.formattedAsDayName(context: Context?, stringArrayId: Int) =
        getStringArray(context, stringArrayId)[this - 1]

    @SuppressLint("DefaultLocale")
    private fun CoupleNative.timestampReadable(): String {
        val w = this@NoteFragmentPresenter.realWeek
        return try {
            "$w неделя, ${day.formattedAsDayName(context, R.array.days_of_week).decapitalize()}, $num пара"
        } catch (e: Exception) {
            "$w неделя, день ${day - 1}, $num пара"
        }
    }

    private val CoupleNative.realWeek get() = Time().weekOfSemester + week - 1



}