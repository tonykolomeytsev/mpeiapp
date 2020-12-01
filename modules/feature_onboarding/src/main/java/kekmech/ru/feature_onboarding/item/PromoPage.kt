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

    WELCOME(
        icon = R.drawable.ic_ill_1,
        title = R.string.onboarding_welcome_title_1,
        subtitle = R.string.onboarding_welcome_subtitle_1
    ),

    SCHEDULE(
        icon = R.drawable.ill_schedule,
        title = R.string.onboarding_welcome_title_2,
        subtitle = R.string.onboarding_welcome_subtitle_2
    ),

    NOTES(
        icon = R.drawable.ill_notes,
        title = R.string.onboarding_welcome_title_3,
        subtitle = R.string.onboarding_welcome_subtitle_3
    ),

    MAP(
        icon = R.drawable.ill_map,
        title = R.string.onboarding_welcome_title_4,
        subtitle = R.string.onboarding_welcome_subtitle_4
    ),

    SEARCH(
        icon = R.drawable.ill_search,
        title = R.string.onboarding_welcome_title_5,
        subtitle = R.string.onboarding_welcome_subtitle_5
    )
}

internal interface PromoPageViewHolder {
    fun setIcon(@DrawableRes icon: Int)
    fun setTitle(@StringRes title: Int)
    fun setSubtitle(@StringRes subtitle: Int)
}

internal class PromoPageViewHolderImpl(
    containerView: View
) : PromoPageViewHolder, RecyclerView.ViewHolder(containerView) {

    private val viewBinding = ItemPromoPageBinding.bind(containerView)

    override fun setIcon(@DrawableRes icon: Int) {
        viewBinding.promoIcon.setBackgroundResource(icon)
    }

    override fun setTitle(@StringRes title: Int) {
        viewBinding.promoTitle.setText(title)
    }

    override fun setSubtitle(@StringRes subtitle: Int) {
        viewBinding.promoSubtitle.setText(subtitle)
    }
}

internal class PromoPageBinder : BaseItemBinder<PromoPageViewHolder, PromoPage>() {

    override fun bind(vh: PromoPageViewHolder, model: PromoPage, position: Int) {
        vh.setIcon(model.icon)
        vh.setTitle(model.title)
        vh.setSubtitle(model.subtitle)
    }
}

internal class PromoPageAdapterItem : AdapterItem<PromoPageViewHolder, PromoPage>(
    isType = { it is PromoPage },
    layoutRes = R.layout.item_promo_page,
    viewHolderGenerator = ::PromoPageViewHolderImpl,
    itemBinder = PromoPageBinder()
)