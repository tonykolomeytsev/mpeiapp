package kekmech.ru.coreui.banner

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kekmech.ru.coreui.R

public fun Fragment.showBanner(@StringRes resId: Int, @ColorRes color: Int = R.color.colorRed): Unit =
    showBanner(getString(resId), color)

public fun Fragment.showBanner(text: String, @ColorRes color: Int = R.color.colorRed) {
    findBanner()?.let { container ->
        val bannerBackgroundColor = ContextCompat.getColor(requireContext(), color)
        container.show(Banner(text, bannerBackgroundColor))
    }
}

public fun Fragment.findBanner(): BannerContainer? =
    requireActivity().findBanner()

public fun FragmentActivity.findBanner(): BannerContainer? =
    findViewById<BannerContainer>(R.id.bannerContainer)
        ?.takeIf { findNetworkStateBanner()?.isVisible != true }

private fun FragmentActivity.findNetworkStateBanner(): NetworkStateBannerContainer? =
    findViewById(R.id.networkStateBannerContainer)
