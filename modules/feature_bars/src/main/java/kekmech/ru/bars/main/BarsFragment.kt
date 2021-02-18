package kekmech.ru.bars.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import kekmech.ru.bars.R
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_mvi.DisposableDelegate
import kekmech.ru.common_mvi.DisposableDelegateImpl
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
import kekmech.ru.domain_notes.NotesFeatureLauncher
import kekmech.ru.domain_schedule.GROUP_NUMBER_PATTERN
import kekmech.ru.domain_schedule.ScheduleRepository
import org.koin.android.ext.android.inject

internal class BarsFragment : Fragment(R.layout.fragment_bars),
    DisposableDelegate by DisposableDelegateImpl() {

    private val scheduleRepository by inject<ScheduleRepository>()
    private val settingsFeatureLauncher by inject<AppSettingsFeatureLauncher>()
    private val notesFeatureLauncher by inject<NotesFeatureLauncher>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        // because view binding sucks, it don't see included layout id's
        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewSubtitle = view.findViewById<TextView>(R.id.textViewSubtitle)

        textViewTitle.setText(R.string.bars_stub_student_name)
        scheduleRepository.getSelectedScheduleName()
            .doOnSuccess {
                textViewSubtitle.text = if (it.matches(GROUP_NUMBER_PATTERN)) {
                    requireContext().getString(R.string.bars_stub_student_group, it)
                } else {
                    it
                }
            }
            .subscribe()
            .bind()
        view.findViewById<View>(R.id.buttonSettings).setOnClickListener {
            settingsFeatureLauncher.launch()
        }
        view.findViewById<View>(R.id.buttonNotes).setOnClickListener {
            notesFeatureLauncher.launchAllNotes()
        }
    }
}