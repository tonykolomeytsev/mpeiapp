package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.coreui.R
import kekmech.ru.ext_android.viewbinding.ReusableViewHolder
import kekmech.ru.ext_android.viewbinding.lazyBinding

public interface LabeledTextViewHolder : ClickableItemViewHolder {
    public fun setMainText(mainText: String)
    public fun setLabel(labelText: String)
}

public class LabeledTextViewHolderImpl(
    override val containerView: View
) :
    LabeledTextViewHolder,
    RecyclerView.ViewHolder(containerView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView),
    ReusableViewHolder {

    private val textViewMainText by lazyBinding<TextView>(R.id.textViewMainText)
    private val textViewLabel by lazyBinding<TextView>(R.id.textViewLabel)

    override fun setMainText(mainText: String) {
        textViewMainText.text = mainText
    }

    override fun setLabel(labelText: String) {
        textViewLabel.text = labelText
    }
}
