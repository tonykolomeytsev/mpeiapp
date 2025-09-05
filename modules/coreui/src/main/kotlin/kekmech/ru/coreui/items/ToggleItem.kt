package kekmech.ru.coreui.items

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemToggleBinding

private const val DEFAULT_TOGGLE_ITEM_ID = 0

public data class ToggleItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val isChecked: Boolean = false,
    val itemId: Int = DEFAULT_TOGGLE_ITEM_ID
)

public interface ToggleViewHolder {
    public fun setTitle(title: String)
    public fun setTitle(@StringRes titleRes: Int)
    public fun setIsChecked(isChecked: Boolean, withAnimation: Boolean = false)
    public fun onCheckedChanged(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener)
}

public class ToggleViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), ToggleViewHolder {

    private val viewBinding = ItemToggleBinding.bind(containerView)

    override fun setTitle(title: String) {
        viewBinding.textViewTitle.text = title
    }

    override fun setTitle(@StringRes titleRes: Int) {
        viewBinding.textViewTitle.setText(titleRes)
    }

    override fun setIsChecked(isChecked: Boolean, withAnimation: Boolean) {
        with(viewBinding) {
            if (withAnimation) toggle.post {
                toggle.isChecked = isChecked
            } else {
                toggle.isChecked = isChecked
            }
        }
    }

    override fun onCheckedChanged(onCheckedChangeListener: CompoundButton.OnCheckedChangeListener) {
        viewBinding.toggle.setOnCheckedChangeListener(onCheckedChangeListener)
    }
}

public class ToggleItemBinder(
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

public class ToggleAdapterItem(
    itemId: Int = DEFAULT_TOGGLE_ITEM_ID,
    onCheckedChangeListener: (Boolean) -> Unit
) : AdapterItem<ToggleViewHolder, ToggleItem>(
    isType = { it is ToggleItem && it.itemId == itemId },
    layoutRes = R.layout.item_toggle,
    viewHolderGenerator = ::ToggleViewHolderImpl,
    itemBinder = ToggleItemBinder(onCheckedChangeListener),
    areItemsTheSame = { a, b -> a.itemId == b.itemId && a.title == b.title }
)
