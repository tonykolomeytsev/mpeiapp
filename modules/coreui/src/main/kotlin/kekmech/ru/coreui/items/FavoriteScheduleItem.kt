package kekmech.ru.coreui.items

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemFavoriteScheduleBinding
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

public data class FavoriteScheduleItem(
    val value: FavoriteSchedule,
    val isSelected: Boolean = false
)

public class FavoriteScheduleAdapterItem(
    onClickListener: ((FavoriteScheduleItem) -> Unit)? = null
) : AdapterItem<FavoriteScheduleViewHolder, FavoriteScheduleItem>(
    isType = { it is FavoriteScheduleItem },
    layoutRes = R.layout.item_favorite_schedule,
    viewHolderGenerator = ::FavoriteScheduleViewHolder,
    itemBinder = FavoriteScheduleItemBinder(onClickListener),
    areItemsTheSame = { a, b -> a.value.name == b.value.name }
)

public class FavoriteScheduleViewHolder(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView) {

    private val viewBinding = ItemFavoriteScheduleBinding.bind(containerView)

    public fun setTitle(title: String) {
        viewBinding.textViewTitle.text = title
    }

    public fun setDescription(description: String) {
        viewBinding.textViewDescription.text = description
    }

    public fun setIsSelected(isSelected: Boolean) {
        with(viewBinding) {
            val context = containerView.context
            if (isSelected) {
                containerView.backgroundTintList =
                    ColorStateList.valueOf(context.getThemeColor(R.attr.colorMain))
                textViewTitle.setTextColor(context.getThemeColor(R.attr.colorWhite))
                textViewDescription.setTextColor(context.getThemeColor(R.attr.colorGray10))
            } else {
                containerView.backgroundTintList =
                    ColorStateList.valueOf(context.getThemeColor(R.attr.colorGray10))
                textViewTitle.setTextColor(context.getThemeColor(R.attr.colorBlack))
                textViewDescription.setTextColor(context.getThemeColor(R.attr.colorGray70))
            }
        }
    }
}

public class FavoriteScheduleItemBinder(
    private val onClickListener: ((FavoriteScheduleItem) -> Unit)? = null
) : BaseItemBinder<FavoriteScheduleViewHolder, FavoriteScheduleItem>() {

    override fun bind(vh: FavoriteScheduleViewHolder, model: FavoriteScheduleItem, position: Int) {
        vh.setTitle(model.value.name)
        vh.setDescription(model.value.description)
        vh.setIsSelected(model.isSelected)
        onClickListener?.let { vh.setOnClickListener { it(model) } }
    }
}
