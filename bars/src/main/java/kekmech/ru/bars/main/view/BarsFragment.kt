package kekmech.ru.bars.main.view

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.bars.R
import kekmech.ru.bars.main.BarsViewModel
import kekmech.ru.coreui.Resources
import kotlinx.android.synthetic.main.fragment_bars.*
import org.koin.android.ext.android.inject


class BarsFragment : Fragment(R.layout.fragment_bars) {

    init {
        retainInstance = true
    }

    private val viewModel: BarsViewModel by inject()

    override fun onResume() {
        viewModel.setUserAgent(webView.settings.userAgentString)
        super.onResume()
        viewModel.checkForUpdates()
        viewModel.refresh()

        if (recyclerView?.layoutManager == null)
            recyclerView?.layoutManager = LinearLayoutManager(context)
        if (recyclerView?.adapter == null)
            recyclerView?.adapter = viewModel.adapter
        swipeRefresh?.setProgressViewEndTarget(false, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 144f, resources.displayMetrics).toInt())
        swipeRefresh?.setOnRefreshListener { viewModel.refresh() }
        swipeRefresh?.setColorSchemeColors(
            Resources.getColor(context, R.color.colorPrimary),
            Resources.getColor(context, R.color.colorSecondary)
        )
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }
        //BottomSheetBehavior.from(bottomMenu).state = BottomSheetBehavior.STATE_HIDDEN
        webView?.settings?.loadsImagesAutomatically = false
        webView?.settings?.javaScriptEnabled = true

        viewModel.barsState.observe(this, Observer {
            if (it == true) swipeRefresh?.isEnabled = true
            else {
                swipeRefresh?.isRefreshing = false
                swipeRefresh?.isEnabled = false
            }
        })
        viewModel.contentItems.observe(this, Observer {
            viewModel.updateAdapter(it ?: emptyList())
            hideLoading()
        })
        viewModel.loadUrl.observe(this, Observer {
            viewModel.setUserAgent(webView.settings.userAgentString)
            val (url, listener) = it ?: return@Observer
            webView?.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?){
                    CookieSyncManager.getInstance().sync()
                    listener(url ?: "")
                }
            }
            webView?.loadUrl(url)
        })
        viewModel.executeScript.observe(this, Observer {
            viewModel.setUserAgent(webView.settings.userAgentString)
            val (script, callback, pageListener) = it ?: return@Observer
            webView?.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    CookieSyncManager.getInstance().sync()
                    pageListener(url ?: "")
                }
            }
            webView?.evaluateJavascript(script) {
                callback(it ?: "")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedView?.parent != null) (savedView?.parent as ViewGroup?)?.removeView(savedView)
        if (savedView == null) savedView = super.onCreateView(inflater, container, savedInstanceState)
        return savedView!!
    }

    private fun hideLoading() = swipeRefresh?.post { swipeRefresh?.isRefreshing = false }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnApplyWindowInsetsListener { _, insets ->
            val statusBarSize = insets.systemWindowInsetTop
            val p = swipeRefresh?.layoutParams as ViewGroup.MarginLayoutParams?
            p?.topMargin = -statusBarSize
            swipeRefresh?.layoutParams = p
            swipeRefresh?.setPadding(0, statusBarSize, 0, 0)
            insets
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("main_rv", recyclerView?.layoutManager?.onSaveInstanceState())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            if (recyclerView?.layoutManager == null) recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable("main_rv"))
        }
    }

    override fun onAttach(context: Context) {
        retainInstance = true
        super.onAttach(context)
    }

    companion object {
        private var savedView: View? = null
    }
}