package kekmech.ru.feature_schedule.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.main.item.ClassesShimmerAdapterItem
import kekmech.ru.feature_schedule.main.item.HeaderAdapterItem
import kekmech.ru.feature_schedule.main.item.HeaderShimmerAdapterItem
import kekmech.ru.feature_schedule.main.presentation.*
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.koin.android.ext.android.inject

class ScheduleFragment : BaseFragment<ScheduleEvent, ScheduleEffect, ScheduleState, ScheduleFeature>() {

    override val initEvent = Wish.Init

    override fun createFeature() = inject<ScheduleFeatureFactory>().value.create()

    override var layoutId = R.layout.fragment_schedule

    private val dependencies by inject<ScheduleDependencies>()

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun handleEffect(effect: ScheduleEffect) = when (effect) {
        is ScheduleEffect.SelectDay -> Unit
        is ScheduleEffect.ShowWeekLoadingError -> showBanner(R.string.something_went_wrong_error)
    }

    override fun render(state: ScheduleState) {
        adapter.update(ScheduleListConverter.map(state))
    }

    private fun createAdapter() = BaseAdapter(
        HeaderShimmerAdapterItem(),
        ClassesShimmerAdapterItem(),
        HeaderAdapterItem(
            context = requireContext(),
            onWeekSelectListener = { feature.accept(Wish.Action.SelectWeek(it)) },
            onDaySelectListener = { feature.accept(Wish.Click.OnDayClick(it.date)) }
        )
    )
}