package kekmech.ru.feature_search

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.afterTextChanged
import kekmech.ru.common_android.close
import kekmech.ru.common_android.showKeyboard
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
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

internal class SearchFragment : BaseFragment<SearchEvent, SearchEffect, SearchState, SearchFeature>() {

    override val initEvent get() = SearchEvent.Wish.Init

    private val dependencies by inject<SearchDependencies>()

    override fun createFeature(): SearchFeature = dependencies.searchFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_search

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        navBackButton.setOnClickListener { close() }
        searchView.showKeyboard()
        searchView.observeChanges()
            .debounce(400, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { feature.accept(SearchEvent.Wish.Action.SearchContent(it)) }
            .bind()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    override fun render(state: SearchState) {
        adapter.update(SearchListConverter().map(state))
    }

    private fun EditText.observeChanges() = Observable.create<String> { emitter ->
        afterTextChanged { emitter.onNext(it) }
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        SectionHeaderAdapterItem(),
        NoteAdapterItem(requireContext()) { /* no-op */ },
        MapMarkerAdapterItem()
    )

    override fun onStop() {
        dispose()
        super.onStop()
    }
}