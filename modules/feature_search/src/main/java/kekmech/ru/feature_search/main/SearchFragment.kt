package kekmech.ru.feature_search.main

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.common_navigation.showDialog
import kekmech.ru.coreui.items.*
import kekmech.ru.domain_map.dto.MapMarker
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.feature_search.R
import kekmech.ru.feature_search.databinding.FragmentSearchBinding
import kekmech.ru.feature_search.di.SearchDependencies
import kekmech.ru.feature_search.item.FilterAdapterItem
import kekmech.ru.feature_search.item.MapMarkerAdapterItem
import kekmech.ru.feature_search.main.elm.SearchEffect
import kekmech.ru.feature_search.main.elm.SearchEvent
import kekmech.ru.feature_search.main.elm.SearchState
import kekmech.ru.feature_search.schedule_details.ScheduleDetailsFragment
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

private const val ARG_QUERY = "Arg.Query"
private const val ARG_FILTER = "Arg.Filter"
private const val DEFAULT_INPUT_DEBOUNCE = 300L

internal class SearchFragment : BaseFragment<SearchEvent, SearchEffect, SearchState>() {

    override val initEvent get() = SearchEvent.Wish.Init
    private val dependencies by inject<SearchDependencies>()

    override var layoutId: Int = R.layout.fragment_search
    private val adapter by fastLazy { createAdapter() }
    private val filterItemsAdapter by fastLazy { createFilterItemsAdapter() }
    private val analytics by screenAnalytics("Search")
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()
    private val viewBinding by viewBinding(FragmentSearchBinding::bind)

    override fun createStore() = dependencies.searchFeatureFactory
        .create(
            getArgument(ARG_QUERY),
            getArgument(ARG_FILTER)
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemVerticalPadding()
        viewBinding.apply {
            navBackButton.setOnClickListener { close() }
            searchView.showKeyboard()
            searchView.observeChanges()
                .debounce(DEFAULT_INPUT_DEBOUNCE, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { feature.accept(SearchEvent.Wish.Action.SearchContent(it)) }
                .let {}
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            filterItemsRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            filterItemsRecycler.adapter = filterItemsAdapter
        }
    }

    override fun render(state: SearchState) {
        filterItemsAdapter.update(state.filterItems)
        adapter.update(SearchListConverter().map(state))
    }

    override fun handleEffect(effect: SearchEffect) = when (effect) {
        is SearchEffect.SetInitialQuery -> with(viewBinding) {
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
        AdapterItem(
            isType = { it is SearchResult },
            layoutRes = R.layout.item_text_bottom_labeled,
            viewHolderGenerator = ::LabeledTextViewHolderImpl,
            itemBinder = object : BaseItemBinder<LabeledTextViewHolder, SearchResult>() {
                override fun bind(vh: LabeledTextViewHolder, model: SearchResult, position: Int) {
                    vh.setMainText(model.name)
                    vh.setLabel(model.description)
                    vh.setOnClickListener {
                        analytics.sendClick("ScheduleDetails")
                        openScheduleDetails(model)
                    }
                }
            }
        ),
        EmptyStateAdapterItem()
    )

    private fun createFilterItemsAdapter() = BaseAdapter(
        FilterAdapterItem {
            analytics.sendClick("SearchFilter_${it.type}")
            feature.accept(SearchEvent.Wish.Action.SelectFilter(it))
        }
    )

    private fun navigateToMapMarker(marker: MapMarker) {
        dependencies.mapFeatureLauncher.selectPlace(marker.uid)
        bottomTabsSwitcher.changeTab(BottomTab.MAP)
        viewBinding.searchView.hideKeyboard()
        close()
    }

    private fun openScheduleDetails(searchResult: SearchResult) {
        viewBinding.searchView.hideKeyboard()
        showDialog {
            ScheduleDetailsFragment.newInstance(searchResult)
        }
    }

    companion object {

        fun newInstance(query: String, filter: String = "ALL") = SearchFragment()
            .withArguments(
                ARG_QUERY to query,
                ARG_FILTER to filter
            )
    }
}