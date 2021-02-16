package kekmech.ru.feature_search.item

import android.view.View
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder

private const val DEFAULT_BUTTON_ITEM_ID = 0

internal data class ButtonItem(
    val itemId: Int = DEFAULT_BUTTON_ITEM_ID,
    @StringRes val textResId: Int
)

internal class ButtonViewHolderImpl(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val button = itemView as Button

    fun setOnCLickListener(listener: (View) -> Unit) {
        button.setOnClickListener(listener)
    }

    fun setText(textResId: Int) {
        button.setText(textResId)
    }
}

internal class ButtonItemBinder(
    private val onClickListener: (ButtonItem) -> Unit
) : BaseItemBinder<ButtonViewHolderImpl, ButtonItem>() {

    override fun bind(vh: ButtonViewHolderImpl, model: ButtonItem, position: Int) {
        vh.setOnCLickListener { onClickListener(model) }
        vh.setText(model.textResId)
    }
}

internal class ButtonAdapterItem(
    itemId: Int,
    @LayoutRes layoutRes: Int,
    onClickListener: (ButtonItem) -> Unit
) : AdapterItem<ButtonViewHolderImpl, ButtonItem>(
    isType = { it is ButtonItem && it.itemId == itemId },
    layoutRes = layoutRes,
    viewHolderGenerator = ::ButtonViewHolderImpl,
    itemBinder = ButtonItemBinder(onClickListener)
)