package kekmech.ru.bars.screen.details

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.FragmentBarsDetailsBinding
import kekmech.ru.bars.items.ControlActivityAdapterItem
import kekmech.ru.bars.items.FinalGradeAdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_bars.dto.AssessedDiscipline

private const val BULLET_SEPARATOR = " • "
private const val ARG_DISCIPLINE_INFO = "Arg.Discipline"

class BarsDetailsFragment : BottomSheetDialogFragment() {

    override val layoutId: Int = R.layout.fragment_bars_details

    private val viewBinding by viewBinding(FragmentBarsDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            recyclerView.addSystemVerticalPadding()
            recyclerView.adapter = adapter
        }
        adapter.update(getItems(getArgument(ARG_DISCIPLINE_INFO)))
    }

    private fun getItems(discipline: AssessedDiscipline): List<Any> = mutableListOf<Any>().apply {
        add(PullItem)
        add(TextItem(text = discipline.name, styleResId = R.style.H2))
        add(TextItem(text = SpannableStringBuilder()
            .append(discipline.assessmentType, ForegroundColorSpan(requireContext().getThemeColor(R.attr.colorBlack)), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            .append(BULLET_SEPARATOR)
            .append(discipline.person), styleResId = R.style.H4_Gray70))
        add(SpaceItem.VERTICAL_12)
        addAll(discipline.controlActivities)
        add(SpaceItem.VERTICAL_16)
        discipline.finalGrades
            .filter { it.finalMark != -1f }
            .takeIf { it.isNotEmpty() }
            ?.let { grades ->
                add(SectionHeaderItem("Итоговые оценки"))
                add(SpaceItem.VERTICAL_12)
                addAll(grades)
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