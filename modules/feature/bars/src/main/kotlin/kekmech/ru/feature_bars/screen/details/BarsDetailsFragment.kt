package kekmech.ru.feature_bars.screen.details

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_bars.dto.AssessedDiscipline
import kekmech.ru.domain_bars.dto.FinalGradeType
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_bars.R
import kekmech.ru.feature_bars.databinding.FragmentBarsDetailsBinding
import kekmech.ru.feature_bars.items.ControlActivityAdapterItem
import kekmech.ru.feature_bars.items.FinalGradeAdapterItem
import kekmech.ru.library_adapter.BaseAdapter
import kekmech.ru.coreui.R as coreui_R

private const val BULLET_SEPARATOR = " • "
private const val ARG_DISCIPLINE_INFO = "Arg.Discipline"

class BarsDetailsFragment : BottomSheetDialogFragment(R.layout.fragment_bars_details) {

    private val analytics by screenAnalytics("BarsDetails")
    private val viewBinding by viewBinding(FragmentBarsDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            recyclerView.addSystemBottomPadding()
            recyclerView.adapter = adapter
            recyclerView.addScrollAnalytics(analytics, "BarsDetailsRecycler")
        }
        adapter.update(getItems(getArgument(ARG_DISCIPLINE_INFO)))
    }

    @Suppress("NestedBlockDepth")
    private fun getItems(discipline: AssessedDiscipline): List<Any> = mutableListOf<Any>().apply {
        add(PullItem)
        add(TextItem(text = discipline.name, styleResId = coreui_R.style.H2))
        add(SpaceItem.VERTICAL_4)
        add(
            TextItem(
                text = SpannableStringBuilder()
                    .append(
                        discipline.assessmentType,
                        ForegroundColorSpan(requireContext().getThemeColor(coreui_R.attr.colorBlack)),
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    .append(BULLET_SEPARATOR)
                    .append(discipline.person), styleResId = coreui_R.style.H4_Gray70
            )
        )
        add(SpaceItem.VERTICAL_12)
        addAll(discipline.controlActivities)
        add(SpaceItem.VERTICAL_16)
        discipline.finalGrades
            .filter { it.finalMark != -1f }
            .takeIf { it.isNotEmpty() }
            ?.let { grades ->
                add(SectionHeaderItem("Итоговые оценки"))
                add(SpaceItem.VERTICAL_12)
                grades.forEach {
                    if (it.type == FinalGradeType.INTERMEDIATE_MARK) {
                        add(SpaceItem.VERTICAL_16)
                    }
                    add(it)
                }
            }
        add(SpaceItem.VERTICAL_24)
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        SectionHeaderAdapterItem(),
        TextAdapterItem(),
        PullAdapterItem(),
        ControlActivityAdapterItem(),
        FinalGradeAdapterItem()
    )

    companion object {

        fun newInstance(assessedDiscipline: AssessedDiscipline) = BarsDetailsFragment()
            .withArguments(ARG_DISCIPLINE_INFO to assessedDiscipline)
    }
}
