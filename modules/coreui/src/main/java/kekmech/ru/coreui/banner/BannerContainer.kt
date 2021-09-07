package kekmech.ru.coreui.banner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_kotlin.Option
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ViewBannerContainerBinding
import java.util.concurrent.TimeUnit


class BannerContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val bannerSubject = BehaviorSubject.create<Option<Banner>>().toSerialized()
    private val viewBinding: ViewBannerContainerBinding
    private val bannerText get() = viewBinding.bannerText

    init {
        View.inflate(context, R.layout.view_banner_container, this)
        viewBinding = ViewBannerContainerBinding.bind(this)
        id = R.id.bannerContainer
        bannerText.alpha = 0f
        bannerText.addSystemTopPadding()

        observeBannerShowing()
            .subscribe { it.value?.let(::displayBanner) }
        observeBannerHiding()
            .subscribe { it.value?.let { hideBanner() } }
    }

    fun show(banner: Banner) {
        bannerSubject.onNext(Option(banner))
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

    private fun observeBannerShowing() =
        bannerSubject
            .distinctUntilChanged()
            .observeOn(mainThread())

    private fun observeBannerHiding() =
        bannerSubject
            .debounce(BANNER_SHOWING_DURATION, TimeUnit.MILLISECONDS)
            .doOnNext { bannerSubject.onNext(Option(null)) }
            .observeOn(mainThread())

    private companion object {

        const val ANIMATION_DURATION = 200L
        const val BANNER_SHOWING_DURATION = 3000L // ms
    }
}