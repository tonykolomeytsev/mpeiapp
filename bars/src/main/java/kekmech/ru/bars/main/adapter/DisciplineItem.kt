package kekmech.ru.bars.main.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.R
import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder

class DisciplineItem(val discipline: AcademicDiscipline) : BaseClickableItem<DisciplineItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.name.text = getFormattedName(discipline.name)
    }

    private fun getFormattedName(name: String): String {
        return "\"(.+)\"".toRegex().find(name)?.groupValues?.getOrNull(1) ?: name
    }

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