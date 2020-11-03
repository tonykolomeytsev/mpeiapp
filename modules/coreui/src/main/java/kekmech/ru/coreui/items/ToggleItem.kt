package kekmech.ru.coreui.items

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_toggle.*

private const val DEFAULT_TOGGLE_ITEM_ID = 0

data class ToggleItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val isChecked: Boolean = false,
    val itemId: Int = DEFAULT_TOGGLE_ITEM_ID
)

interface ToggleViewHolder {
    fun setTitle(title: String)
    fun setTitle(@StringRes titleRes: Int)
    fun setIsChecked(isChecked: Boolean, withAnimation: Boolean = false)
    fun onCheckedChanged(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener)
}

class ToggleViewHolderImpl(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), ToggleViewHolder, LayoutContainer {

    override fun setTitle(title: String) {
        textViewTitle.text = title
    }

    override fun setTitle(@StringRes titleRes: Int) {
        textViewTitle.setText(titleRes)
    }

    override fun setIsChecked(isChecked: Boolean, withAnimation: Boolean) {
        if (withAnimation) toggle.post {
            toggle.isChecked = isChecked
        } else {
            toggle.isChecked = isChecked
        }
    }

    override fun onCheckedChanged(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener) {
        toggle.setOnCheckedChangeListener(onCheckedChangeListener)
    }
}

class ToggleItemBinder(
    private val onCheckedChangeListener: (Boolean) -> Unit
) : BaseItemBinder<ToggleViewHolder, ToggleItem>() {

    override fun bind(vh: ToggleViewHolder, model: ToggleItem, position: Int) {
        model.title?.let(vh::setTitle)
        model.titleRes?.let(vh::setTitle)
        vh.setIsChecked(model.isChecked)
        vh.onCheckedChanged { _, isChecked -> onCheckedChangeListener(isChecked) }
    }

    override fun update(
        vh: ToggleViewHolder,
        model: ToggleItem,
        position: Int,
        payloads: List<Any>
    ) {
        model.title?.let(vh::setTitle)
        model.titleRes?.let(vh::setTitle)
        vh.setIsChecked(model.isChecked, withAnimation = true)
    }
}

class ToggleAdapterItem(
    itemId: Int = DEFAULT_TOGGLE_ITEM_ID,
    onCheckedChangeListener: (Boolean) -> Unit
) : AdapterItem<ToggleViewHolder, ToggleItem>(
    isType = { it is ToggleItem && it.itemId == itemId },
    layoutRes = R.layout.item_toggle,
    viewHolderGenerator = ::ToggleViewHolderImpl,
    itemBinder = ToggleItemBinder(onCheckedChangeListener),
    areItemsTheSame = { a, b -> a.itemId == b.itemId && a.title == b.title }
)