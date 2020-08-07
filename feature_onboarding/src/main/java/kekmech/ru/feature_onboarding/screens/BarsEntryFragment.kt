package kekmech.ru.feature_onboarding.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.close
import kekmech.ru.common_android.hideKeyboard
import kekmech.ru.common_android.withResultFor
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.common_webview.WebViewFragment
import kekmech.ru.feature_onboarding.R
import kotlinx.android.synthetic.main.fragment_bars_entry.*
import timber.log.Timber

private const val REQUEST_CODE = 2910

class BarsEntryFragment : Fragment(R.layout.fragment_bars_entry) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        toolbar.setNavigationOnClickListener { close() }
        buttonSkip.setOnClickListener { }
        buttonStart.setOnClickListener {
            addScreenForward {
                WebViewFragment
                    .newInstance("https://bars.mpei.ru/bars_web/", enableJs = true)
                    .withResultFor(this, REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("requestCode: $requestCode")
        super.onActivityResult(requestCode, resultCode, data)
    }
}