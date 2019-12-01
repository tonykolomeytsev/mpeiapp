package kekmech.ru.feed.items

import android.view.View
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.coreui.adapter.BaseViewHolder
import kekmech.ru.feed.R

class ExamWeekItem : BaseItem<ExamWeekItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {

    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {

    }

    class Factory : BaseFactory(R.layout.item_exam_week, ::ViewHolder)
}