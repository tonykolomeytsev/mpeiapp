package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.main.presentation.*
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish
import org.koin.android.ext.android.inject

class ScheduleFragment : BaseFragment<ScheduleEvent, ScheduleEffect, ScheduleState, ScheduleFeature>() {

    override val initEvent = Wish.Init

    override fun createFeature() = inject<ScheduleFeatureFactory>().value.create()

    override var layoutId = R.layout.fragment_schedule

    private val dependencies by inject<ScheduleDependencies>()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {

    }

    override fun handleEffect(effect: ScheduleEffect) {

    }

    override fun render(state: ScheduleState) {

    }
}