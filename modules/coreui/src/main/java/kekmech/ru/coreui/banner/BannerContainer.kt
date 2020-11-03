package kekmech.ru.coreui.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kekmech.ru.coreui.R
import kotlinx.android.synthetic.main.view_banner_container.view.*

class BannerContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_banner_container, this)
        id = R.id.bannerContainer
        bannerText.alpha = 0f
    }

    fun show(banner: Banner) {
        bannerText.text = banner.text
        bannerText.setBackgroundColor(banner.color)
        animateFadeIn()
    }

    private fun animateFadeIn() {
        bannerText.animate()
            .alpha(1f)
            .setDuration(200L)
            .withEndAction { animateFadeOut() }
            .start()
    }

    private fun animateFadeOut() {
        bannerText.animate()
            .alpha(0f)
            .setDuration(200L)
            .setStartDelay(1000L)
            .start()
    }
}