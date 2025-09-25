package kekmech.ru.coreui.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ViewBannerContainerBinding
import kekmech.ru.ext_android.addSystemTopPadding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
public class BannerContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val bannerFlow = MutableStateFlow<Banner?>(null)
    private val viewBinding: ViewBannerContainerBinding
    private val bannerText get() = viewBinding.bannerText

    init {
        View.inflate(context, R.layout.view_banner_container, this)
        viewBinding = ViewBannerContainerBinding.bind(this)
        id = R.id.bannerContainer
        bannerText.alpha = 0f
        bannerText.addSystemTopPadding()

        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            bannerFlow
                .onEach { banner ->
                    if (banner != null) {
                        displayBanner(banner)
                    } else {
                        hideBanner()
                    }
                }
                .launchIn(this)

            // Автоматическое скрытие баннера
            bannerFlow
                .filterNotNull()
                .debounce(BANNER_SHOWING_DURATION)
                .onEach { bannerFlow.value = null }
                .launchIn(this)
        }
    }

    public fun show(banner: Banner) {
        bannerFlow.value = banner
    }

    private fun displayBanner(banner: Banner) {
        bannerText.animate().cancel()
        bannerText.alpha = 0f
        bannerText.text = banner.text
        bannerText.setBackgroundColor(banner.color)
        bannerText.animate()
            .alpha(1f)
            .setDuration(ANIMATION_DURATION)
            .start()
    }

    private fun hideBanner() {
        bannerText.animate().cancel()
        bannerText.alpha = 1f
        bannerText.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION)
            .start()
    }

    private companion object {

        const val ANIMATION_DURATION = 200L
        const val BANNER_SHOWING_DURATION = 3000L // ms
    }
}
