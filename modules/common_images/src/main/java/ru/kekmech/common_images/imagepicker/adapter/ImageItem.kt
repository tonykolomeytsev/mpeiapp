package ru.kekmech.common_images.imagepicker.adapter

import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_images.R
import kekmech.ru.common_images.databinding.ItemImageBinding
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl

internal data class ImageItem(
    val url:  String,
    val isSelected: Boolean,
    val selectionIndex: Int
)

internal interface ImageItemViewHolder : ClickableItemViewHolder {
    fun setImageByUrl(url: String)
    fun setIsSelected(isSelected: Boolean, selectionIndex: Int, withAnimation: Boolean = false)
    fun setOnImageSelectListener(onSelectListener: (View) -> Unit)
}

private class ImageItemViewHolderImpl(
    itemView: View
) : RecyclerView.ViewHolder(itemView),
    ClickableItemViewHolder by ClickableItemViewHolderImpl(itemView),
    ImageItemViewHolder {

    private val viewBinding = ItemImageBinding.bind(itemView)

    override fun setImageByUrl(url: String) {
        with(viewBinding.imageView) {
            Picasso.get()
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .fit()
                .centerCrop()
                .into(this)
        }
    }

    override fun setIsSelected(isSelected: Boolean, selectionIndex: Int, withAnimation: Boolean) {
        with(viewBinding.textViewIndex) {
            text = if (isSelected) selectionIndex.toString() else ""
            this.isSelected = isSelected
        }
        with (viewBinding.imageView) {
            if (withAnimation) {
                if (isSelected) {
                    animate()
                        .scaleX(SELECTED_IMAGE_SCALE)
                        .scaleY(SELECTED_IMAGE_SCALE)
                        .setDuration(SELECT_ANIMATION_DURATION)
                        .start()
                } else {
                    animate()
                        .scaleX(DEFAULT_IMAGE_SCALE)
                        .scaleY(DEFAULT_IMAGE_SCALE)
                        .setDuration(SELECT_ANIMATION_DURATION)
                        .start()
                }
            } else {
                if (isSelected) {
                    scaleX = SELECTED_IMAGE_SCALE
                    scaleY = SELECTED_IMAGE_SCALE
                } else {
                    scaleX = DEFAULT_IMAGE_SCALE
                    scaleY = DEFAULT_IMAGE_SCALE
                }
            }
        }
    }

    override fun setOnImageSelectListener(onSelectListener: (View) -> Unit) {
        viewBinding.clickableIndexContainer.setOnClickListener(onSelectListener)
    }

    companion object {
        private const val SELECTED_IMAGE_SCALE = 0.75f
        private const val DEFAULT_IMAGE_SCALE = 1f
        private const val SELECT_ANIMATION_DURATION = 50L // ms
    }
}

private class ImageItemBinder(
    private val onClickListener: (String) -> Unit,
    private val onSelectListener: (String) -> Unit
) : BaseItemBinder<ImageItemViewHolder, ImageItem>() {

    override fun bind(vh: ImageItemViewHolder, model: ImageItem, position: Int) {
        vh.setImageByUrl(model.url)
        vh.setIsSelected(model.isSelected, model.selectionIndex)
        vh.setOnClickListener { onClickListener(model.url) }
        vh.setOnImageSelectListener { onSelectListener(model.url) }
    }

    override fun update(
        vh: ImageItemViewHolder,
        model: ImageItem,
        position: Int,
        payloads: List<Any>
    ) {
        vh.setIsSelected(model.isSelected, model.selectionIndex, withAnimation = true)
    }
}

internal class ImageAdapterItem(
    onClickListener: (String) -> Unit,
    onSelectListener: (String) -> Unit
) : AdapterItem<ImageItemViewHolder, ImageItem>(
    isType = { it is ImageItem },
    layoutRes = R.layout.item_image,
    viewHolderGenerator = ::ImageItemViewHolderImpl,
    itemBinder = ImageItemBinder(onClickListener, onSelectListener),
    areItemsTheSame = { a, b -> a.url == b.url }
)