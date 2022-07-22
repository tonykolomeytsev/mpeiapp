package kekmech.ru.coreui.banner

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kekmech.ru.coreui.R

fun Fragment.showBanner(@StringRes resId: Int, @ColorRes color: Int = R.color.colorRed) =
    showBanner(getString(resId), color)

fun Fragment.showBanner(text: String, @ColorRes color: Int = R.color.colorRed) {
    findBanner()?.let { container ->
        val bannerBackgroundColor = ContextCompat.getColor(requireContext(), color)
        container.show(Banner(text, bannerBackgroundColor))
    }
}

private fun Fragment.findNetworkStateBanner(): NetworkStateBannerContainer? =
    requireActivity().findViewById(R.id.networkStateBannerContainer)

fun Fragment.findBanner(): BannerContainer? =
    requireActivity().findViewById<BannerContainer>(R.id.bannerContainer)
        ?.takeIf { findNetworkStateBanner()?.isVisible != true }
