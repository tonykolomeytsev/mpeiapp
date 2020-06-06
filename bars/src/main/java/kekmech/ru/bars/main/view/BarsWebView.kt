package kekmech.ru.bars.main.view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.webkit.WebView

class BarsWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : WebView(context.createConfigurationContext(Configuration()), attrs, defStyleAttr, defStyleRes) {


    override fun loadUrl(url: String?) {
        super.loadUrl(url, mapOf("X-Requested-With" to ""))
    }
}