package kekmech.ru.feature_bars_impl.presentation.items

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.feature_bars_impl.databinding.ItemUserNameHeaderBinding
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseItemBinder

internal data class UserNameHeaderItem(val name: String?)

internal class UserNameHeaderAdapterItem(
    onMenuClickListener: () -> Unit,
) : AdapterItem<UserNameHeaderViewHolder, UserNameHeaderItem>(
    isType = { it is UserNameHeaderItem },
    layoutRes = R.layout.item_user_name_header,
    viewHolderGenerator = ::UserNameHeaderViewHolder,
    itemBinder = object : BaseItemBinder<UserNameHeaderViewHolder, UserNameHeaderItem>() {

        override fun bind(
            vh: UserNameHeaderViewHolder,
            model: UserNameHeaderItem,
            position: Int
        ) {
            vh.setShimmerEnabled(model.name == null)
            vh.setName(model.name.orEmpty())
            vh.setOnMenuClick { onMenuClickListener.invoke() }
        }
    },
    areItemsTheSame = { _, _ -> true }
)

internal class UserNameHeaderViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemUserNameHeaderBinding.bind(itemView)

    fun setShimmerEnabled(isEnabled: Boolean) {
        viewBinding.shimmerFrameLayout.isVisible = isEnabled
        if (isEnabled) {
            viewBinding.shimmerFrameLayout.startShimmer()
        } else {
            viewBinding.shimmerFrameLayout.stopShimmer()
        }
    }

    fun setName(name: String) {
        viewBinding.textViewTextItem.text = name
    }

    fun setOnMenuClick(listener: View.OnClickListener) {
        viewBinding.menu.setOnClickListener(listener)
    }
}
