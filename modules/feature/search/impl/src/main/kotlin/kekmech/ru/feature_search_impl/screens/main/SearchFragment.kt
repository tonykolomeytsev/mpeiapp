package kekmech.ru.feature_search_impl.screens.main

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.LabeledTextViewHolder
import kekmech.ru.coreui.items.LabeledTextViewHolderImpl
import kekmech.ru.coreui.items.NoteAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.ext_android.addSystemVerticalPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.hideKeyboard
import kekmech.ru.ext_android.notNullStringArg
import kekmech.ru.ext_android.showKeyboard
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.R
import kekmech.ru.feature_search_impl.databinding.FragmentSearchBinding
import kekmech.ru.feature_search_impl.di.SearchDependencies
import kekmech.ru.feature_search_impl.item.FilterAdapterItem
import kekmech.ru.feature_search_impl.item.MapMarkerAdapterItem
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEffect
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent
import kekmech.ru.feature_search_impl.screens.main.elm.SearchState
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsFragment
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.showDialog
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import money.vivid.elmslie.android.renderer.ElmRendererDelegate
import money.vivid.elmslie.android.renderer.androidElmStore
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.milliseconds
import kekmech.ru.coreui.R as coreui_R

private const val ARG_QUERY = "Arg.Query"
private const val ARG_FILTER = "Arg.Filter"
private const val DEFAULT_INPUT_DEBOUNCE = 300L

internal class SearchFragment : Fragment(R.layout.fragment_search),
    ElmRendererDelegate<SearchEffect, SearchState> {

    private val dependencies by inject<SearchDependencies>()
    private val adapter by fastLazy { createAdapter() }
    private val filterItemsAdapter by fastLazy { createFilterItemsAdapter() }
    private val analytics by screenAnalytics("Search")
    private val bottomTabsSwitcher by inject<BottomTabsSwitcher>()
    private val viewBinding by viewBinding(FragmentSearchBinding::bind)

    private val store by androidElmStore {
        dependencies.searchStoreFactory
            .create(
                query = notNullStringArg(ARG_QUERY),
                filter = notNullStringArg(ARG_FILTER)
            )
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemVerticalPadding()
        viewBinding.apply {
            navBackButton.setOnClickListener { close() }
            searchView.showKeyboard()
            viewLifecycleOwner.lifecycleScope.launch {
                searchView.observeChanges()
                    .debounce(DEFAULT_INPUT_DEBOUNCE.milliseconds)
                    .distinctUntilChanged()
                    .collect { text ->
                        store.accept(SearchEvent.Ui.Action.SearchContent(text))
                    }
            }
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

    fun EditText.observeChanges(): Flow<String> = callbackFlow {
        val watcher = addTextChangedListener { text ->
            trySend(text?.toString().orEmpty())
        }
        awaitClose { removeTextChangedListener(watcher) }
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
            layoutRes = coreui_R.layout.item_text_bottom_labeled,
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
            store.accept(SearchEvent.Ui.Action.SelectFilter(it))
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
