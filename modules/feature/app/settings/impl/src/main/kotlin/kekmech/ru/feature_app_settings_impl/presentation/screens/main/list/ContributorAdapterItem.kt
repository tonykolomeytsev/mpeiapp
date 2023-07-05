package kekmech.ru.feature_app_settings_impl.presentation.screens.main.list

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kekmech.ru.ext_android.dpToPx
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.feature_app_settings_impl.databinding.ItemContributorBinding
import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kekmech.ru.library_adapter.AdapterItem
import kekmech.ru.library_adapter.BaseItemBinder

internal class ContributorAdapterItem(
    onClick: (Contributor) -> Unit,
) : AdapterItem<ContributorViewHolder, Contributor>(
    isType = { it is Contributor },
    layoutRes = R.layout.item_contributor,
    viewHolderGenerator = ::ContributorViewHolder,
    itemBinder = object : BaseItemBinder<ContributorViewHolder, Contributor>() {

        override fun bind(vh: ContributorViewHolder, model: Contributor, position: Int) {
            vh.setData(model)
            vh.setOnClickListener { onClick.invoke(model) }
        }
    }
)

internal class ContributorViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = ItemContributorBinding.bind(itemView)
    private val dp8 = itemView.resources.dpToPx(dp = 24)

    fun setData(contributor: Contributor) =
        with(viewBinding) {
            login.text = contributor.login

            bio.isVisible = contributor.bio != null
            contributor.bio?.let(bio::setText)

            name.isVisible = contributor.name != null
            contributor.name?.let(name::setText)

            Picasso.get().cancelRequest(avatar)
            Picasso.get()
                .load(contributor.avatarUrl)
                .tag(ContributorAdapterItem::class)
                .fit()
                .centerCrop()
                .transform(RoundedCornersTransformation(dp8.toFloat()))
                .placeholder(R.drawable.background_placeholder)
                .error(R.drawable.background_placeholder)
                .into(avatar)
        }

    fun setOnClickListener(onClick: () -> Unit) {
        viewBinding.root.setOnClickListener { onClick.invoke() }
    }
}

private class RoundedCornersTransformation(val radius: Float) : Transformation {

    private val clipPath = Path().apply {
        addCircle(radius, radius, radius, Path.Direction.CW)
    }

    override fun transform(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(source.width, source.height, source.config)
        val canvas = Canvas(output)
        canvas.clipPath(clipPath)
        canvas.drawBitmap(source, 0f, 0f, Paint())
        source.recycle()
        return output
    }

    override fun key(): String {
        return "RoundImage"
    }
}
