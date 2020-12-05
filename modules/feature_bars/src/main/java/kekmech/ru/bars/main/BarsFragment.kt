package kekmech.ru.bars.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import kekmech.ru.bars.R
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleRepository
import org.koin.android.ext.android.inject

internal class BarsFragment : Fragment(R.layout.fragment_bars),
    DisposableDelegate by DisposableDelegateImpl() {

    private val scheduleRepository by inject<ScheduleRepository>()
    private val settingsFeatureLauncher by inject<AppSettingsFeatureLauncher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        // because view binding sucks, it don't see included layout id's
        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewSubtitle = view.findViewById<TextView>(R.id.textViewSubtitle)
        val buttonSettings = view.findViewById<View>(R.id.buttonSettings)

        textViewTitle.setText(R.string.bars_stub_student_name)
        scheduleRepository.getSelectedGroup()
            .doOnSuccess {
                textViewSubtitle.text = requireContext().getString(R.string.bars_stub_student_group, it)
            }
            .subscribe()
            .bind()
        buttonSettings.setOnClickListener {
            settingsFeatureLauncher.launch()
        }
    }
}