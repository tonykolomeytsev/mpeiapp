package kekmech.ru.coreui.items

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.viewbinding.ReusableViewHolder
import kekmech.ru.common_android.viewbinding.lazyBinding
import kekmech.ru.coreui.R

public data class TextItem(
    val text: CharSequence? = null,
    @StringRes val textResId: Int? = null,
    @StyleRes val styleResId: Int? = null,
    val textGravity: Int = Gravity.START,
)

public class TextAdapterItem(
    @LayoutRes layoutRes: Int = R.layout.item_text
) : AdapterItem<TextItemViewHolder, TextItem>(
    isType = { it is TextItem },
    layoutRes = layoutRes,
    viewHolderGenerator = ::TextItemViewHolder,
    itemBinder = TextItemBinder()
)

public class TextItemViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), ReusableViewHolder {

    private val textViewTextItem by lazyBinding<TextView>(R.id.textViewTextItem)

    public fun setText(text: CharSequence) {
        textViewTextItem.text = text
    }

    public fun setText(@StringRes textResId: Int) {
        textViewTextItem.setText(textResId)
    }

    public fun setStyle(@StyleRes styleResId: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            textViewTextItem.setTextAppearance(styleResId)
        } else {
            @Suppress("DEPRECATION")
            textViewTextItem.setTextAppearance(containerView.context, styleResId)
        }
    }

    public fun setTextGravity(textGravity: Int) {
        textViewTextItem.gravity = textGravity
    }
}

public class TextItemBinder : BaseItemBinder<TextItemViewHolder, TextItem>() {

    override fun bind(vh: TextItemViewHolder, model: TextItem, position: Int) {
        model.apply {
            text?.let(vh::setText)
            textResId?.let(vh::setText)
            styleResId?.let(vh::setStyle)
            textGravity.let(vh::setTextGravity)
        }
    }
}
