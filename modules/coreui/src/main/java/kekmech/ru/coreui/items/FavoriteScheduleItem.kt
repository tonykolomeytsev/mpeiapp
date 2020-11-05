package kekmech.ru.coreui.items

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.coreui.R
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_favorite_schedule.*

class FavoriteScheduleItem(
    val value: FavoriteSchedule,
    val isSelected: Boolean = false
)

interface FavoriteScheduleViewHolder : ClickableItemViewHolder {
    fun setTitle(title: String)
    fun setDescription(description: String)
    fun setIsSelected(isSelected: Boolean)
}

class FavoriteScheduleViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView),
    FavoriteScheduleViewHolder,
    LayoutContainer {

    override fun setTitle(title: String) {
        textViewTitle.text = title
    }

    override fun setDescription(description: String) {
        textViewDescription.text = description
    }

    override fun setIsSelected(isSelected: Boolean) {
        val context = containerView.context
        if (isSelected) {
            containerView.backgroundTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.colorMain))
            textViewTitle.setTextColor(context.getThemeColor(R.attr.colorWhite))
            textViewDescription.setTextColor(context.getThemeColor(R.attr.colorGray30))
        } else {
            containerView.backgroundTintList = ColorStateList.valueOf(context.getThemeColor(R.attr.colorGray10))
            textViewTitle.setTextColor(context.getThemeColor(R.attr.colorBlack))
            textViewDescription.setTextColor(context.getThemeColor(R.attr.colorGray70))
        }
    }
}

class FavoriteScheduleItemBinder(
    private val onClickListener: ((FavoriteScheduleItem) -> Unit)? = null
) : BaseItemBinder<FavoriteScheduleViewHolder, FavoriteScheduleItem>() {

    override fun bind(vh: FavoriteScheduleViewHolder, model: FavoriteScheduleItem, position: Int) {
        vh.setTitle(model.value.groupNumber)
        vh.setDescription(model.value.description)
        vh.setIsSelected(model.isSelected)
        onClickListener?.let { vh.setOnClickListener { it(model) } }
    }
}

class FavoriteScheduleAdapterItem(
    onClickListener: ((FavoriteScheduleItem) -> Unit)? = null
) : AdapterItem<FavoriteScheduleViewHolder, FavoriteScheduleItem>(
    isType = { it is FavoriteScheduleItem },
    layoutRes = R.layout.item_favorite_schedule,
    viewHolderGenerator = ::FavoriteScheduleViewHolderImpl,
    itemBinder = FavoriteScheduleItemBinder(onClickListener)
)