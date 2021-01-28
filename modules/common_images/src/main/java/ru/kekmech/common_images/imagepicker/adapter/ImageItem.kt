package ru.kekmech.common_images.imagepicker.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.ViewSizeResolver
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_images.R
import kekmech.ru.common_images.databinding.ItemImageBinding
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl

internal data class ImageItem(
    val url:  String,
    val isSelected: Boolean
)

internal interface ImageItemViewHolder : ClickableItemViewHolder {
    fun setImageByUrl(url: String)
    fun setIsSelected(isSelected: Boolean)
}

private class ImageItemViewHolderImpl(
    itemView: View
) : RecyclerView.ViewHolder(itemView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(itemView),
    ImageItemViewHolder {

    private val viewBinding = ItemImageBinding.bind(itemView)

    override fun setImageByUrl(url: String) {
        viewBinding.imageView.load(url) {
            crossfade(true)
            size(ViewSizeResolver(viewBinding.imageView))
        }
    }

    override fun setIsSelected(isSelected: Boolean) {
        /* no-op */
    }
}

private class ImageItemBinder(
    private val onClickListener: (String) -> Unit
) : BaseItemBinder<ImageItemViewHolder, ImageItem>() {

    override fun bind(vh: ImageItemViewHolder, model: ImageItem, position: Int) {
        vh.setImageByUrl(model.url)
        vh.setIsSelected(model.isSelected)
        vh.setOnClickListener { onClickListener(model.url) }
    }
}

internal class ImageAdapterItem(
    onClickListener: (String) -> Unit
) : AdapterItem<ImageItemViewHolder, ImageItem>(
    isType = { it is ImageItem },
    layoutRes = R.layout.item_image,
    viewHolderGenerator = ::ImageItemViewHolderImpl,
    itemBinder = ImageItemBinder(onClickListener)
)