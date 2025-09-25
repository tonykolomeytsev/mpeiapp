package kekmech.ru.feature_bars_impl.presentation.screen.details

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import kekmech.ru.coreui.items.PullAdapterItem
import kekmech.ru.coreui.items.PullItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SectionHeaderItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.coreui.items.TextItem
import kekmech.ru.ext_android.addSystemBottomPadding
import kekmech.ru.ext_android.fragment.BottomSheetDialogFragment
import kekmech.ru.ext_android.getThemeColor
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_bars_impl.R
import kekmech.ru.feature_bars_impl.databinding.FragmentBarsDetailsBinding
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.feature_bars_impl.domain.FinalGradeType
import kekmech.ru.feature_bars_impl.presentation.items.ControlActivityAdapterItem
import kekmech.ru.feature_bars_impl.presentation.items.FinalGradeAdapterItem
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.addScrollAnalytics
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
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
        adapter.update(getItems(notNullSerializableArg(ARG_DISCIPLINE_INFO)))
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
