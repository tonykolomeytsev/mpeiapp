package kekmech.ru.common_webview

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import kekmech.ru.common_android.close
import kekmech.ru.common_android.findArgument
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BackButtonListener
import kekmech.ru.common_webview.presentation.*
import kekmech.ru.common_webview.presentation.WebViewEvent.Wish
import kotlinx.android.synthetic.main.fragment_web_view.*
import org.koin.android.ext.android.inject

private const val START_URL_ARG = "UrlArg"
private const val JS_ARG = "JsArg"
private const val ALLOWED_URLS = "AllowedArg"

class WebViewFragment : BaseFragment<WebViewEvent, WebViewEffect, WebViewState, WebViewFeature>(),
    BackButtonListener {

    override val initEvent = Wish.Init

    override fun createFeature() = inject<WebViewFeatureFactory>().value
        .create(getArgument(START_URL_ARG))

    private val isJavascriptEnabled: Boolean get() = findArgument(JS_ARG) ?: false
    private val allowedUrls: ArrayList<Regex> get() = findArgument(ALLOWED_URLS) ?: arrayListOf()

    override var layoutId = R.layout.fragment_web_view

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        initWebView()
        toolbar.init()
    }

    override fun render(state: WebViewState) {
        webView.loadUrl(state.url)
        // show loading state
        if (state.isLoading) {
            progressBar.animate()
                .alpha(1f)
                .start()
        } else {
            progressBar.animate()
                .cancel()
        }
    }

    override fun handleEffect(effect: WebViewEffect) = when (effect) {
        is WebViewEffect.CloseScreen -> close()
    }

    private fun initWebView() {
        webView.apply {
            settings.apply {
                javaScriptEnabled = isJavascriptEnabled
                javaScriptCanOpenWindowsAutomatically = false
                domStorageEnabled = false
                allowFileAccess = false
            }
            webViewClient = object : WebViewClient() {

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest
                ): Boolean = handleRequest(request.url) // allow to load if return false

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return url != null && handleRequest(Uri.parse(url)) // allow to load if return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    toolbar.title = url.orEmpty()
                }
            }

            webChromeClient = object : WebChromeClient() {

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    feature.accept(Wish.Action.LoadingProgressChanged(newProgress))
                }
            }
        }
    }

    private fun handleRequest(url: Uri): Boolean {
        return !allowedUrls.any { it.matches(url.toString()) } // block url if non matches
    }

    override fun onBackPressed(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return false
    }

    companion object {

        fun newInstance(
            startUrl: String,
            allowedUrls: ArrayList<Regex> = arrayListOf(),
            enableJs: Boolean = false
        ) = WebViewFragment()
            .withArguments(
                START_URL_ARG to startUrl,
                ALLOWED_URLS to allowedUrls,
                JS_ARG to enableJs
            )
    }
}