package kekmech.ru.bars.screen.rating_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.bars.R
import kekmech.ru.bars.databinding.FragmentRatingDetailsBinding
import kekmech.ru.bars.screen.rating_details.item.CompositeRatingAdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.items.TextAdapterItem
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
        adapter.update(RatingDetailsListConverter(requireContext()).map(getArgument(ARG_RATING)))
    }

    private fun createAdapter() = BaseAdapter(
        TextAdapterItem(),
        SpaceAdapterItem(),
        CompositeRatingAdapterItem { /* no-op */ }
    )

    companion object {

        internal const val ITEM_ID_STUDY = 0
        internal const val ITEM_ID_SCIENCE = 1
        internal const val ITEM_ID_SOCIAL = 2
        private const val ARG_RATING = "ARG_RATING"

        fun newInstance(rating: Rating): Fragment =
            RatingDetailsFragment().withArguments(ARG_RATING to rating)
    }
}