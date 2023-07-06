package kekmech.ru.feature_search_impl.item

import android.view.View
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

private const val DEFAULT_BUTTON_ITEM_ID = 0

internal data class ButtonItem(
    val itemId: Int = DEFAULT_BUTTON_ITEM_ID,
    @StringRes val textResId: Int,
)

internal class ButtonAdapterItem(
    itemId: Int,
    @LayoutRes layoutRes: Int,
    onClickListener: (ButtonItem) -> Unit,
) : AdapterItem<ButtonViewHolder, ButtonItem>(
    isType = { it is ButtonItem && it.itemId == itemId },
    layoutRes = layoutRes,
    viewHolderGenerator = ::ButtonViewHolder,
    itemBinder = object : BaseItemBinder<ButtonViewHolder, ButtonItem>() {

        override fun bind(vh: ButtonViewHolder, model: ButtonItem, position: Int) {
            vh.setOnCLickListener { onClickListener(model) }
            vh.setText(model.textResId)
        }
    }
)

internal class ButtonViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val button = itemView as Button

    fun setOnCLickListener(listener: (View) -> Unit) {
        button.setOnClickListener(listener)
    }

    fun setText(textResId: Int) {
        button.setText(textResId)
    }
}
