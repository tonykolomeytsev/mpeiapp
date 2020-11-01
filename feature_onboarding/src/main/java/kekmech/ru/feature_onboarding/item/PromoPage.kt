package kekmech.ru.feature_onboarding.item

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.feature_onboarding.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_promo_page.*

internal enum class PromoPage(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int
) {

    WELCOME(
        icon = R.drawable.onboarding_carousel_stub,
        title = R.string.onboarding_welcome_title_1,
        subtitle = R.string.onboarding_welcome_subtitle_1
    ),

    SCHEDULE(
        icon = R.drawable.onboarding_carousel_stub,
        title = R.string.onboarding_welcome_title_2,
        subtitle = R.string.onboarding_welcome_subtitle_2
    ),

    NOTES(
        icon = R.drawable.onboarding_carousel_stub,
        title = R.string.onboarding_welcome_title_3,
        subtitle = R.string.onboarding_welcome_subtitle_3
    ),

    MAP(
        icon = R.drawable.onboarding_carousel_stub,
        title = R.string.onboarding_welcome_title_4,
        subtitle = R.string.onboarding_welcome_subtitle_4
    ),

    SEARCH(
        icon = R.drawable.onboarding_carousel_stub,
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
    override val containerView: View
) : PromoPageViewHolder, RecyclerView.ViewHolder(containerView), LayoutContainer {

    override fun setIcon(@DrawableRes icon: Int) {
        promoIcon.setBackgroundResource(icon)
    }

    override fun setTitle(@StringRes title: Int) {
        promoTitle.setText(title)
    }

    override fun setSubtitle(@StringRes subtitle: Int) {
        promoSubtitle.setText(subtitle)
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