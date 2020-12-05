package kekmech.ru.feature_onboarding.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.close
import kekmech.ru.common_android.hideKeyboard
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withResultFor
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.common_webview.WebViewFragment
import kekmech.ru.domain_main_screen.MainScreenLauncher
import kekmech.ru.feature_onboarding.BarsEntryScreenAnalytics
import kekmech.ru.feature_onboarding.R
import kekmech.ru.feature_onboarding.databinding.FragmentBarsEntryBinding
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val REQUEST_CODE = 2910

internal class BarsEntryFragment : Fragment(R.layout.fragment_bars_entry) {

    private val mainScreenLauncher: MainScreenLauncher by inject()
    private val analytics: BarsEntryScreenAnalytics by inject()
    private val viewBinding by viewBinding(FragmentBarsEntryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()

        viewBinding.toolbar.setNavigationOnClickListener { close() }
        viewBinding.buttonSkip.setOnClickListener {
            analytics.sendClick("SkipLoginBars")
            mainScreenLauncher.launch()
            close()
        }
        viewBinding.buttonStart.setOnClickListener {
            analytics.sendClick("LoginBars")
            addScreenForward {
                WebViewFragment
                    .newInstance("https://bars.mpei.ru/bars_web/", enableJs = true)
                    .withResultFor(this, REQUEST_CODE)
            }
        }
        analytics.sendScreenShown()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("requestCode: $requestCode")
        super.onActivityResult(requestCode, resultCode, data)
    }
}