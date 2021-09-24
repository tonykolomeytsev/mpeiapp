package kekmech.ru.feature_search.schedule_details

import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.close
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseBottomSheetDialogFragment
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_schedule.items.ClassesItemBinder
import kekmech.ru.common_schedule.items.ClassesViewHolderImpl
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.feature_search.R
import kekmech.ru.feature_search.databinding.FragmentScheduleDetailsBinding
import kekmech.ru.feature_search.item.ButtonAdapterItem
import kekmech.ru.feature_search.item.WeekMinItem
import kekmech.ru.feature_search.item.WeekMinItemBinder
import kekmech.ru.feature_search.item.WeekMinViewHolder
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEffect
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Wish
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsFeatureFactory
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsState
import org.koin.android.ext.android.inject

private const val ARG_RESULT_ITEM = "Arg.Item"
internal const val ITEM_BUTTON_SWITCH = 1
internal const val ITEM_FAVORITES = 1

internal class ScheduleDetailsFragment :
    BaseBottomSheetDialogFragment<ScheduleDetailsEvent, ScheduleDetailsEffect,
            ScheduleDetailsState>() {

    private val viewBinding by viewBinding(FragmentScheduleDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }
    private val router by inject<Router>()
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()
    private val analytics by screenAnalytics("SearchScheduleDetails")

    override val initEvent: ScheduleDetailsEvent get() = Wish.Init
    override var layoutId: Int = R.layout.fragment_schedule_details

    override fun createStore() = inject<ScheduleDetailsFeatureFactory>().value
        .create(getArgument(ARG_RESULT_ITEM))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
        }
        setFixedSizeWorkaround()
    }

    private fun setFixedSizeWorkaround() {
        val display = requireActivity().windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        viewBinding.recyclerView.minimumHeight = point.y
    }

    override fun render(state: ScheduleDetailsState) {
        adapter.update(ScheduleDetailsListConverter.map(state, requireContext()))
    }

    override fun handleEffect(effect: ScheduleDetailsEffect) = when (effect) {
        is ScheduleDetailsEffect.CloseAndGoToSchedule -> {
            close()
            router.executeCommand(ClearBackStack())
            bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
        }
    }

    private fun createAdapter() = BaseAdapter(
        TextAdapterItem(),
        SpaceAdapterItem(),
        PullAdapterItem(),
        ShimmerAdapterItem(R.layout.item_text_shimmer),
        ShimmerAdapterItem(R.layout.item_week_shimmer),
        ShimmerAdapterItem(R.layout.item_classes_shimmer),
        ButtonAdapterItem(ITEM_BUTTON_SWITCH, R.layout.item_button) {
            analytics.sendClick("SwitchSchedule")
            feature.accept(Wish.Click.SwitchSchedule)
        },
        TextWithIconAdapterItem {
            when (it.itemId) {
                ITEM_FAVORITES -> {
                    analytics.sendClick("AddToFavorites")
                    feature.accept(Wish.Click.Favorites)
                }
                else -> Unit
            }
        },
        AdapterItem(
            isType = { it is WeekMinItem },
            layoutRes = R.layout.item_week_min,
            viewHolderGenerator = ::WeekMinViewHolder,
            itemBinder = WeekMinItemBinder(requireContext()) {
                feature.accept(Wish.Click.Day(it.date))
            },
            areItemsTheSame = { _, _ -> true }
        ),
        AdapterItem(
            isType = { it is Classes },
            layoutRes = R.layout.item_classes_padding_horisontal_16dp,
            viewHolderGenerator = ::ClassesViewHolderImpl,
            itemBinder = ClassesItemBinder(requireContext()) { /* no-op */ }
        ),
        EmptyStateAdapterItem()
    )

    companion object {

        fun newInstance(resultItem: SearchResult) = ScheduleDetailsFragment()
            .withArguments(ARG_RESULT_ITEM to resultItem)
    }
}