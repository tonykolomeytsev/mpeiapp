package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_text_top_labeled.*

interface LabeledTextItem {
    val mainText: String?
    val mainTextResId: Int?
    val label: String?
    val labelResId: Int?
}

data class TopLabeledTextItem(
    override val mainText: String? = null,
    @StringRes override val mainTextResId: Int? = null,
    override val label: String? = null,
    @StringRes override val labelResId: Int? = null
) : LabeledTextItem

data class BottomLabeledTextItem(
    override val mainText: String? = null,
    @StringRes override val mainTextResId: Int? = null,
    override val label: String? = null,
    @StringRes override val labelResId: Int? = null
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
    LayoutContainer,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView)
{

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