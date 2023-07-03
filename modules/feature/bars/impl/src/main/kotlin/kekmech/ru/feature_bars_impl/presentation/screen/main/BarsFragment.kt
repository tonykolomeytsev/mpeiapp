package kekmech.ru.feature_bars_impl.presentation.screen.main

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import android.webkit.*
import androidx.appcompat.widget.Toolbar
import androidx.core.text.toSpannable
import androidx.core.view.forEach
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextWithIconAdapterItem
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.doOnApplyWindowInsets
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.views.setProgressViewOffset
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.feature_bars_impl.databinding.FragmentBarsBinding
import kekmech.ru.feature_bars_impl.presentation.items.AssessedDisciplineAdapterItem
import kekmech.ru.feature_bars_impl.presentation.items.LoginToBarsAdapterItem
import kekmech.ru.feature_bars_impl.presentation.items.UserNameHeaderAdapterItem
import kekmech.ru.feature_bars_impl.presentation.screen.details.BarsDetailsFragment
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEffect
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsEvent.Ui
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsState
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsStoreProvider
import kekmech.ru.library_adapter.BaseAdapter
import kekmech.ru.library_analytics_android.addScrollAnalytics
import kekmech.ru.library_analytics_android.ext.screenAnalytics
import kekmech.ru.library_elm.BaseFragment
import kekmech.ru.library_navigation.features.ScrollToTop
import kekmech.ru.library_navigation.features.TabScreenStateSaver
import kekmech.ru.library_navigation.features.TabScreenStateSaverImpl
import kekmech.ru.library_navigation.showDialog
import kekmech.ru.strings.Strings
import org.koin.android.ext.android.inject
import timber.log.Timber
import kekmech.ru.coreui.R as coreui_R

private const val JS_INTERFACE_NAME = "kti"

@Suppress("TooManyFunctions")
internal class BarsFragment :
    BaseFragment<BarsEvent, BarsEffect, BarsState>(R.layout.fragment_bars), ScrollToTop,
    TabScreenStateSaver by TabScreenStateSaverImpl("bars") {

    private val analytics by screenAnalytics("Bars")
    private val viewBinding by viewBinding(FragmentBarsBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val settingsFeatureLauncher by inject<AppSettingsFeatureLauncher>()

    override fun createStore() = inject<BarsStoreProvider>().value.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
        with(viewBinding) {
            recyclerView.adapter = adapter
            recyclerView.addSystemTopPadding()
            recyclerView.addScrollAnalytics(analytics, "BarsRecycler")
            restoreState(recyclerView)

            webViewContainer.addSystemTopPadding()
            returnBanner.setOnClickListener {
                analytics.sendClick("BarsReturnBanner")
                store.accept(Ui.Click.HideBrowser)
            }
            swipeRefresh.apply {
                setOnRefreshListener {
                    analytics.sendClick("BarsUpdate")
                    store.accept(Ui.Click.SwipeToRefresh)
                }
                doOnApplyWindowInsets { _, windowInsets, _ ->
                    setProgressViewOffset(windowInsets.systemWindowInsetTop)
                }
            }
        }
    }

    override fun onDestroyView() {
        saveState(viewBinding.recyclerView)
        super.onDestroyView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(viewBinding) {
            with(webView) {
                settings.allowFileAccess = false
                settings.allowContentAccess = false
                settings.javaScriptEnabled = true
                settings.compromiseUserAgent()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    settings.safeBrowsingEnabled = false
                }

                addJavascriptInterface(JSInterface(), JS_INTERFACE_NAME)
                webViewClient = BarsWebViewClient()
            }
            webViewToolbar.setNavigationOnClickListener {
                analytics.sendClick("BarsHideBrowser")
                store.accept(Ui.Click.HideBrowser)
            }
            webViewToolbar.enableOverflowMenuColorWorkaround()
            webViewToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.refresh -> store.accept(Ui.Action.Update)
                    R.id.open_external -> webView.url?.let { requireContext().openLinkExternal(it) }
                    else -> Unit
                }
                false
            }
        }
    }

    override fun render(state: BarsState) {
        with(viewBinding) {
            webViewContainer.isInvisible = !state.isBrowserVisible
            swipeRefresh.isInvisible = state.isBrowserVisible
            swipeRefresh.post {
                swipeRefresh.isRefreshing = state.isLoading
            }
            disciplineDetailsFragmentContainer?.let { it.isInvisible = state.isBrowserVisible }
            returnBanner.isVisible = state.isReturnBannerVisible

            // render webview
            val (url, pageTitle, isLoading) = state.webViewUiState
            val decoratedUrl = url.replace("https://", "").replace("http://", "")
            with(webViewToolbar) {
                title = pageTitle ?: decoratedUrl
                subtitle = decoratedUrl.takeIf { pageTitle != null }
            }
            progressIndicator.isVisible = isLoading
        }
        adapter.update(BarsListConverter(requireContext()).map(state))
    }

    override fun handleEffect(effect: BarsEffect) =
        when (effect) {
            is BarsEffect.LoadPage -> viewBinding.webView.loadUrl(effect.url)
            is BarsEffect.InvokeJs -> viewBinding.webView.evaluateJavascript(effect.js) {
                Timber.d(it)
            }
            is BarsEffect.OpenSettings -> settingsFeatureLauncher.launch()
            is BarsEffect.ShowCommonError -> showBanner(Strings.something_went_wrong_error)
            is BarsEffect.OpenExternalBrowser ->
                requireContext().openLinkExternal(effect.url)
            is BarsEffect.ScrollToTop -> viewBinding.recyclerView.scrollToPosition(0)
        }

    override fun onResume() {
        super.onResume()
        store.accept(Ui.Action.Update)
    }

    override fun onScrollToTop() {
        store.accept(Ui.Action.ScrollToTop)
    }

    inner class BarsWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String) {
            store.accept(Ui.Action.PageFinished(url, view?.title))
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            store.accept(Ui.Action.PageStarted)
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return handleUrlLoading(url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest,
        ): Boolean {
            return handleUrlLoading(request.url.toString())
        }

        private fun handleUrlLoading(url: String): Boolean {
            val allowToFollowTheUrl = "https://bars.mpei.ru" in url
            if (!allowToFollowTheUrl) {
                store.accept(Ui.Click.NotAllowedUrl(url))
            }
            return !allowToFollowTheUrl
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError,
        ) {
            if (request.url?.host?.contains("mpei.ru") != true) return

            analytics.sendCustomAction("BarsWebViewErrorReceived")
            store.accept(Ui.Action.PageLoadingError)
        }
    }

    @Suppress("unused")
    inner class JSInterface {

        @JavascriptInterface
        fun onStudentNameExtracted(name: String) {
            store.accept(Ui.Extract.StudentName(name))
        }

        @JavascriptInterface
        fun onStudentGroupExtracted(group: String) {
            store.accept(Ui.Extract.StudentGroup(group))
        }

        @JavascriptInterface
        fun onStudentMetaExtracted(metadataJson: String) {
            store.accept(Ui.Extract.MetaData(metadataJson))
        }

        @JavascriptInterface
        fun onStudentRatingExtracted(ratingJson: String) {
            store.accept(Ui.Extract.Rating(ratingJson))
        }

        @JavascriptInterface
        fun onSemestersExtracted(semestersJson: String, selectedSemesterName: String) {
            store.accept(Ui.Extract.Semesters(semestersJson, selectedSemesterName))
        }

        @JavascriptInterface
        fun onMarksExtracted(marksJson: String) {
            store.accept(Ui.Extract.Marks(marksJson))
        }
    }

    private fun createAdapter() = BaseAdapter(
        AssessedDisciplineAdapterItem {
            analytics.sendClick("BarsDisciplineDetails")
            viewBinding.disciplineDetailsFragmentContainer?.let { _ ->
                childFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.disciplineDetailsFragmentContainer,
                        BarsDetailsFragment.newInstance(it)
                    )
                    .commitAllowingStateLoss()
            } ?: showDialog { BarsDetailsFragment.newInstance(it) }
        },
        SpaceAdapterItem(),
        UserNameHeaderAdapterItem {
            analytics.sendClick("BarsSettings")
            store.accept(Ui.Click.Settings)
        },
        TextWithIconAdapterItem {
            when (it.itemId) {
                ITEM_BROWSER_LABEL -> {
                    analytics.sendClick("BarsShowBrowser")
                    store.accept(Ui.Click.ShowBrowser)
                }
                else -> { /* no-op */
                }
            }
        },
        LoginToBarsAdapterItem {
            analytics.sendClick("BarsMainLogin")
            store.accept(Ui.Click.Login)
        },
        EmptyStateAdapterItem(),
        ShimmerAdapterItem(coreui_R.layout.item_text_shimmer),
        ShimmerAdapterItem(R.layout.item_discipline_shimmer),
        ShimmerAdapterItem(R.layout.item_login_to_bars_shimmer),
    )

    private fun WebSettings.compromiseUserAgent() {
        userAgentString = userAgentString.replace("; wv", "")
    }

    private fun Toolbar.enableOverflowMenuColorWorkaround() {
        val blackThemeColor = requireContext().getThemeColor(coreui_R.attr.colorBlack)
        overflowIcon?.setTint(blackThemeColor)
        menu?.forEach { menuItem ->
            val title = menuItem.title ?: ""
            val spannable = title.toSpannable()
            spannable.setSpan(
                ForegroundColorSpan(blackThemeColor),
                0,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            menuItem.title = spannable
        }
    }

    companion object {

        const val ITEM_GROUP_LABEL = 0
        const val ITEM_BROWSER_LABEL = 1
    }
}
