package kekmech.ru.bars.screen.main

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.FragmentBarsBinding
import kekmech.ru.bars.items.AssessedDisciplineAdapterItem
import kekmech.ru.bars.items.LoginToBarsAdapterItem
import kekmech.ru.bars.items.UserNameHeaderAdapterItem
import kekmech.ru.bars.screen.details.BarsDetailsFragment
import kekmech.ru.bars.screen.main.elm.BarsEffect
import kekmech.ru.bars.screen.main.elm.BarsEvent
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
import kekmech.ru.bars.screen.main.elm.BarsFeatureFactory
import kekmech.ru.bars.screen.main.elm.BarsState
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.doOnApplyWindowInsets
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.views.setProgressViewOffset
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.showDialog
import kekmech.ru.coreui.items.ShimmerAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextWithIconAdapterItem
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_notes.NotesFeatureLauncher
import org.koin.android.ext.android.inject

private const val JS_INTERFACE_NAME = "kti"

internal class BarsFragment : BaseFragment<BarsEvent, BarsEffect, BarsState>() {

    override val initEvent: BarsEvent = Wish.Init
    override val layoutId: Int = R.layout.fragment_bars

    private val viewBinding by viewBinding(FragmentBarsBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val settingsFeatureLauncher by inject<AppSettingsFeatureLauncher>()
    private val notesFeatureLauncher by inject<NotesFeatureLauncher>()

    override fun createStore() = inject<BarsFeatureFactory>().value.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
        with(viewBinding) {
            recyclerView.adapter = adapter
            recyclerView.addSystemTopPadding()
            webViewContainer.addSystemTopPadding()
            returnBanner.setOnClickListener { feature.accept(Wish.Click.HideBrowser) }
            swipeRefresh.apply {
                setOnRefreshListener {
                    feature.accept(Wish.Click.SwipeToRefresh)
                }
                doOnApplyWindowInsets { _, windowInsets, _ ->
                    setProgressViewOffset(windowInsets.systemWindowInsetTop)
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(viewBinding) {
            with(webView) {
                settings.allowFileAccess = false
                settings.allowContentAccess = false
                settings.javaScriptEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    settings.safeBrowsingEnabled = false
                }

                addJavascriptInterface(JSInterface(), JS_INTERFACE_NAME)
                webViewClient = BarsWebViewClient()
            }
            webViewToolbar.setNavigationOnClickListener {
                feature.accept(Wish.Click.HideBrowser)
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
            returnBanner.isVisible = state.isReturnBannerVisible
        }
        adapter.update(BarsListConverter(requireContext()).map(state))
    }

    override fun handleEffect(effect: BarsEffect) = when (effect) {
        is BarsEffect.LoadPage -> viewBinding.webView.loadUrl(effect.url)
        is BarsEffect.InvokeJs -> viewBinding.webView.evaluateJavascript(effect.js, null)
        is BarsEffect.OpenSettings -> settingsFeatureLauncher.launch()
        is BarsEffect.OpenAllNotes -> notesFeatureLauncher.launchAllNotes()
    }

    override fun onResume() {
        super.onResume()
        feature.accept(Wish.Action.Update)
    }

    inner class BarsWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            feature.accept(Wish.Action.PageFinished(url))
            viewBinding.webViewToolbar.title = url
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return true
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }

    @Suppress("unused")
    inner class JSInterface {

        @JavascriptInterface
        fun onStudentNameExtracted(name: String) {
            feature.accept(Wish.Extract.StudentName(name))
        }

        @JavascriptInterface
        fun onStudentGroupExtracted(group: String) {
            feature.accept(Wish.Extract.StudentGroup(group))
        }

        @JavascriptInterface
        fun onStudentMetaExtracted(metadataJson: String) {
            feature.accept(Wish.Extract.MetaData(metadataJson))
        }

        @JavascriptInterface
        fun onStudentRatingExtracted(ratingJson: String) {
            feature.accept(Wish.Extract.Rating(ratingJson))
        }

        @JavascriptInterface
        fun onSemestersExtracted(semestersJson: String, selectedSemesterName: String) {
            feature.accept(Wish.Extract.Semesters(semestersJson, selectedSemesterName))
        }

        @JavascriptInterface
        fun onMarksExtracted(marksJson: String) {
            feature.accept(Wish.Extract.Marks(marksJson))
        }
    }

    private fun createAdapter() = BaseAdapter(
        AssessedDisciplineAdapterItem { showDialog { BarsDetailsFragment.newInstance(it) } },
        SpaceAdapterItem(),
        UserNameHeaderAdapterItem { feature.accept(Wish.Click.Settings) },
        TextWithIconAdapterItem {
            if (it.itemId == 1) {
                feature.accept(Wish.Click.ShowBrowser)
            }
        },
        LoginToBarsAdapterItem { feature.accept(Wish.Click.Login) },
        ShimmerAdapterItem(ITEM_TEXT_SHIMMER, R.layout.item_text_shimmer),
        ShimmerAdapterItem(ITEM_DISCIPLINE_SHIMMER, R.layout.item_discipline_shimmer),
    )

    companion object {
        const val ITEM_TEXT_SHIMMER = 0
        const val ITEM_DISCIPLINE_SHIMMER = 1
    }
}