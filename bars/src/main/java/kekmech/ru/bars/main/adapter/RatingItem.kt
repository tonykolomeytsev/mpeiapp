package kekmech.ru.bars.main.adapter

import android.view.View
import android.widget.TextView
import kekmech.ru.bars.R
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.coreui.adapter.BaseClickableItem
import kekmech.ru.coreui.adapter.BaseFactory
import kekmech.ru.coreui.adapter.BaseViewHolder

class RatingItem(val rating: AcademicScore.Rating): BaseClickableItem<RatingItem.ViewHolder>() {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.total.text = rating.total.toString()
        viewHolder.study.text = rating.study.toString()
        viewHolder.science.text = rating.science.toString()
        viewHolder.social.text = rating.social_total.toString()
        viewHolder.sport.text = rating.social_sport.toString()
        viewHolder.activity.text = rating.social_act.toString()
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val total by bind<TextView>(R.id.textViewScoreTotal)
        val study by bind<TextView>(R.id.textViewScoreStudy)
        val science by bind<TextView>(R.id.textViewScoreScience)
        val social by bind<TextView>(R.id.textViewScoreSocial)
        val sport by bind<TextView>(R.id.textViewScoreSport)
        val activity by bind<TextView>(R.id.textViewScoreActivity)
    }

    class Factory : BaseFactory(R.layout.item_rating, ::ViewHolder)
}