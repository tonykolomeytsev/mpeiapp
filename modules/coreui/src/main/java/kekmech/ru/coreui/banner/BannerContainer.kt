package kekmech.ru.coreui.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ViewBannerContainerBinding

class BannerContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val viewBinding: ViewBannerContainerBinding

    init {
        View.inflate(context, R.layout.view_banner_container, this)
        viewBinding = ViewBannerContainerBinding.bind(this)
        id = R.id.bannerContainer
        viewBinding.bannerText.alpha = 0f
    }

    fun show(banner: Banner) {
        viewBinding.bannerText.text = banner.text
        viewBinding.bannerText.setBackgroundColor(banner.color)
        animateFadeIn()
    }

    private fun animateFadeIn() {
        viewBinding.bannerText.animate()
            .alpha(1f)
            .setDuration(200L)
            .withEndAction { animateFadeOut() }
            .start()
    }

    private fun animateFadeOut() {
        viewBinding.bannerText.animate()
            .alpha(0f)
            .setDuration(200L)
            .setStartDelay(1000L)
            .start()
    }
}