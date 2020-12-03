package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.viewbinding.ReusableViewHolder
import kekmech.ru.common_android.viewbinding.lazyBinding
import kekmech.ru.coreui.R

interface LabeledTextItem {
    val mainText: String?
    val mainTextResId: Int?
    val label: String?
    val labelResId: Int?
    val itemId: Int?
}

data class TopLabeledTextItem(
    override val mainText: String? = null,
    @StringRes override val mainTextResId: Int? = null,
    override val label: String? = null,
    @StringRes override val labelResId: Int? = null,
    override val itemId: Int? = null
) : LabeledTextItem

data class BottomLabeledTextItem(
    override val mainText: String? = null,
    @StringRes override val mainTextResId: Int? = null,
    override val label: String? = null,
    @StringRes override val labelResId: Int? = null,
    override val itemId: Int? = null
) : LabeledTextItem

data class RightLabeledTextItem(
    override val mainText: String? = null,
    @StringRes override val mainTextResId: Int? = null,
    override val label: String? = null,
    @StringRes override val labelResId: Int? = null,
    override val itemId: Int? = null
) : LabeledTextItem

interface LabeledTextViewHolder : ClickableItemViewHolder {
    fun setMainText(mainText: String)
    fun setMainText(@StringRes mainTextResId: Int)
    fun setLabel(labelText: String)
    fun setLabel(@StringRes labelTextResId: Int)
}

class LabeledTextViewHolderImpl(
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

    override fun setMainText(@StringRes mainTextResId: Int) {
        textViewMainText.setText(mainTextResId)
    }

    override fun setLabel(labelText: String) {
        textViewLabel.text = labelText
    }

    override fun setLabel(@StringRes labelTextResId: Int) {
        textViewLabel.setText(labelTextResId)
    }
}

class LabeledTextItemBinder(
    private val onClickListener: ((LabeledTextItem) -> Unit)?
) : BaseItemBinder<LabeledTextViewHolder, LabeledTextItem>() {

    override fun bind(vh: LabeledTextViewHolder, model: LabeledTextItem, position: Int) {
        model.mainText?.let(vh::setMainText)
        model.mainTextResId?.let(vh::setMainText)
        model.label?.let(vh::setLabel)
        model.labelResId?.let(vh::setLabel)
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }
}

class TopLabeledTextAdapterItem(
    onClickListener: ((LabeledTextItem) -> Unit)? = null
) : AdapterItem<LabeledTextViewHolder, TopLabeledTextItem>(
    isType = { it is TopLabeledTextItem },
    layoutRes = R.layout.item_text_top_labeled,
    viewHolderGenerator = ::LabeledTextViewHolderImpl,
    itemBinder = LabeledTextItemBinder(onClickListener)
)

class BottomLabeledTextAdapterItem(
    onClickListener: ((LabeledTextItem) -> Unit)? = null
) : AdapterItem<LabeledTextViewHolder, BottomLabeledTextItem>(
    isType = { it is BottomLabeledTextItem },
    layoutRes = R.layout.item_text_bottom_labeled,
    viewHolderGenerator = ::LabeledTextViewHolderImpl,
    itemBinder = LabeledTextItemBinder(onClickListener)
)

class RightLabeledTextAdapterItem(
    onClickListener: ((LabeledTextItem) -> Unit)? = null
) : AdapterItem<LabeledTextViewHolder, BottomLabeledTextItem>(
    isType = { it is RightLabeledTextItem },
    layoutRes = R.layout.item_text_right_labeled,
    viewHolderGenerator = ::LabeledTextViewHolderImpl,
    itemBinder = LabeledTextItemBinder(onClickListener)
)