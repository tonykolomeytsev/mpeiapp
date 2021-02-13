package kekmech.ru.feature_search.schedule_details

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseBottomSheetDialogFragment
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.feature_search.R
import kekmech.ru.feature_search.databinding.FragmentScheduleDetailsBinding
import kekmech.ru.feature_search.schedule_details.mvi.*
import kekmech.ru.feature_search.schedule_details.mvi.ScheduleDetailsEffect
import kekmech.ru.feature_search.schedule_details.mvi.ScheduleDetailsEvent
import kekmech.ru.feature_search.schedule_details.mvi.ScheduleDetailsState
import org.koin.android.ext.android.inject

private const val ARG_RESULT_ITEM = "Arg.Item"
private const val ITEM_SHIMMER_ID = 0

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
    }

    override fun render(state: ScheduleDetailsState) {
        adapter.update(getItems(state))
    }

    private fun getItems(state: ScheduleDetailsState) = mutableListOf<Any>().apply {
        add(PullItem)
        add(TextItem(text = state.searchResult.name, styleResId = R.style.H3))
        add(SpaceItem.VERTICAL_12)
        add(TextItem(text = state.searchResult.description, styleResId = R.style.H6_Gray70))
        add(SpaceItem.VERTICAL_12)
        if (state.isLoading) {
            add(ShimmerItem(ITEM_SHIMMER_ID))
        } else {
            addDescriptionItem(state)
        }
    }

    private fun MutableList<Any>.addDescriptionItem(state: ScheduleDetailsState) {
        val thisWeekDescription: CharSequence = if (state.thisWeekSchedule!!.isNotEmpty()) {
            val adoptedNumber = adoptNumber(state.thisWeekSchedule.size)
            val fullString = getString(R.string.search_schedule_details_this_week_description, adoptedNumber)
            SpannableString.valueOf(fullString).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, adoptedNumber.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(ForegroundColorSpan(requireContext().getThemeColor(R.attr.colorMain)), 0, adoptedNumber.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
        } else {
            getString(R.string.search_schedule_details_this_week_description_empty)
        }
        val nextWeekDescription: CharSequence = if (state.nextWeekSchedule!!.isNotEmpty()) {
            val adoptedNumber = adoptNumber(state.nextWeekSchedule.size)
            val fullString = getString(R.string.search_schedule_details_next_week_description, adoptedNumber)
            SpannableString.valueOf(fullString).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, adoptedNumber.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                setSpan(ForegroundColorSpan(requireContext().getThemeColor(R.attr.colorMain)), 0, adoptedNumber.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
        } else {
            getString(R.string.search_schedule_details_next_week_description_empty)
        }
        val builder = SpannableStringBuilder()
        builder.append(thisWeekDescription)
        builder.append("\n")
        builder.append(nextWeekDescription)
        add(TextItem(
            text = builder,
            styleResId = R.style.H5
        ))
    }

    private fun adoptNumber(
        num: Int
    ): String = "$num " + when {
        num in 11..19 -> getString(R.string.search_schedule_details_classes_5_0)
        num % 10 == 1 -> getString(R.string.search_schedule_details_classes_1)
        num % 10 in 2..4 -> getString(R.string.search_schedule_details_classes_2_4)
        else -> getString(R.string.search_schedule_details_classes_5_0)
    }

    private fun createAdapter() = BaseAdapter(
        TextAdapterItem(),
        SpaceAdapterItem(),
        PullAdapterItem(),
        ShimmerAdapterItem(
            ITEM_SHIMMER_ID,
            layout = R.layout.item_text_shimmer
        )
    )

    companion object {

        fun newInstance(resultItem: SearchResult) = ScheduleDetailsFragment()
            .withArguments(ARG_RESULT_ITEM to resultItem)
    }
}