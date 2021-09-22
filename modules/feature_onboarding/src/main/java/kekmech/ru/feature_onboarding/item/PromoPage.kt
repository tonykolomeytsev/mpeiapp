package kekmech.ru.feature_onboarding.item

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_onboarding.R
import kekmech.ru.feature_onboarding.databinding.ItemPromoPageBinding

internal enum class PromoPage(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int
) {

    SCHEDULE(
        icon = R.drawable.ill_schedule,
        title = R.string.onboarding_welcome_title_schedule,
        subtitle = R.string.onboarding_welcome_subtitle_schedule
    ),

    MAP(
        icon = R.drawable.ill_map,
        title = R.string.onboarding_welcome_title_map,
        subtitle = R.string.onboarding_welcome_subtitle_map
    ),

    BARS(
        icon = R.drawable.ill_bars,
        title = R.string.onboarding_welcome_title_bars,
        subtitle = R.string.onboarding_welcome_subtitle_bars
    ),

    NOTES(
        icon = R.drawable.ill_notes,
        title = R.string.onboarding_welcome_title_notes,
        subtitle = R.string.onboarding_welcome_subtitle_notes
    ),
}

internal class PromoPageAdapterItem : AdapterItem<PromoPageViewHolder, PromoPage>(
    isType = { it is PromoPage },
    layoutRes = R.layout.item_promo_page,
    viewHolderGenerator = ::PromoPageViewHolder,
    itemBinder = PromoPageBinder()
)

internal class PromoPageViewHolder(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    private val viewBinding = ItemPromoPageBinding.bind(containerView)

    fun setIcon(@DrawableRes icon: Int) {
        viewBinding.promoIcon.setBackgroundResource(icon)
    }

    fun setTitle(@StringRes title: Int) {
        viewBinding.promoTitle.setText(title)
    }

    fun setSubtitle(@StringRes subtitle: Int) {
        viewBinding.promoSubtitle.setText(subtitle)
    }
}

private class PromoPageBinder : BaseItemBinder<PromoPageViewHolder, PromoPage>() {

    override fun bind(vh: PromoPageViewHolder, model: PromoPage, position: Int) {
        vh.setIcon(model.icon)
        vh.setTitle(model.title)
        vh.setSubtitle(model.subtitle)
    }
}