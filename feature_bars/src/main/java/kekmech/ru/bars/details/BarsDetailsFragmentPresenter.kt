package kekmech.ru.bars.details

import kekmech.ru.bars.details.adapter.EventItem
import kekmech.ru.bars.details.adapter.FinalItem
import kekmech.ru.bars.details.adapter.WeekItem
import kekmech.ru.bars.details.view.BarsDetailsFragmentView
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.usecases.GetDetailsDisciplineUseCase
import kekmech.ru.coreui.adapter.BaseAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BarsDetailsFragmentPresenter constructor(
    private val getDetailsDisciplineUseCase: GetDetailsDisciplineUseCase,
    private val router: Router
) : Presenter<BarsDetailsFragmentView>() {

    override fun onResume(view: BarsDetailsFragmentView) {
        super.onResume(view)
        val d = getDetailsDisciplineUseCase()
        if (d == null) {
            router.popBackStack() // если вдруг нам не вернулся рейтинг
            return
        }
        view.onNavBackListener = router::popBackStack
        view.setTitle(d.name)
        GlobalScope.launch(Dispatchers.Main) {
            delay(150)
            val eventsAdapter = BaseAdapter.Builder()
                .registerViewTypeFactory(EventItem.Factory())
                .registerViewTypeFactory(FinalItem.Factory())
                .build()
            eventsAdapter.baseItems.addAll(d.controlEvents.map { EventItem(it) })

            if (d.currentMark != -1f)
                eventsAdapter.baseItems.add(FinalItem("Балл текущего контроля", d.currentMark))
            val last1 = (eventsAdapter.baseItems.last())
            if (last1 is EventItem) last1.divider = false
            if (last1 is FinalItem) last1.divider = false
            view.setEventsAdapter(eventsAdapter)

            val weeksAdapter = BaseAdapter.Builder()
                .registerViewTypeFactory(WeekItem.Factory())
                .build()
            weeksAdapter.baseItems.addAll(d.controlWeeks.mapIndexed { i, it -> WeekItem(it, i+1) })
            val last2 = (weeksAdapter.baseItems.last())
            if (last2 is FinalItem) last2.divider = false
            if (last2 is WeekItem) last2.divider = false
            view.setWeeksAdapter(weeksAdapter)

            val finalAdapter = BaseAdapter.Builder()
                .registerViewTypeFactory(FinalItem.Factory())
                .build()
            finalAdapter.baseItems.addAll(listOf(
                FinalItem("Промежуточная аттестация ${getExamType(d)}", d.examMark),
//                FinalItem("Итоговая рассчитанная", d.finalComputedMark),
                FinalItem("Итоговая проставленная", d.finalFinalMark)
            ))
            (finalAdapter.baseItems.last() as FinalItem).divider = false
            view.setFinalAdapter(finalAdapter)
        }
    }

    private fun getExamType(d: AcademicDiscipline): String {
        if (d.examType.isNotEmpty()) {
            return "\n(${d.examType})"
        } else return ""
    }
}