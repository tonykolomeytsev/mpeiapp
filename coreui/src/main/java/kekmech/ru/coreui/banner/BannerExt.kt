package kekmech.ru.coreui.banner

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kekmech.ru.coreui.R

fun Fragment.showBanner(@StringRes resId: Int, @ColorRes color: Int = R.color.C11) =
    showBanner(getString(resId, color))
fun Fragment.showBanner(text: String, @ColorRes color: Int = R.color.C11) {
    val container = findBanner() ?: error("Add BannerContainer to $this layout")
    val bannerBackgroundColor = ContextCompat.getColor(requireContext(), color)
    container.show(Banner(text, bannerBackgroundColor))
}

fun Fragment.findBanner(): BannerContainer? = view?.findViewById(R.id.bannerContainer)