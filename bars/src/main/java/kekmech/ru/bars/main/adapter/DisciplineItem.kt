package kekmech.ru.bars.main.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder
import kotlin.math.round

class DisciplineItem(val discipline: AcademicDiscipline) :
    BaseClickableItem<DisciplineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = getFormattedName(discipline.name)
        if (discipline.controlEvents.isNotEmpty() && !discipline.controlEvents.all { it.mark == -1f }) {
            viewHolder.recyclerEvents.visibility = View.VISIBLE
            viewHolder.headerEvents.visibility = View.VISIBLE
            viewHolder.recyclerEvents.apply {
                layoutManager = LinearLayoutManager(viewHolder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                val marksAdapter = BaseAdapter.Builder()
                    .registerViewTypeFactory(MarkItem.Factory())
                    .build()
                    .apply { baseItems.addAll(discipline.controlEvents
                        .filter { it.mark != -1f }
                        .map { MarkItem(it.mark) })
                    }
                adapter = marksAdapter
            }
        } else {
            viewHolder.recyclerEvents.visibility = View.GONE
            viewHolder.headerEvents.visibility = View.GONE
        }
        if (discipline.controlWeeks.isNotEmpty() && !discipline.controlWeeks.all { it.mark == -1f }) {
            viewHolder.recyclerWeeks.visibility = View.VISIBLE
            viewHolder.headerWeeks.visibility = View.VISIBLE
            viewHolder.recyclerWeeks.apply {
                layoutManager = LinearLayoutManager(viewHolder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                val marksAdapter = BaseAdapter.Builder()
                    .registerViewTypeFactory(MarkItem.Factory())
                    .build()
                    .apply { baseItems.addAll(discipline.controlWeeks
                        .filter { it.mark != -1f }
                        .map { MarkItem(it.mark) })
                    }
                adapter = marksAdapter
            }
        } else {
            viewHolder.recyclerWeeks.visibility = View.GONE
            viewHolder.headerWeeks.visibility = View.GONE
        }
        viewHolder.average.visibility = View.VISIBLE
        if (discipline.finalFinalMark != -1f) {
            viewHolder.average.text = formatFloat(discipline.finalFinalMark)
        } else if (discipline.finalComputedMark != -1f) {
            viewHolder.average.text = formatFloat(discipline.finalComputedMark)
        } else if (discipline.examMark != -1f) {
            viewHolder.average.text = formatFloat(discipline.examMark)
        } else if (discipline.currentMark != -1f) {
            viewHolder.average.text = formatFloat(discipline.currentMark)
        } else {
            viewHolder.average.visibility = View.INVISIBLE
        }
    }

    private fun getFormattedName(name: String): String {
        return "\"(.+)\"".toRegex().find(name)?.groupValues?.getOrNull(1) ?: name
    }

    private fun formatFloat(float: Float): String = if (round(float) == float) float.toInt().toString() else float.toString()


    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val name by bind<TextView>(R.id.textViewDisciplineName)
        val recyclerWeeks by bind<RecyclerView>(R.id.recyclerControlWeeks)
        val recyclerEvents by bind<RecyclerView>(R.id.recyclerControlEvents)
        val headerWeeks by bind<TextView>(R.id.textViewControlWeeks)
        val headerEvents by bind<TextView>(R.id.textViewCOntrolEvents)
        val average by bind<TextView>(R.id.textViewMarkOverage)
    }

    class Factory : BaseFactory(R.layout.item_discipline, ::ViewHolder)
}