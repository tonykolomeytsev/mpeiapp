package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemEmptyStateBinding
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder

public data class EmptyStateItem(
    @StringRes val titleRes: Int? = null,
    @StringRes val subtitleRes: Int? = null
)

public class EmptyStateAdapterItem : AdapterItem<EmptyStateViewHolder, EmptyStateItem>(
    isType = { it is EmptyStateItem },
    layoutRes = R.layout.item_empty_state,
    viewHolderGenerator = ::EmptyStateViewHolder,
    itemBinder = EmptyStateItemBinder()
)

public class EmptyStateViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    private val viewBinding = ItemEmptyStateBinding.bind(containerView)

    public fun setTitle(@StringRes titleRes: Int) {
        viewBinding.textViewTitle.setText(titleRes)
    }

    public fun setSubtitle(@StringRes subtitleRes: Int) {
        viewBinding.textViewSubtitle.setText(subtitleRes)
    }
}

public class EmptyStateItemBinder : BaseItemBinder<EmptyStateViewHolder, EmptyStateItem>() {

    override fun bind(vh: EmptyStateViewHolder, model: EmptyStateItem, position: Int) {
        model.titleRes?.let(vh::setTitle)
        model.subtitleRes?.let(vh::setSubtitle)
    }
}
