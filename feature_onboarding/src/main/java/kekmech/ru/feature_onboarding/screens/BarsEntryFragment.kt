package kekmech.ru.feature_onboarding.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.close
import kekmech.ru.common_android.hideKeyboard
import kekmech.ru.feature_onboarding.R
import kotlinx.android.synthetic.main.fragment_bars_entry.*

class BarsEntryFragment : Fragment(R.layout.fragment_bars_entry) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        toolbar.setNavigationOnClickListener { close() }
        buttonSkip.setOnClickListener { }
        buttonStart.setOnClickListener { }
    }
}