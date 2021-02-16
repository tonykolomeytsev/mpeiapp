package kekmech.ru.feature_search.schedule_details

import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseBottomSheetDialogFragment
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
import kekmech.ru.feature_search.item.WeekMinViewHolderImpl
import kekmech.ru.feature_search.schedule_details.mvi.*
import org.koin.android.ext.android.inject

private const val ARG_RESULT_ITEM = "Arg.Item"
internal const val ITEM_TEXT_SHIMMER_ID = 0
internal const val ITEM_WEEK_SHIMMER_ID = 1
internal const val ITEM_CLASSES_SHIMMER_ID = 2
internal const val ITEM_BUTTON_SWITCH = 1
internal const val ITEM_FAVORITES = 1

internal class ScheduleDetailsFragment : BaseBottomSheetDialogFragment<ScheduleDetailsEvent, ScheduleDetailsEffect, ScheduleDetailsState, ScheduleDetailsFeature>() {

    private val viewBinding by viewBinding(FragmentScheduleDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }

    override val initEvent: ScheduleDetailsEvent get() = ScheduleDetailsEvent.Wish.Init
    override var layoutId: Int = R.layout.fragment_schedule_details

    override fun createFeature() = inject<ScheduleDetailsFeatureFactory>().value
        .create(getArgument(ARG_RESULT_ITEM))

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
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

    private fun createAdapter() = BaseAdapter(
        TextAdapterItem(),
        SpaceAdapterItem(),
        PullAdapterItem(),
        ShimmerAdapterItem(ITEM_TEXT_SHIMMER_ID, R.layout.item_text_shimmer),
        ShimmerAdapterItem(ITEM_WEEK_SHIMMER_ID, R.layout.item_week_shimmer),
        ShimmerAdapterItem(ITEM_CLASSES_SHIMMER_ID, R.layout.item_classes_shimmer),
        ButtonAdapterItem(ITEM_BUTTON_SWITCH, R.layout.item_button_primary) { /* no-op */ },
        TextWithIconAdapterItem {
            when (it.itemId) {
                ITEM_FAVORITES -> feature.accept(ScheduleDetailsEvent.Wish.Click.Favorites)
                else -> Unit
            }
        },
        AdapterItem(
            isType = { it is WeekMinItem },
            layoutRes = R.layout.item_week_min,
            viewHolderGenerator = ::WeekMinViewHolderImpl,
            itemBinder = WeekMinItemBinder(requireContext()) {
                feature.accept(ScheduleDetailsEvent.Wish.Click.Day(it.date))
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