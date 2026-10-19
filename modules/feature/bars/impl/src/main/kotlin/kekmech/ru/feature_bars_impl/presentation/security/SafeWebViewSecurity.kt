package kekmech.ru.feature_bars_impl.presentation.security

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * OWASP-compliant security configurator for WebViews.
 *
 * Enforces:
 * - Disabled file access from filesystem / content providers.
 * - Disabled cross-origin / file scheme access.
 * - Strict HTTPS domain whitelisting (bars.mpei.ru, ts.mpei.ru, mpei.ru).
 */
public object SafeWebViewSecurity {

    private val TRUSTED_HOSTS = setOf(
        "bars.mpei.ru",
        "ts.mpei.ru",
        "mpei.ru",
    )

    public fun isHostTrusted(host: String?): Boolean {
        if (host.isNullOrBlank()) return false
        val cleanHost = host.lowercase()
        return TRUSTED_HOSTS.any { trusted ->
            cleanHost == trusted || cleanHost.endsWith(".$trusted")
        }
    }

    public fun isUrlAllowed(uri: Uri?): Boolean {
        if (uri == null) return false
        if (uri.scheme != "https") return false
        return isHostTrusted(uri.host)
    }

    public fun isUrlAllowed(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val uri = runCatching { Uri.parse(url) }.getOrNull() ?: return false
        return isUrlAllowed(uri)
    }

    @SuppressLint("SetJavaScriptEnabled")
    public fun configureSafeSettings(webView: WebView) {
        val settings = webView.settings
        settings.allowFileAccess = false
        settings.allowContentAccess = false
        @Suppress("DEPRECATION")
        settings.allowFileAccessFromFileURLs = false
        @Suppress("DEPRECATION")
        settings.allowUniversalAccessFromFileURLs = false
        settings.setGeolocationEnabled(false)
        settings.databaseEnabled = false
    }

    public fun createSafeWebViewClient(
        onBlockedUrl: ((Uri) -> Unit)? = null,
        onPageFinishedCallback: ((WebView, String?) -> Unit)? = null,
    ): WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val uri = request?.url
            if (isUrlAllowed(uri)) {
                return false
            }
            onBlockedUrl?.invoke(uri ?: Uri.EMPTY)
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val uri = url?.let { runCatching { Uri.parse(it) }.getOrNull() }
            if (isUrlAllowed(uri)) {
                return false
            }
            onBlockedUrl?.invoke(uri ?: Uri.EMPTY)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (view != null) {
                onPageFinishedCallback?.invoke(view, url)
            }
        }
    }
}
