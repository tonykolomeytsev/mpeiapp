package kekmech.ru.bars.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.bars.R
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_mvi.util.DisposableDelegate
import kekmech.ru.common_mvi.util.DisposableDelegateImpl
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleRepository
import kotlinx.android.synthetic.main.item_profile_header.*
import org.koin.android.ext.android.inject

internal class BarsFragment : Fragment(R.layout.fragment_bars),
    DisposableDelegate by DisposableDelegateImpl() {

    private val scheduleRepository by inject<ScheduleRepository>()

    private val settingsFeatureLauncher by inject<AppSettingsFeatureLauncher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
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