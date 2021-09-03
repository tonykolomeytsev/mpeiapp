package kekmech.ru.bars.items

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.databinding.ItemUserNameHeaderBinding

internal data class UserNameHeaderItem(val name: String?)

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
