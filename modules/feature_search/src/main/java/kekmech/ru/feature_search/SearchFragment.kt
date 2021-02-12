package kekmech.ru.feature_search

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.unit
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.feature_search.databinding.FragmentSearchBinding
import kekmech.ru.feature_search.di.SearchDependencies
import kekmech.ru.feature_search.item.FilterAdapterItem
import kekmech.ru.feature_search.item.MapMarkerAdapterItem
import kekmech.ru.feature_search.mvi.SearchEffect
import kekmech.ru.feature_search.mvi.SearchEvent
import kekmech.ru.feature_search.mvi.SearchFeature
import kekmech.ru.feature_search.mvi.SearchState
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

private const val ARG_QUERY = "Arg.Query"
private const val ARG_FILTER = "Arg.Filter"

internal class SearchFragment : BaseFragment<SearchEvent, SearchEffect, SearchState, SearchFeature>() {

    override val initEvent get() = SearchEvent.Wish.Init
    private val dependencies by inject<SearchDependencies>()

    override fun createFeature(): SearchFeature = dependencies.searchFeatureFactory
        .create(
            getArgument(ARG_QUERY),
            getArgument(ARG_FILTER)
        )

    override var layoutId: Int = R.layout.fragment_search
    private val adapter by fastLazy { createAdapter() }
    private val filterItemsAdapter by fastLazy { createFilterItemsAdapter() }
    private val analytics by inject<SearchAnalytics>()
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()
    private val viewBinding by viewBinding(FragmentSearchBinding::bind)

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        viewBinding.apply {
            navBackButton.setOnClickListener { close() }
            searchView.showKeyboard()
            searchView.observeChanges()
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { feature.accept(SearchEvent.Wish.Action.SearchContent(it)) }
                .let {}
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            filterItemsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            filterItemsRecycler.adapter = filterItemsAdapter
        }
        analytics.sendScreenShown()
    }

    override fun render(state: SearchState) {
        filterItemsAdapter.update(state.filterItems)
        adapter.update(SearchListConverter().map(state))
    }

    override fun handleEffect(effect: SearchEffect) = when (effect) {
        is SearchEffect.SetInitialQuery -> viewBinding.unit {
            searchView.setText(effect.query)
            searchView.setSelection(effect.query.length)
        }
    }

    private fun EditText.observeChanges() = Observable.create<String> { emitter ->
        afterTextChanged { emitter.onNext(it) }
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        SectionHeaderAdapterItem(),
        NoteAdapterItem(requireContext()) {
            analytics.sendClick("SearchNote")
            dependencies.notesFeatureLauncher.launchAllNotes(it)
        },
        MapMarkerAdapterItem {
            analytics.sendClick("SearchMarker")
            navigateToMapMarker(it)
        },
        BottomLabeledTextAdapterItem {
            /* no-op */
        },
        EmptyStateAdapterItem()
    )

    private fun createFilterItemsAdapter() = BaseAdapter(
        FilterAdapterItem {
            feature.accept(SearchEvent.Wish.Action.SelectFilter(it))
        }
    )

    private fun navigateToMapMarker(marker: MapMarker) {
        dependencies.mapFeatureLauncher.selectPlace(marker.uid)
        bottomTabsSwitcher.changeTab(BottomTab.MAP)
        viewBinding.searchView.hideKeyboard()
        close()
    }

    companion object {

        fun newInstance(query: String, filter: String = "ALL") = SearchFragment()
            .withArguments(
                ARG_QUERY to query,
                ARG_FILTER to filter
            )
    }
}