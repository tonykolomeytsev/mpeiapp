package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R

data class SectionTextItem(
    val text: String? = null,
    @StringRes val resId: Int? = null
)

interface SectionTextViewHolder {
    fun setText(text: String)
    fun setText(@StringRes resId: Int)
}

class SectionTextViewHolderImpl(
    containerView: View
) : SectionTextViewHolder, RecyclerView.ViewHolder(containerView) {

    private val textViewSectionText = containerView.findViewById<TextView>(R.id.textViewSectionText)

    override fun setText(text: String) {
        textViewSectionText.text = text
    }

    override fun setText(@StringRes resId: Int) {
        textViewSectionText.setText(resId)
    }
}

class SectionTextItemBinder : BaseItemBinder<SectionTextViewHolder, SectionTextItem>() {

    override fun bind(vh: SectionTextViewHolder, model: SectionTextItem, position: Int) {
        model.text?.let(vh::setText)
        model.resId?.let(vh::setText)
    }
}

class SectionTextAdapterItem : AdapterItem<SectionTextViewHolder, SectionTextItem>(
    isType = { it is SectionTextItem },
    layoutRes = R.layout.item_section_text,
    viewHolderGenerator = ::SectionTextViewHolderImpl,
    itemBinder = SectionTextItemBinder()
)