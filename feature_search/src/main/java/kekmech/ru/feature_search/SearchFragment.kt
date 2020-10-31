package kekmech.ru.feature_search

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.*
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.items.EmptyStateAdapterItem
import kekmech.ru.coreui.items.NoteAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.feature_search.di.SearchDependencies
import kekmech.ru.feature_search.item.MapMarkerAdapterItem
import kekmech.ru.feature_search.mvi.SearchEffect
import kekmech.ru.feature_search.mvi.SearchEvent
import kekmech.ru.feature_search.mvi.SearchFeature
import kekmech.ru.feature_search.mvi.SearchState
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

private const val ARG_QUERY = "Arg.Query"

internal class SearchFragment : BaseFragment<SearchEvent, SearchEffect, SearchState, SearchFeature>() {

    override val initEvent get() = SearchEvent.Wish.Init

    private val dependencies by inject<SearchDependencies>()

    override fun createFeature(): SearchFeature = dependencies.searchFeatureFactory
        .create(getArgument(ARG_QUERY))

    override var layoutId: Int = R.layout.fragment_search

    private val adapter by fastLazy { createAdapter() }

    private val analytics by inject<SearchAnalytics>()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
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
        analytics.sendScreenShown()
    }

    override fun render(state: SearchState) {
        adapter.update(SearchListConverter().map(state))
    }

    override fun handleEffect(effect: SearchEffect) = when (effect) {
        is SearchEffect.SetInitialQuery -> {
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
        NoteAdapterItem(requireContext()) { dependencies.notesFeatureLauncher.launchAllNotes(it) },
        MapMarkerAdapterItem(),
        EmptyStateAdapterItem()
    )

    companion object {

        fun newInstance(query: String) = SearchFragment()
            .withArguments(ARG_QUERY to query)
    }
}