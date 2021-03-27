package kekmech.ru.bars.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.bars.databinding.ItemMenusBinding

internal object MenusItem

internal class MenusViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemMenusBinding.bind(itemView)

    fun setOnNotesClickListener(listener: View.OnClickListener) {
        viewBinding.buttonNotes.setOnClickListener(listener)
    }

    fun setOnSettingsClickListener(listener: View.OnClickListener) {
        viewBinding.buttonSettings.setOnClickListener(listener)
    }
}