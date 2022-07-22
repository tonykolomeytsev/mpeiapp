package kekmech.ru.coreui.items

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemBannerBinding

private const val DEFAULT_BANNER_ID = 0

data class BannerItem(
    val title: String? = null,
    @StringRes val titleRes: Int? = null,
    val description: String? = null,
    @StringRes val descriptionRes: Int? = null,
    @DrawableRes val iconRes: Int? = null,
    val itemId: Int = DEFAULT_BANNER_ID
)

interface BannerViewHolder {
    fun setTitle(title: String)
    fun setTitle(@StringRes titleRes: Int)
    fun setDescription(description: String)
    fun setDescription(@StringRes descriptionRes: Int)
    fun setIcon(@DrawableRes iconRes: Int)
    fun setOnClickListener(listener: (View) -> Unit)
    fun setOnCloseListener(listener: (View) -> Unit)
}

class BannerViewHolderImpl(
    private val containerView: View
) : RecyclerView.ViewHolder(containerView), BannerViewHolder {

    private val viewBinding = ItemBannerBinding.bind(containerView)

    override fun setTitle(title: String) {
        viewBinding.bannerTitle.text = title
    }

    override fun setTitle(@StringRes titleRes: Int) {
        viewBinding.bannerTitle.setText(titleRes)
    }

    override fun setDescription(description: String) {
        viewBinding.bannerDescription.text = description
    }

    override fun setDescription(descriptionRes: Int) {
        viewBinding.bannerDescription.setText(descriptionRes)
    }

    override fun setIcon(iconRes: Int) {
        viewBinding.bannerIcon.setImageResource(iconRes)
    }

    override fun setOnClickListener(listener: (View) -> Unit) {
        containerView.setOnClickListener(listener)
    }

    override fun setOnCloseListener(listener: (View) -> Unit) {
        viewBinding.bannerExit.setOnClickListener(listener)
    }
}


class BannerItemBinder(
    private val onClickListener: ((BannerItem) -> Unit)? = null,
    private val onCloseListener: ((BannerItem) -> Unit)? = null
) : BaseItemBinder<BannerViewHolder, BannerItem>() {

    override fun bind(vh: BannerViewHolder, model: BannerItem, position: Int) {
        model.title?.let(vh::setTitle)
        model.titleRes?.let(vh::setTitle)
        model.description?.let(vh::setDescription)
        model.descriptionRes?.let(vh::setDescription)
        model.iconRes?.let(vh::setIcon)
        onClickListener?.let { vh.setOnClickListener{ it(model) } }
        onCloseListener?.let { vh.setOnCloseListener { it(model) } }
    }
}

class BannerAdapterItem(
    onClickListener: ((BannerItem) -> Unit)? = null,
    onCloseListener: ((BannerItem) -> Unit)? = null
) : AdapterItem<BannerViewHolder, BannerItem>(
    isType = { it is BannerItem },
    layoutRes = R.layout.item_banner,
    viewHolderGenerator = ::BannerViewHolderImpl,
    itemBinder = BannerItemBinder(onClickListener, onCloseListener)
)
