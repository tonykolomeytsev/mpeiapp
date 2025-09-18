package kekmech.ru.coreui.banner

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ViewNetworkStateBannerContainerBinding
import kekmech.ru.ext_android.addSystemTopPadding

public class NetworkStateBannerContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val viewBinding: ViewNetworkStateBannerContainerBinding
    private val bannerText get() = viewBinding.bannerText
    public var isVisible: Boolean = false
        private set

    init {
        inflate(context, R.layout.view_network_state_banner_container, this)
        viewBinding = ViewNetworkStateBannerContainerBinding.bind(this)
        id = R.id.networkStateBannerContainer
        bannerText.alpha = 0f
        bannerText.addSystemTopPadding()
    }

    public fun showBanner() {
        isVisible = true
        bannerText.animate().cancel()
        bannerText.animate()
            .alpha(1f)
            .setDuration(ANIMATION_DURATION)
            .start()
    }

    public fun hideBanner() {
        isVisible = false
        bannerText.animate().cancel()
        bannerText.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION)
            .start()
    }

    private companion object {

        const val ANIMATION_DURATION = 200L
    }
}
