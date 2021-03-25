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
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.FragmentBarsBinding
import kekmech.ru.bars.screen.main.elm.BarsEffect
import kekmech.ru.bars.screen.main.elm.BarsEvent
import kekmech.ru.bars.screen.main.elm.BarsEvent.Wish
import kekmech.ru.bars.screen.main.elm.BarsFeatureFactory
import kekmech.ru.bars.screen.main.elm.BarsState
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.NeedToUpdate
import org.koin.android.ext.android.inject


private const val JS_INTERFACE_NAME = "kti"

class BarsFragment : BaseFragment<BarsEvent, BarsEffect, BarsState>(), NeedToUpdate {

    override val initEvent: BarsEvent = Wish.Init
    override val layoutId: Int = R.layout.fragment_bars

    private val viewBinding by viewBinding(FragmentBarsBinding::bind)

    override fun createStore() = inject<BarsFeatureFactory>().value.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(viewBinding.webView) {
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.javaScriptEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                settings.safeBrowsingEnabled = false
            }

            addJavascriptInterface(JSInterface(), JS_INTERFACE_NAME)
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
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
        }
    }

    override fun render(state: BarsState) {

    }

    override fun handleEffect(effect: BarsEffect) = when (effect) {
        is BarsEffect.LoadPage -> viewBinding.webView.loadUrl(effect.url)
        is BarsEffect.InvokeJs -> viewBinding.webView.evaluateJavascript(effect.js, null)
    }

    override fun onUpdate() {
        feature.accept(Wish.Action.Update)
    }

    @Suppress("unused")
    inner class JSInterface {

        @JavascriptInterface
        fun marksExtracted(content: String) {

        }
    }
}