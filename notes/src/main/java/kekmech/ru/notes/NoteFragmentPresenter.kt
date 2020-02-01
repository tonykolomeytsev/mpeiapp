package kekmech.ru.notes

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.NoteNative
import kekmech.ru.coreui.Resources.getStringArray
import kekmech.ru.notes.model.NoteFragmentModel
import kekmech.ru.notes.view.NoteFragmentView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteFragmentPresenter constructor(
    private val model: NoteFragmentModel,
    private val router: Router,
    private val context: Context
): Presenter<NoteFragmentView>() {

    private var realWeek: Int? = null
    private val locker = Any()
    @Volatile
    private var isWriteAllowed = false

    override fun onResume(view: NoteFragmentView) {
        super.onResume(view)
        view.onBackNavClick = router::popBackStack
        view.onTextEdit = ::onTextEdit
        val couple = model.transactedCouple
        this.realWeek = model.transactedRealWeek
        if (couple == null) {
            router.popBackStack()
            return
        } else {
            view.setStatus(
                couple.name,
                couple.timestampReadable()
            )
            GlobalScope.launch(Main) {
                val coupleContent = withContext(IO) { model.getNoteContent() }
                view.setContent(coupleContent)
                isWriteAllowed = true
            }
        }

    }

    private fun onTextEdit(string: String) {
        Log.d("NotePresenter", isWriteAllowed.toString())
        if (!isWriteAllowed) return
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

    override fun onPause(view: NoteFragmentView) {
        isWriteAllowed = false
        super.onPause(view)
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
}