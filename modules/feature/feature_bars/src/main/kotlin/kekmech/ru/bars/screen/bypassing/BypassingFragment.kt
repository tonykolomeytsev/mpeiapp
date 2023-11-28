package kekmech.ru.bars.screen.bypassing

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.FragmentBypassingBinding
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.close
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.strings.Strings

internal class BypassingFragment : Fragment(R.layout.fragment_bypassing) {

    @Suppress("unused")
    private val analytics by screenAnalytics("Bypassing")
    private val viewBinding by viewBinding(FragmentBypassingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            appBarLayout.addSystemTopPadding()
            toolbar.setNavigationOnClickListener { close() }
            buttonContinue.setOnClickListener { openWebViewSettings() }
            root.addSystemBottomPadding()
        }
    }

    private fun openWebViewSettings() {
        val intent = Intent("com.android.webview.SHOW_DEV_UI").apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            setPackage("com.google.android.webview")
            putExtra("fragment-id", 2)
        }
        try {
            startActivity(intent)
        } catch (_: Exception) {
            Toast
                .makeText(requireContext(), Strings.bars_bypassing_error, Toast.LENGTH_SHORT)
                .show()
        }
    }
}
