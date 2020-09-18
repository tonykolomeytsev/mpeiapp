package kekmech.ru.bars.main.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.coreui.deprecated.adapter.BaseAdapter
import kekmech.ru.coreui.deprecated.adapter.BaseClickableItem
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder2
import kotlin.math.round

class DisciplineItem(val discipline: AcademicDiscipline) :
    BaseClickableItem<DisciplineItem.ViewHolder>() {

    private val marksAdapter = BaseAdapter.Builder()
        .registerViewTypeFactory(MarkItem.Factory())
        .build()

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.apply {
            name = getFormattedName(discipline.name)
            if (discipline.controlEvents.isNotEmpty() && !discipline.controlEvents.all { it.mark == -1f }) {
                recyclerEvents.visibility = View.VISIBLE
                recyclerEvents.setRecycledViewPool(Companion.eventsRecyclerViewPool)
                recyclerEvents.setItemViewCacheSize(10)
                recyclerEvents.setHasFixedSize(true)
                recyclerEvents.isEnabled = false
                isHeaderEventsVisible = true

                recyclerEvents.apply {
                    layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

                    marksAdapter.items.clear()
                    marksAdapter.items.addAll(discipline.controlEvents
                        .filter { it.mark != -1f }
                        .map { MarkItem(it.mark) })

                    adapter = marksAdapter
                }
            } else {
                isHeaderEventsVisible = false
                recyclerEvents.visibility = View.GONE
            }
            if (discipline.controlWeeks.isNotEmpty() && !discipline.controlWeeks.all { it.mark == -1f }) {
                recyclerWeeks.visibility = View.VISIBLE
                recyclerWeeks.apply {
                    layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                    val marksAdapter = BaseAdapter.Builder()
                        .registerViewTypeFactory(WeekMarkItem.Factory())
                        .build()
                        .apply {
                            baseItems.addAll(discipline.controlWeeks
                                .filter { it.mark != -1f }
                                .map { WeekMarkItem(it.mark) })
                        }
                    adapter = marksAdapter
                }
            } else {
                recyclerWeeks.visibility = View.GONE
            }
            average = when {
                discipline.finalFinalMark != -1f    -> formatFloat(discipline.finalFinalMark)
                discipline.finalComputedMark != -1f -> formatFloat(discipline.finalComputedMark)
                discipline.examMark != -1f          -> formatFloat(discipline.examMark)
                discipline.currentMark != -1f       -> formatFloat(discipline.currentMark)
                else -> "Ø"
            }
        }
    }

    private fun getFormattedName(name: String) =
        "\"(.+)\"".toRegex().find(name)?.groupValues?.getOrNull(1) ?: name

    private fun formatFloat(float: Float): String = if (round(float) == float) float.toInt().toString() else float.toString()

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder2(view) {
        var name            by bindText(R.id.textViewDisciplineName)
        var average         by bindText(R.id.textViewMarkOverage)
        val recyclerEvents  by bind<RecyclerView>(R.id.recyclerControlEvents)
        val recyclerWeeks   by bind<RecyclerView>(R.id.recyclerControlWeeks)
        var isHeaderEventsVisible  by bindVisibility(R.id.textViewCOntrolEvents)
    }

    class Factory : BaseFactory(R.layout.item_discipline, ::ViewHolder)

    companion object {
        private val eventsRecyclerViewPool = RecyclerView.RecycledViewPool()
    }


    /**
     * Для оптимизации главного экрана БАРСа
     */
    override fun equals(other: Any?) = if (other is DisciplineItem) this.discipline == other.discipline else false
    override fun hashCode() = discipline.hashCode()
}