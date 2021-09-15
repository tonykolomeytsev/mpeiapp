package kekmech.ru.bars.screen.rating_details

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.FragmentRatingDetailsBinding
import kekmech.ru.bars.screen.main.util.DeclensionHelper
import kekmech.ru.bars.screen.rating_details.item.CompositeRatingAdapterItem
import kekmech.ru.bars.screen.rating_details.item.CompositeRatingItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.coreui.items.TextAdapterItem
import kekmech.ru.coreui.items.TextItem
import kekmech.ru.domain_bars.dto.Rating

internal class RatingDetailsFragment : Fragment(R.layout.fragment_rating_details) {

    private val binding by viewBinding(FragmentRatingDetailsBinding::bind)
    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemTopPadding()
        with(binding) {
            toolbar.setNavigationOnClickListener { close() }
            recycler.addSystemBottomPadding()
            recycler.adapter = adapter
        }
        adapter.update(map(getArgument(ARG_RATING)))
    }

    private fun map(rating: Rating): List<Any> =
        mutableListOf<Any>().apply {
            add(SpaceItem.VERTICAL_8)
            add(TextItem(
                textResId = R.string.bars_rating_complex_name,
                styleResId = R.style.H4_Gray70,
            ))

            val complexRatingPoints =
                DeclensionHelper.format(
                    declensions = requireContext().getStringArray(R.array.points_declensions),
                    n = rating.complex.toLong()
                )
            add(TextItem(
                text = SpannableStringBuilder()
                    .append(complexRatingPoints)
                    .apply {
                        setSpan(
                            ForegroundColorSpan(requireContext().getThemeColor(R.attr.colorActive)),
                            0,
                            rating.complex.toString().length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    },
                styleResId = R.style.H1
            ))
            add(SpaceItem.VERTICAL_16)

            val studyRatingPoints =
                DeclensionHelper.format(
                    declensions = requireContext().getStringArray(R.array.points_declensions),
                    n = rating.study.value.toLong()
                )
            add(CompositeRatingItem(
                nameResId = R.string.bars_rating_study_name,
                value = SpannableStringBuilder()
                    .append(studyRatingPoints)
                    .apply {
                        setSpan(
                            ForegroundColorSpan(requireContext().getThemeColor(R.attr.colorActive)),
                            0,
                            rating.complex.toString().length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    },
                weight = rating.study.weight
            ))
            val scienceRatingPoints =
                DeclensionHelper.format(
                    declensions = requireContext().getStringArray(R.array.points_declensions),
                    n = rating.science.value.toLong()
                )
            add(CompositeRatingItem(
                nameResId = R.string.bars_rating_science_name,
                value = SpannableStringBuilder()
                    .append(scienceRatingPoints)
                    .apply {
                        setSpan(
                            ForegroundColorSpan(requireContext().getThemeColor(R.attr.colorActive)),
                            0,
                            rating.complex.toString().length,
                            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    },
                weight = rating.science.weight
            ))
        }

    private fun createAdapter() = BaseAdapter(
        TextAdapterItem(),
        SpaceAdapterItem(),
        CompositeRatingAdapterItem { /* no-op */ }
    )

    companion object {

        private const val ARG_RATING = "ARG_RATING"

        fun newInstance(rating: Rating): Fragment =
            RatingDetailsFragment().withArguments(ARG_RATING to rating)
    }
}